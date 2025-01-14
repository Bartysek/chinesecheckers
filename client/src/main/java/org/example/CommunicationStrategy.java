package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public abstract class CommunicationStrategy {
  protected static final int BYTES_IN_MOVE_PACKET = 4;
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
  }
}

class ReceivePlayerCountQuestion extends CommunicationStrategy {

  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter number of players:");
    out.write(scanner.nextInt());
  }
}

class ReceiveGameModeQuestion extends CommunicationStrategy {

  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter game mode:");
    out.write(scanner.nextInt());
  }
}

class ReceiveTurn extends CommunicationStrategy {
  private void requestMove(Board board, OutputStream out) throws IOException {
    board.bc.setOut(BYTES_IN_MOVE_PACKET, out);
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
