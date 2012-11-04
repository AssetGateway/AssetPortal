package asset.portal.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class UserRegistry {

	private Map<String, User> users = new HashMap<String, User>();
	private ReentrantLock usersLock = new ReentrantLock();

	public void register(User user) {
		if(user == null) {
			return;
		}
		this.usersLock.lock();
		try {
			this.users.put(user.getName(), user);
		} finally {
			this.usersLock.unlock();
		}
	}

	public void unregister(User user) {
		this.usersLock.lock();
		try {
			this.users.remove(user);
		} finally {
			this.usersLock.unlock();
		}
	}

	public void addAll(Collection<User> users) {
		for(User user : users) {
			this.register(user);
		}
	}
	
	public User getByName(String name) {
		return this.users.get(name);
	}

	public Collection<User> getAll() {
		return this.users.values();
	}
	
	public boolean hasName(String name) {
		return this.users.containsKey(name);
	}

	public void clear() {
		this.users.clear();
	}
	
}
