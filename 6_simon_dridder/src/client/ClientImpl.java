package client;

import java.net.UnknownHostException;
import java.util.Scanner;

import de.root1.simon.Lookup;
import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;
import enums.State;
import server.ServerInterface;

@SimonRemote
public class ClientImpl implements ClientCallbackInterface {

	Lookup nameLookup;
	ServerInterface server;

	public static void main(String[] args) {
		ClientImpl client = new ClientImpl();
		client.runGame();
	}

	public void runGame() {

		if (connectToServer()) { // Connection to Server worked
			
			System.out.println("Enter a Number from 1-9 to set a Turn.\n");
			System.out.println("1 2 3");
			System.out.println("4 5 6");
			System.out.println("7 8 9\n");

			gameLoop();

			System.out.println(server.getState().toString().replace("_", ": "));
			nameLookup.release(server);
		} else {
			System.out.println("ERROR: Could not connect to the Server.");
		}
	}

	private boolean connectToServer() {
		boolean connected = false;

		try {
			nameLookup = Simon.createNameLookup("localhost");
			server = (ServerInterface) nameLookup.lookup("server");
			server.login(this);

			connected = true; // if we reach this line, the connection should
								// work
		} catch (Exception e) {
			e.printStackTrace();
		}

		return connected;
	}

	private void gameLoop() {
		Scanner sc = new Scanner(System.in);
		int row, column, idx;

		System.out.println(server.getStringBoard());

		while (server.getState() == State.NOT_DONE) {
			while (true) {
				System.out.println("Enter a Turn");
				idx = Integer.parseInt(sc.nextLine().trim());
				row = (idx - 1) / 3;
				column = (idx - 1) % 3;

				if (server.place(row, column) == State.FIELD_NOT_EMPTY) {
					System.out.println("Invalid turn: Field is not Empty.");
				} else {
					break;
				}
			}
			System.out.println(server.getStringBoard());
		}

		sc.close();
	}

	public void callback(int[] move) {

	}

}
