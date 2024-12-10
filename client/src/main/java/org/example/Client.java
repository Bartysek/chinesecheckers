package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
  private final static int MESSAGE_INDICATOR = 254;
  private final static int MOVE_INDICATOR = 255;
  private final static int END_OF_MESSAGE = 253;

  private final static int BYTES_IN_MOVE_PACKET = 4;

  private Socket connection;
  private InputStream in;
  private OutputStream out;

  private Board board;

  public static void main(String[] args) {

  }

  /**
   * constructing the client object also runs the connection
   * @param ip ip to connect to
   * @param port port to connect to
   */
  public Client(String ip, int port) {
    try {
      connection = new Socket(ip, port);
      this.in = connection.getInputStream();
      this.out = connection.getOutputStream();
    } catch (IOException e) {
      System.err.println("IO exception");
    }
    run();
  }

  /**
   *
   * @param msg
   */
  private void printMsg(byte[] msg) {
    for (byte b : msg) {
      System.out.print((char)b);
    }
  }

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

  private void sendMove(byte[] content) {
    //send move to the server
    try {
      out.write(content);
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

  public void run() {
    while(connection.isConnected()){
      try {
        int b = in.read();

        //
        if (b == MESSAGE_INDICATOR) {
          byte[] receivedMessage = new byte[1024];
          int i = 0;
          while ((b = in.read()) != END_OF_MESSAGE) {
            receivedMessage[i] = (byte)b;
            i++;
          }
          printMsg(receivedMessage);
        }

        else if (b == MOVE_INDICATOR) {
          int isHisTurn = in.read();
          int[] receivedMessage = new int[BYTES_IN_MOVE_PACKET];
          for (int i=0; i<BYTES_IN_MOVE_PACKET; i++) {
            receivedMessage[i] = in.read();
          }
          board.move(receivedMessage[0], receivedMessage[1], receivedMessage[2], receivedMessage[3]);
          board.print();
          if (isHisTurn == 1) {
            sendMove(requestMove());
          }
        }

      } catch (IOException e) {
        System.err.println("IO exception");
      }
      //TODO odbieranie wiadomości i wysyłanie jeśli otrzyma magiczny bit twojej tury
    }
  }


}
