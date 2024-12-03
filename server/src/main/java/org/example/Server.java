package org.example;

import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class Server {
  private ServerSocket serverSocket;
  private final ArrayList<Player> waitingPlayers = new ArrayList<>();

  private final Thread collectConnections = new Thread(new Runnable() {
    @Override
    public void run() {
      while(true) {
        try {
          Socket newConn = serverSocket.accept();
          synchronized (waitingPlayers) {
            waitingPlayers.add(new Player(newConn));
          }
        } catch (IOException e) {
          System.err.println("IO exception");
        }
      }
    }
  });

  public void start(int port) throws IOException{
    serverSocket = new ServerSocket(port);
    collectConnections.start();
  }
}
