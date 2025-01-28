package org.example;

import entities.StoredMove;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class PlaybackPlayer implements PlayerInterface{
  private int playerNumber;
  private Set<StoredMove> moves;
  private Iterator<StoredMove> it;

  public PlaybackPlayer(int playerNumber, Set<StoredMove> moves) {
    this.playerNumber = playerNumber;
    this.moves = moves;
    it = new TreeSet<>(moves).iterator();
  }

  @Override
  public void closeSocket() {}

  @Override
  public void sendBoardState(int size, int[][] state) {}

  @Override
  public void removePiece(int[] pieceInfo) {}

  @Override
  public void addPiece(int[] pieceInfo) {}

  @Override
  public void sendMessage(String content) {}

  @Override
  public byte[] listen() throws IOException {
    byte[] move = new byte[4];
    StoredMove play;
    do {
      if (!it.hasNext()) throw new IOException();
      play = it.next();
    } while (play.getPlayer() != playerNumber);
    System.out.print("move_number = ");
    System.out.println(play.getMoveNum());
    move[0] = (byte)play.getY1();
    move[1] = (byte)play.getX1();
    move[2] = (byte)play.getY2();
    move[3] = (byte)play.getX2();
    return move;
  }

  @Override
  public int queryNumPlayers() {
    return 0;
  }

  @Override
  public AbstractRules queryGameRules() {
    return null;
  }

  @Override
  public void sendTheirTurn() {

  }

  @Override
  public void sendEndOfMove(int playerNum) {

  }
}
