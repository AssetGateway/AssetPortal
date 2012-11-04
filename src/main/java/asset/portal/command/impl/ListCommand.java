package asset.portal.command.impl;

import org.bukkit.entity.Player;

import asset.portal.command.Command;
import asset.portal.command.CommandPermissionException;
import asset.portal.command.CommandSyntaxException;
import asset.portal.gate.Gate;
import asset.portal.gate.GateRegistry;
import asset.portal.util.MessageConstants;
import asset.portal.util.PermissionConstants;
import asset.portal.util.StringUtils;

public class ListCommand implements Command {

	private GateRegistry gateRegistry;
	
	public ListCommand(GateRegistry gateRegistry) {
		this.gateRegistry = gateRegistry;
	}
	
	public void execute(Player player, String[] args) throws CommandPermissionException, CommandSyntaxException {
		if(!player.hasPermission(PermissionConstants.PORTAL_LIST)) {
			throw new CommandPermissionException(PermissionConstants.PORTAL_LIST);
		}
		String[] gates = new String[this.gateRegistry.getAll().size()];
		int i = 0;
		for(Gate gate : this.gateRegistry.getAll()) {
			gates[i++] = gate.getDestinationServer();
		}
		player.sendMessage(MessageConstants.format(MessageConstants.GATE_LIST, StringUtils.concat(gates, ", ")));
	}

	public String getId() {
		return "list";
	}

}
