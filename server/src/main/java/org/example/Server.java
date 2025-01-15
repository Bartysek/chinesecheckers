package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A server. It hosts games and manages connecting players.
 */
public final class Server {
  /** a singleton of the Server */
  private static final Server SERVER_INSTANCE = new Server();

  private ServerSocket serverSocket;
  /** these players have not been assigned to a game yet. */
  private final ArrayList<Player> waitingPlayers = new ArrayList<>();
  /** A hosted game. This version supports only one game per server. */
  private Game game;

  private Server() { }

  /** getter for the Server singleton */
  public static Server getInstance() {
    return SERVER_INSTANCE;
    //game will most likely need access to waitingPlayers or Game, therefore server should be accessible
  }

  /**
   * main, starts the server
   * @param args ignored
   */
  public static void main(String[] args) {
    try {
      new Server().start(25560);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** new connection handler */
  private final Thread collectConnections = new Thread(new Runnable() {
    @Override
    public void run() {
      while (true) {
        try {
          Socket newConn = serverSocket.accept();
          synchronized (waitingPlayers) {
            waitingPlayers.add(new Player(newConn));
          }
          System.out.println("New connection");
        } catch (IOException e) {
          System.err.println("IO exception");
        }
      }
    }
  });

  /** Waiting player handler, connects them to the game. */
  private final Thread addToGame = new Thread(new Runnable() {

    @Override
    public void run() {
      while (true) {
        try {
          Thread.sleep(100); //busyloop, ale bez niego nie wysyła się wiadomość
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        if (!game.isFull() && !waitingPlayers.isEmpty()) {
          PlayerInterface p;
          synchronized (waitingPlayers) {
            p = waitingPlayers.removeFirst();
          }
          System.out.println("Added player to game");
          game.addPlayer(p);
        }
      }
    }
  });

  /**
   * starts the server, listens for connections, starts games and delegates players to games.
   * @param port
   * @throws IOException if a ServerSocket throws IOException
   */
  public void start(final int port) throws IOException {
    serverSocket = new ServerSocket(port);
    collectConnections.start();
    game = new Game();
    addToGame.start();
  }
}
