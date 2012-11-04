package asset.portal.command;

import asset.portal.command.create.CreateCommand;
import asset.portal.command.create.CreateListener;
import asset.portal.command.impl.DeleteCommand;
import asset.portal.command.impl.ListCommand;
import asset.portal.gate.GateRegistry;

public class PortalCommandExecutor extends CommandRegistryExecutor {

	public PortalCommandExecutor(GateRegistry gateRegistry, CreateListener createListener) {
		this.submit(new CreateCommand(gateRegistry, createListener));
		this.submit(new DeleteCommand(gateRegistry));
		this.submit(new ListCommand(gateRegistry));
	}
	
	public String getId() {
		return "portal";
	}

}

