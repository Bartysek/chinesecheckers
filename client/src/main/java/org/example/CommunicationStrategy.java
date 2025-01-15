package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * collection of classes that handle one conversation with the server
 */
public abstract class CommunicationStrategy {
  protected static final int BYTES_IN_MOVE_PACKET = 4;
  protected static final int BYTES_IN_NUM_PLAYERS_PACKET = 1;
  protected static final int BYTES_IN_GAME_MODE_PACKET = 1;
  //protected static final BoardVisualizer BOARD_VISUALIZER = new AwtBoardVisualizer();

  public abstract void handle(InputStream in, OutputStream out, Board board) throws IOException;
}

class ReceiveBoardState extends CommunicationStrategy {
  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    int size = in.read();
    int[][] state = new int[4 * size - 3][4 * size - 3];
    for(int i = 0; i < size * 4 - 3; i++) {
      for(int j = 0; j < size * 4 - 3; j++) {
        state[i][j] = in.read();
      }
    }
    board.setState(state);
    board.bv.showBoard(board);
  }
}

class ReceiveMessage extends CommunicationStrategy {
  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    StringBuilder sb = new StringBuilder();
    int b;
    while ((b = in.read()) != CommunicationIndicators.END_OF_MESSAGE.getCode()) {
      sb.append((char) b);
    }
    System.out.println(sb);
  }
}

class ReceivePieceRemove extends CommunicationStrategy {
  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    int[] receivedMessage = new int[2];
    for (int i = 0; i < 2; i++) {
      receivedMessage[i] = in.read();
    }
    board.remove(receivedMessage[0], receivedMessage[1]);
  }
}

class ReceivePieceAdd extends CommunicationStrategy {
  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    int[] receivedMessage = new int[3];
    for (int i = 0; i < 3; i++) {
      receivedMessage[i] = in.read();
    }
    board.add(receivedMessage[0], receivedMessage[1], receivedMessage[2]);
  }
}

class ReceiveEndOfMove extends CommunicationStrategy {
  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    board.bv.showBoard(board);
    board.bv.notYourTurn();
  }
}

class ReceivePlayerCountQuestion extends CommunicationStrategy {

  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    board.bc.requestNumPlayers(BYTES_IN_NUM_PLAYERS_PACKET, out);
  }
}

class ReceiveGameModeQuestion extends CommunicationStrategy {

  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    board.bc.requestGameMode(BYTES_IN_GAME_MODE_PACKET, out);
  }
}

class ReceiveTurn extends CommunicationStrategy {
  private void requestMove(Board board, OutputStream out) throws IOException {
    board.bc.requestMove(BYTES_IN_MOVE_PACKET, out);
    board.bv.yourTurn();
  }

  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    requestMove(board, out);
  }
}

class ErrorStrategy extends CommunicationStrategy {
  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    throw new IOException();
  }
}

class WrongStrategy extends CommunicationStrategy {

  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) {
    System.out.println("Received nonsense byte");
  }
}
