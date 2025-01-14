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

class ReceiveMove extends  CommunicationStrategy {
  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    int[] receivedMessage = new int[BYTES_IN_MOVE_PACKET];
    for (int i = 0; i < BYTES_IN_MOVE_PACKET; i++) {
      receivedMessage[i] = in.read();
    }
    board.move(receivedMessage[0], receivedMessage[1], receivedMessage[2], receivedMessage[3]);
    board.bv.showBoard(board);
  }
}

class ReceiveQuestion extends CommunicationStrategy {

  @Override
  public void handle(final InputStream in, final OutputStream out, final Board board) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter number of players:");
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
