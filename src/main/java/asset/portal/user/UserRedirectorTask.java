package asset.portal.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import asset.portal.IRedirector;
import asset.portal.Task;
import asset.portal.util.MessageConstants;

public class UserRedirectorTask extends Task {

	private String username;
	private String server;
	private IRedirector redirector;
	private boolean notified;
	
	public UserRedirectorTask(String username, String server, IRedirector redirector) {
		this.username = username;
		this.server = server;
		this.redirector = redirector;
	}
	
	public void run() {
		Player player = Bukkit.getServer().getPlayerExact(this.username);
		if(player == null) {
			this.cancelTask();
			return;
		}
		if(!this.redirector.redirectResult(this.username, this.server) && !this.notified) {
			for(String message : MessageConstants.format(MessageConstants.SERVER_OFFLINE_REDIRECT)) {
				player.sendMessage(message);
			}
			this.notified = true;
		}
	}
	
}
