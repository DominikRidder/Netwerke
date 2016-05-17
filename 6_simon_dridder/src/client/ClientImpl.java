package client;

import java.util.Scanner;

import de.root1.simon.Lookup;
import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;
import enums.State;
import server.ServerInterface;

@SimonRemote
public class ClientImpl implements ClientCallbackInterface {

	Lookup nameLookup;
	ServerInterface server;
	Scanner sc;
	int[] computerMove;

	public static void main(String[] args) {
		ClientImpl client = new ClientImpl();
		client.runGame();
	}

	/**
	 * Call this Method, to start the game.
	 */
	public void runGame() {
		boolean keepPlaying;
		sc = new Scanner(System.in);

		if (connectToServer()) { // Connection to Server worked
			do {
				server.login(this); // Maybe not the best solution for
									// refreshing everything
				keepPlaying = false;

				clearConsole();
				System.out.println("Enter a Number from 1-9 to set a Turn.\n");
				System.out.println("\t1 2 3");
				System.out.println("\t4 5 6");
				System.out.println("\t7 8 9\n");

				gameLoop();

				// Result
				System.out.println(server.getState().toString()
						.replace("_", ": "));

				// Keep Playing?
				System.out.println("\nDo you want to keep gaming?[Y/n]");
				keepPlaying = sc.nextLine().trim().toLowerCase().equals("y");

			} while (keepPlaying);
		} else {
			System.out.println("ERROR: Could not connect to the Server.");
		}
		nameLookup.release(server);
		sc.close();
	}

	/**
	 * Method that connects the Client with the Server. Note that the nameLookup
	 * should be closed later if the connection should be closed.
	 * 
	 * @return true if the connection is working; false otherwise
	 */
	private boolean connectToServer() {
		boolean connected = false;

		try {
			nameLookup = Simon.createNameLookup("localhost");
			server = (ServerInterface) nameLookup.lookup("server");
			// server.login(this);

			connected = true; // if we reach this line, the connection should
								// work
		} catch (Exception e) {
			e.printStackTrace();
		}

		return connected;
	}

	/**
	 * The Client gameLoop.
	 */
	private void gameLoop() {
		System.out.println(server.getStringBoard());

		while (server.getState() == State.NOT_DONE) {

			// Human turn
			doTurn();
			clearConsole();

			// Board
			System.out.println(server.getStringBoard());

			// Computer turn
			if (computerMove != null) {
				System.out.println("Computer set field "
						+ (computerMove[0] * 3 + computerMove[1] + 1) + "\n");
				computerMove = null;
			}
		}
	}

	/**
	 * Method for a Human turn.
	 */
	private void doTurn() {
		int row, column, idx;

		while (true) { // Turn
			System.out.println("Enter a Turn");
			try {
				idx = Integer.parseInt(sc.nextLine().trim());
			} catch (NumberFormatException e) {
				continue;
			}
			row = (idx - 1) / 3;
			column = (idx - 1) % 3;

			if (server.place(row, column) == State.FIELD_NOT_EMPTY) {
				System.out.println("Invalid turn: Field is not Empty.");
			} else {
				break;
			}
		}
	}

	/**
	 * Called by the Server. Move is the turn of the AI with move = {row,
	 * column}.
	 */
	public void callback(int[] move) {
		computerMove = move;
	}

	/**
	 * Clears the Console by printing a lot of newlines.
	 */
	public void clearConsole() {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n"); // like the Unix clear
														// command, since \b is
														// buggy in some
														// Eclipse version
	}

}
