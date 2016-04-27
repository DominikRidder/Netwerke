package server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bsp.OwnStackImplementation;
import client.IChatClientCallback;

public class ChatServerImpl extends UnicastRemoteObject implements IChatServer {

	private static final long serialVersionUID = 1L;
	private static final int registryPort = 1099;

	// Hier speichern wir die Callbacks!
	private Map<String, IChatClientCallback> users;

	private ChatServerImpl() throws RemoteException {
		super();
		HashMap<String, IChatClientCallback> callbackHashMap = new HashMap<>();
		users = Collections.synchronizedMap(callbackHashMap);
	}

	public boolean login(String userID, IChatClientCallback receiver) throws RemoteException {
		if (users.containsKey(userID)) {
			return false;
		}

		users.put(userID, receiver);
		Object[] users_list = users.keySet().toArray();

		for (IChatClientCallback user : users.values()) {
			user.receiveUserLogin(userID, users_list);
		}

		return true;
	}

	public void logout(String userID) throws RemoteException {
		if (!users.containsKey(userID)) {
			return;
		}

		users.remove(userID);
		Object[] users_list = users.keySet().toArray();

		for (IChatClientCallback user : users.values()) {
			user.receiveUserLogout(userID, users_list);
		}
	}

	public void chat(String userID, String message) throws RemoteException {
		for (IChatClientCallback user : users.values()) {
			user.receiveChat(userID, message);
		}
	}

	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(registryPort);
			Naming.bind("rmi://localhost/queue", new ChatServerImpl());
			System.out.println("ChatServer ready");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public boolean pn(String fromID, String toID, String message) throws RemoteException {
		if (users.containsKey(toID)) {
			users.get(toID).whisper(fromID, message);
			return true;

		} else {
			return false;
		}
	}
}