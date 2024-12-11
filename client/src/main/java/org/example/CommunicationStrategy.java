package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public abstract class CommunicationStrategy {
  protected final static int BYTES_IN_MOVE_PACKET = 4;

  public abstract void handle(InputStream in, OutputStream out, Board board) throws IOException;
}

class ReceiveMessage extends CommunicationStrategy {
  @Override
  public void handle(InputStream in, OutputStream out, Board board) throws IOException{
    StringBuilder sb = new StringBuilder();
    int b;
    while ((b = in.read()) != CommunicationIndicators.END_OF_MESSAGE.getCode()) {
      sb.append((char)b);
    }
    System.out.println(sb);
  }
}

class ReceiveMove extends  CommunicationStrategy {
  @Override
  public void handle(InputStream in, OutputStream out, Board board) throws IOException{
    int[] receivedMessage = new int[BYTES_IN_MOVE_PACKET];
    for (int i=0; i<BYTES_IN_MOVE_PACKET; i++) {
      receivedMessage[i] = in.read();
    }
    board.move(receivedMessage[0], receivedMessage[1], receivedMessage[2], receivedMessage[3]);
    board.print();
  }
}

class ReceiveQuestion extends CommunicationStrategy {

  @Override
  public void handle(InputStream in, OutputStream out, Board board) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter number of players:");
    out.write(scanner.nextInt());
  }
}

class ReceiveTurn extends CommunicationStrategy {
  private byte[] requestMove() {
    byte[] content = new byte[BYTES_IN_MOVE_PACKET];
    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter start position row number:");
    content[0] = (byte)scanner.nextInt();
    System.out.println("Enter start position diagonal number:");
    content[1] = (byte)scanner.nextInt();
    System.out.println("Enter end position row number:");
    content[2] = (byte)scanner.nextInt();
    System.out.println("Enter end position diagonal number:");
    content[3] = (byte)scanner.nextInt();

    return content;
  }

  @Override
  public void handle(InputStream in, OutputStream out, Board board) throws IOException {
    out.write(requestMove());
  }
}

class WrongStrategy extends CommunicationStrategy {

  @Override
  public void handle(InputStream in, OutputStream out, Board board){
    System.out.println("Received nonsense byte");
  }
}
