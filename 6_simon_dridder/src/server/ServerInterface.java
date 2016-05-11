package server;

import client.ClientCallbackInterface;
import enums.State;
 
public interface ServerInterface {
 
  /**
  * login in for telling the server your callback and reset the board
  *
  * @param clientCallbackInterface
  *            your Client-Implementation
  */
  void login(ClientCallbackInterface clientCallbackInterface);
 
  /**
  * convenience method for getting the string representation of the board
  *
  * @param board
  * @return
  */
  String getStringBoard();
 
  /**
  * place your move
  *
  * @param row
  *            of your move
  * @param column
  *            of your move
  * @return if [row][column]!=0 -> State.FIELD_NOT_EMPTY otherwise
  *         WINNER_{KI,HUMAN}, DRAW or NOT_DONE
  */
  State place(int row, int column);
 
  /**
  * get the current game state
  *
  * @return game state
  */
  State getState();
 
}