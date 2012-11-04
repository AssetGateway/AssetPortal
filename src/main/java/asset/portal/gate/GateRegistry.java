package asset.portal.gate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Location;

public class GateRegistry {

	private Map<String, Gate> gates = new HashMap<String, Gate>();
	private ReentrantLock gatesLock = new ReentrantLock();

	public void register(Gate gate) {
		if(gate == null) {
			return;
		}
		this.gatesLock.lock();
		try {
			this.gates.put(gate.getDestinationServer(), gate);
		} finally {
			this.gatesLock.unlock();
		}
	}

	public void unregister(Gate gate) {
		this.gatesLock.lock();
		try {
			this.gates.remove(gate.getDestinationServer());
		} finally {
			this.gatesLock.unlock();
		}
	}

	public void addAll(Collection<Gate> gates) {
		for(Gate gate : gates) {
			this.register(gate);
		}
	}
	
	public Gate getByDestinationServer(String destinationServer) {
		return this.gates.get(destinationServer);
	}

	public Gate getByLocation(Location location) {
		this.gatesLock.lock();
		try {
			for(Gate gate : this.getAll()) {
				if(!gate.isInside(location)) {
					continue;
				}
				return gate;
			}
		} finally {
			this.gatesLock.unlock();
		}
		return null;
	}

	public Collection<Gate> getAll() {
		return this.gates.values();
	}
	
	public boolean hasByDestinationServer(String destinationServer) {
		return this.gates.containsKey(destinationServer);
	}

	public void clear() {
		this.gates.clear();
	}

}
