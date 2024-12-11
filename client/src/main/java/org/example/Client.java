package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

  private InputStream in;
  private OutputStream out;

  private boolean maintainConnection = true;

  private Board board;


  public static void main(final String[] args) {
    new Client("127.0.0.1", 25560);
  }

  /**
   * constructing the client object also runs the connection.
   * @param ip ip to connect to
   * @param port port to connect to
   */
  public Client(final String ip, final int port) {
    try {
      Socket connection = new Socket(ip, port);
      this.in = connection.getInputStream();
      this.out = connection.getOutputStream();
      this.board = new Board();
    } catch (IOException e) {
      maintainConnection = false;
      System.err.println("No server to connect to on " + ip + ":" + port);
    }
    run();
  }

  public void run() {
    while (maintainConnection) {
      try {
        int b = in.read();
        CommunicationStrategy currentStrategy = StrategyFactory.getStrategy(b);
        currentStrategy.handle(in, out, board);
      } catch (IOException e) {
        maintainConnection = false;
        System.err.println("Server can't be reached.");
      }
    }
  }


}
