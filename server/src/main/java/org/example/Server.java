package org.example;

import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class Server {
  private ServerSocket serverSocket;
  /** these players have not been assigned to a game yet */
  private final ArrayList<Player> waitingPlayers = new ArrayList<>();
  private Game game;

  /** always listen for new connections */
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

  private final Thread addToGame = new Thread(new Runnable() {

    @Override
    public void run() {
      while(true) {
        //TODO add players from waitingPlayers to Game
        
      }
    }
  });

  /**
   * starts the server, listens for connections, starts games and delegates players to games
   * @param port
   * @throws IOException if a ServerSocket throws IOException
   */
  public void start(int port) throws IOException{
    serverSocket = new ServerSocket(port);
    collectConnections.start();
    game = new Game();


    //TODO
  }
}
