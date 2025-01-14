package org.example;

import java.io.IOException;

public interface PlayerInterface {
  /** disconnect this player */
  void closeSocket();

  /**
   * send the complete state of the board to this player
   * @param size edge length of the hexagon in the center of the board, in tiles
   * @param state the state
   */
  void sendBoardState(int size, int[][] state);

  /**
   * tell the player which piece should be replaced by an empty tile
   * @param pieceInfo x and y coordinates
   */
  void removePiece(int[] pieceInfo);

  /**
   * tell the player which piece should be put on a tile
   * @param pieceInfo x, y and the piece number
   */
  void addPiece(int[] pieceInfo);

  void sendMessage(String content);

  /**
   * listen for a move
   * @return 4 bytes: x1, y1, x2 and y2 of a move
   * @throws IOException
   */
  byte[] listen() throws IOException;

  /**
   * @return number of players to play in this game
   */
  int queryNumPlayers();

  /**
   * @return rules of this game
   */
  RulesInterface queryGameRules();

  /** tell the player to move */
  void sendTheirTurn();

  /** tell the player he has up-to-date board state */
  void sendEndOfMove();
}
