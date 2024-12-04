package org.example;

import java.io.IOException;
import java.net.Socket;

public class Client {
  private Socket connection;

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

  /**
   * used to visualize the received board state for the user
   * @param state board state to be visualized
   */
  private void printBoardState(byte[] state){
    //TODO to powinno być zrobione za pomocą factory, aby można było wyświetlać różne plansze (nwm czy to będzie potrzebne)
  }

  public void run() {
    while(connection.isConnected()){
      //TODO odbieranie wiadomości i wysyłanie jeśli otrzyma magiczny bit twojej tury
    }
  }
}
