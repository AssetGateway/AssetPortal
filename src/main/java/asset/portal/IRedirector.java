package asset.portal;

import org.bukkit.entity.Player;

import asset.portal.gate.Gate;

public interface IRedirector {

	public void requestRedirect(Player player, Gate gate);
	
	public void respondRedirect(Player player, String server);

	public void redirect(String username, String server);
	
	public boolean redirectResult(String username, String server);
	
	public void redirectLastServer(String username, String server);
	
}
