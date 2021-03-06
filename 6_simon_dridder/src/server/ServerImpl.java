package server;

import java.io.IOException;

import client.ClientCallbackInterface;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;
import de.root1.simon.exceptions.NameBindingException;
import enums.State;

@SimonRemote
public class ServerImpl implements ServerInterface {

	static final int NONE = 0;
	static final int HUMAN = 1;
	static final int COMPUTER = 2;

	ClientCallbackInterface client;
	int[][] board;

	@Override
	public void login(ClientCallbackInterface clientCallbackInterface) {
		board = new int[3][3];
		client = clientCallbackInterface;
	}

	@Override
	public String getStringBoard() {
		StringBuilder board_str = new StringBuilder();
		
		// First creating a String like:
		//
		//    0 1 2
		//    0 0 0
		//    1 0 1
		//
		// And then replacing the Numbers with the String
		// Representation:
		//
		//    - x o
		//    - - -
		//    x - o
		
		board_str.append("\t");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board_str.append(board[i][j]);
				board_str.append(" ");
			}
			board_str.append("\n\t");
		}

		return board_str.toString().replace(Character.forDigit(HUMAN, 10), 'x')
				.replace(Character.forDigit(COMPUTER, 10), 'o')
				.replace("0", "-");
	}

	@Override
	public State place(int row, int column) {
		if (getState() == State.NOT_DONE) { // game already over?
			if (row > 2 || column > 2 || row < 0 || column < 0) { // position in
																	// grid?
				return getState();
			}

			if (board[row][column] != NONE) { // position empty?
				return State.FIELD_NOT_EMPTY;
			}

			board[row][column] = HUMAN;

			if (getState() == State.NOT_DONE) { // if NOT_DONE: placeAI
				placeAI();
			}
		}

		return getState();
	}

	@Override
	public State getState() {
		int winner = NONE;
		boolean draw = false;

		// Check rows
		row: for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length - 1; j++) {
				if (board[i][j] != board[i][j + 1]) {
					continue row;
				}
			}
			winner = board[i][0];
		}

		// Check Columns
		if (winner == NONE) {
			column: for (int i = 0; i < board[0].length; i++) {
				for (int j = 0; j < board.length - 1; j++) {
					if (board[j][i] != board[j + 1][i]) {
						continue column;
					}
				}
				winner = board[0][i];
			}
		}

		// Check Diagonal
		if (winner == NONE) {
			if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
				winner = board[1][1];
			} else if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
				winner = board[1][1];
			}
		}

		// Check Draw
		if (winner == NONE) {
			int check_draw = board[0][0];

			if (check_draw == 0) {
				draw = false;
			} else {
				draw = true;
				row: for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[0].length; j++) {
						if (board[i][j] != check_draw) {
							draw = false;
							break row;
						}
					}
				}
			}
		}

		if (draw) {
			return State.DRAW;
		}

		switch (winner) {
		case HUMAN:
			return State.WINNER_HUMAN;
		case COMPUTER:
			return State.WINNER_AI;
		default:
			return State.NOT_DONE;
		}
	}

	public static void main(String[] agrs) {
		try {
			Registry registry = Simon.createRegistry();
			registry.start();
			registry.bind("server", new ServerImpl());
		} catch (NameBindingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void placeAI() {
		int[] move = new int[2];
		do {
			move[0] = (int) (Math.random() * 3);
			move[1] = (int) (Math.random() * 3);
		} while (board[move[0]][move[1]] != NONE);

		board[move[0]][move[1]] = COMPUTER;
		client.callback(move);
	}
}
