package org.example;

import java.io.IOException;

public interface PlayerInterface {
  void closeSocket();

  void sendBoardState(int size, int[][] state);

  void removePiece(int[] pieceInfo);

  void addPiece(int[] pieceInfo);

  void sendMessage(String content);

  byte[] listen() throws IOException;

  int queryNumPlayers();

  RulesInterface queryGameRules();

  void sendTheirTurn();

  void sendEndOfMove();
}
