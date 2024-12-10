package org.example;

import java.io.IOException;

public interface PlayerInterface {
  /**
   *
   * @param x1 coordinates of the piece to move
   * @param y1 coordinates of the piece to move
   * @param x2 coordinates to which to move the piece
   * @param y2 coordinates to which to move the piece
   * @param isHisTurn
   */
  public void sendMove(int x1, int y1, int x2, int y2, boolean isHisTurn);

  public void sendMessage(String content);

  public byte[] listen() throws IOException;

  public int queryNumPlayers();
}
