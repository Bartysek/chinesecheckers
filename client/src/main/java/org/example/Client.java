package org.example;

import java.io.IOException;
import java.net.Socket;

public class Client {
  private Socket connection;

  public static void main(String[] args) {

  }

  public Client(String ip, int port) {
    try {
      connection = new Socket(ip, port);
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

  private void printMsg(byte[] msg) {
    for (byte b : msg) {
      System.out.print((char)b);
    }
  }

  private void printBoardState(byte[] state){
    //TODO
  }

  public void run() {
    while(connection.isConnected()){
      //TODO
    }
  }
}
