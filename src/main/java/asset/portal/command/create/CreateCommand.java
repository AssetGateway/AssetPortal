package asset.portal.command.create;

import org.bukkit.entity.Player;

import asset.portal.command.Command;
import asset.portal.command.CommandPermissionException;
import asset.portal.command.CommandSyntaxException;
import asset.portal.gate.GateRegistry;
import asset.portal.util.MessageConstants;
import asset.portal.util.PermissionConstants;

public class CreateCommand implements Command {

	private GateRegistry gateRegistry;
	private CreateListener createListener;
	
	public CreateCommand(GateRegistry gateRegistry, CreateListener createListener) {
		this.gateRegistry = gateRegistry;
		this.createListener = createListener;
	}
	
	public void execute(Player player, String[] args) throws CommandPermissionException, CommandSyntaxException {
		if(!player.hasPermission(PermissionConstants.PORTAL_CREATE)) {
			throw new CommandPermissionException(PermissionConstants.PORTAL_CREATE);
		}
		if(args.length < 1) {
			throw new CommandSyntaxException("destinationServer");
		}
		String destinationServer = args[0];
		if(this.gateRegistry.hasByDestinationServer(destinationServer)) {
			player.sendMessage(MessageConstants.format(MessageConstants.CREATE_ALREADY_EXISTS, destinationServer));
			return;
		}
		this.createListener.submitSession(new CreateSession(player, destinationServer));
		player.sendMessage(MessageConstants.format(MessageConstants.CREATE_STEP_1));
	}

	public String getId() {
		return "create";
	}

}
