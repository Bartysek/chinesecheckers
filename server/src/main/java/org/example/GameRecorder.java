package org.example;

import entities.GameInfo;
import entities.StoredMove;
import entities.StoredStatePiece;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder implements PlayerInterface{
  GameInfo storedGame;
  private final GameDAO db;
  private List<Integer> removedPieces = new ArrayList<>();
  private List<Integer> addedPieces = new ArrayList<>();

  public GameRecorder(GameInfo save, GameDAO db) {
    storedGame = save;
    this.db = db;
  }

  @Override
  public void closeSocket() {
    db.saveGame(storedGame);
  }

  @Override
  public void sendBoardState(int size, int[][] state) {
    for(int i = 0; i < 4*size-3; i++) {
      for(int j = 0; j < 4*size-3; j++) {
        StoredStatePiece piece = new StoredStatePiece(storedGame, i, j, state[i][j]);
        storedGame.getInitialState().add(piece);
      }
    }
  }

  @Override
  public void removePiece(int[] pieceInfo) {
    removedPieces.add(pieceInfo[0]);
    removedPieces.add(pieceInfo[1]);
  }

  @Override
  public void addPiece(int[] pieceInfo) {
    addedPieces.add(pieceInfo[0]);
    addedPieces.add(pieceInfo[1]);
    addedPieces.add(pieceInfo[2]);
  }

  @Override
  public void sendMessage(String content) {
    if (content.matches("Player [0-9]* won!")) {
      int winner = 0;
      for(int i = 7; '9' >= content.charAt(i) && content.charAt(i) >= '0'; i++){
        winner *= 10;
        winner += content.charAt(i) - 48;
      }
      storedGame.setWinner(winner);
    }
  }

  @Override
  public byte[] listen() throws IOException {
    byte[] ret = {0, 0, 0, 0};
    return ret;
  }

  @Override
  public int[] queryGameSettings() {
    return new int[0];
  }

  @Override
  public Game queryGameLoad(GameDAO dao) {
    return null;
  }

  @Override
  public void sendTheirTurn() {}

  @Override
  public void sendEndOfMove(int playerNum) {
    int y1 = removedPieces.removeLast();
    int x1 = removedPieces.removeLast();
    addedPieces.removeLast();
    int y2 = addedPieces.removeLast();
    int x2 = addedPieces.removeLast();
    StoredMove move = new StoredMove(storedGame, storedGame.progressMoveNum(), x1, y1, x2, y2, playerNum); //adds itself where needed
    storedGame.getMoves().add(move);
  }
}
