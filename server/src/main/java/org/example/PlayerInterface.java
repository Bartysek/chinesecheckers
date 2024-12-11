package org.example;

import java.io.IOException;

public interface PlayerInterface {
  void closeSocket();

  /**
   *
   * @param x1 coordinates of the piece to move
   * @param y1 coordinates of the piece to move
   * @param x2 coordinates to which to move the piece
   * @param y2 coordinates to which to move the piece
   */
   void sendMove(int x1, int y1, int x2, int y2);

   void sendMessage(String content);

   byte[] listen() throws IOException;

   int queryNumPlayers();

   void sendTheirTurn();
}
