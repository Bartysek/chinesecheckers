package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
  private Game game = null;

  private GameDAO dao;

  private Server() { }

  /** getter for the Server singleton */
  public static Server getInstance() {
    return SERVER_INSTANCE;
  }

  public GameDAO getDao() {
    return dao;
  }

  public Game getGame() {
    return game;
  }

  /**
   * main, starts the server
   * @param args ignored
   */
  public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
    Server instance = Server.getInstance();
    instance.dao = (GameDAO) ctx.getBean("Dao");


    try {
      instance.start(25560);
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
          Player p = new Player(newConn);
          synchronized (waitingPlayers) {
            waitingPlayers.add(p);
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
        if (!waitingPlayers.isEmpty()) {
          if (game == null) {
            Player gamemaster = waitingPlayers.getFirst();
            setGame(gamemaster.queryGameLoad(getDao()));
          } else if (!game.isFull()) {
            PlayerInterface p;
            System.out.println("Added player to game");
            synchronized (waitingPlayers) {
              p = waitingPlayers.removeFirst();
            }

            game.addPlayer(p);
          }
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
    addToGame.start();
  }

  public void setGame(Game game) {
    this.game = game;
  }
}
