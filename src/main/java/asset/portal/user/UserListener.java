package asset.portal.user;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import asset.connect.api.Connect;
import asset.connect.api.DirectEvent;
import asset.connect.api.DirectEventListener;
import asset.portal.IConnector;
import asset.portal.IRedirector;
import asset.portal.gate.Gate;
import asset.portal.gate.GateRegistry;

public class UserListener implements Listener, DirectEventListener {

	private Plugin plugin;
	private GateRegistry gateRegistry;
	private UserRegistry userRegistry;
	private IConnector connector;
	private IRedirector redirector;
	
	public UserListener(Plugin plugin, GateRegistry gateRegistry, UserRegistry userRegistry, IConnector connector, IRedirector redirector) {
		this.plugin = plugin;
		this.gateRegistry = gateRegistry;
		this.userRegistry = userRegistry;
		this.connector = connector;
		this.redirector = redirector;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
		final Player player = playerJoinEvent.getPlayer();
		User user = this.getUser(player.getName());
		String fromServer = user.getFromServer();
		if(fromServer == null) {
			if(user.getServer().equals(this.connector.getConnect().getSettings().getUsername())) {
				return;
			}
			this.redirector.redirectLastServer(user.getName(), user.getServer());
			return;
		}
		this.redirector.respondRedirect(player, fromServer);
		user.setServer(this.connector.getConnect().getSettings().getUsername());
		user.setFromServer(null);
		final Gate gate = this.gateRegistry.getByDestinationServer(fromServer);
		if(gate == null) {
			return;
		}
		this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
			public void run() {
				player.teleport(new Location(Bukkit.getServer().getWorld(gate.getOutwardWorld()), gate.getOutwardX(), gate.getOutwardY(), gate.getOutwardZ(), gate.getOutwardYaw(), 0));
			}
		}, 1L);
	}

	public void onDirect(Connect connect, DirectEvent directEvent) {
		String[] message = directEvent.getMessage().trim().split(" ");
		if(message.length != 3) {
			return;
		}
		if(!message[0].equals("PORTAL")) {
			return;
		}
		if(message[1].equals("REQUEST")) {
			this.redirector.redirect(message[2], this.connector.getConnect().getSettings().getUsername());
			this.getUser(message[2]).setFromServer(directEvent.getUsername());
		} else if(message[1].equals("RESPONSE")) {
			this.getUser(message[2]).setServer(directEvent.getUsername());
		}
	}
	
	public User getUser(String name) {
		if(!this.userRegistry.hasName(name)) {
			this.userRegistry.register(new User(name, this.connector.getConnect().getSettings().getUsername()));
		}
		return this.userRegistry.getByName(name);
	}
	
}
