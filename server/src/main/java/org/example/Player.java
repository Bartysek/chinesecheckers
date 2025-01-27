package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * a class representing a player connecting over a socket
 */
public class Player implements PlayerInterface {
  //transmission indicators
  private static final int BOARD_STATE_INDICATOR = 255;
  private static final int MESSAGE_INDICATOR = 254;
  private static final int END_OF_MESSAGE = 253;
  private static final int PLAYER_COUNT_QUESTION_INDICATOR = 252;
  private static final int THEIR_TURN_INDICATOR = 251;
  private static final int RULES_QUESTION_INDICATOR = 250;
  private static final int PIECE_REMOVE_INDICATOR = 249;
  private static final int PIECE_ADD_INDICATOR = 248;
  private static final int END_MOVE_INDICATOR = 247;

  private static final int BYTES_IN_MOVE_PACKET = 4;

  private Socket socket;
  private OutputStream out;
  private InputStream in;

  /**
   * setup in and out streams.
   * @param connection
   */
  public Player(final Socket connection) {
    try {
      this.socket = connection;
      this.out = connection.getOutputStream();
      this.in = connection.getInputStream();
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

  /**
   * disconnects the player
   */
  @Override
  public void closeSocket() {
      try {
          socket.close();
      } catch (IOException e) {
          System.err.println("Socket close error");
      }
  }

  /**
   * sends the full state of the board
   * @param size edge length of the hexagon in the center of the board, in tiles
   * @param state the state
   */
  @Override
  public void sendBoardState(int size, int[][] state) {
    try {
      out.write(BOARD_STATE_INDICATOR);
      out.write(size);
      for (int i = 0; i < 4 * size - 3; i++) {
        for (int j = 0; j < 4 * size - 3; j++) {
          out.write(state[i][j]);
        }
      }
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

  /**
   * sends info about a piece that should be replaced by an empty tile
   * @param pieceInfo x and y coordinates
   */
  @Override
  public void removePiece(int[] pieceInfo) {
    try {
      out.write(PIECE_REMOVE_INDICATOR);
      out.write(pieceInfo[0]); //x
      out.write(pieceInfo[1]); //y
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

  /**
   * sends info about a piece that should be changed
   * @param pieceInfo x, y and the piece number
   */
  @Override
  public void addPiece(int[] pieceInfo) {
    try {
      out.write(PIECE_ADD_INDICATOR);
      out.write(pieceInfo[0]); //x
      out.write(pieceInfo[1]); //y
      out.write(pieceInfo[2]); //piece
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

  /**
   * sends a text message to the player
   * @param content the message
   */
  @Override
  public void sendMessage(final String content) {
    try {
      byte[] contentBytes = content.getBytes();
      for (byte b : contentBytes) {
        if (b < 0) {
          System.err.println("Bad character in a string, dropping message.");
          return;
        }
      }
      out.write(MESSAGE_INDICATOR);
      out.write(contentBytes);
      out.write(END_OF_MESSAGE);
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

  /**
   * @return move sent by a client
   * @throws IOException
   */
  public byte[] listen() throws IOException {
    return in.readNBytes(BYTES_IN_MOVE_PACKET);
  }

  /**
   * asks the player about the number of players
   * @return the answer
   */
  @Override
  public int queryNumPlayers() {
    try {
      out.write(PLAYER_COUNT_QUESTION_INDICATOR);
      int pick = in.read();
      return switch (pick) {
        case 2, 3, 4, 6 -> pick;
        default -> {
          sendMessage("Wrong number.");
          yield queryNumPlayers();
        }
      };
    } catch (IOException e) {
      System.err.println("IO exception");
    }
    return -1;
  }

  /**
   * asks the player about the rules of the game
   * @return the answer
   */
  public AbstractRules queryGameRules() {
    try {
      out.write(RULES_QUESTION_INDICATOR);
      int pick = in.read();
      AbstractRules rules = RulesFactory.getRules(pick);
      if(rules == null) {
        sendMessage("These rules Don't exist");
        return queryGameRules();
      }
      return rules;
    } catch (IOException e) {
      System.err.println("IO exception");
    }
    return null;
  }

  /**
   * sends the player a signal to start their move
   */
  @Override
  public void sendTheirTurn() {
    try {
      out.write(THEIR_TURN_INDICATOR);
    } catch (IOException e) {
      System.err.println("IO exception");
      //TODO they should be handled sometime later
    }
  }

  /**
   * sends the player a signal they have the correct state of the board
   */
  @Override
  public void sendEndOfMove(int playerNum) {
    try {
      out.write(END_MOVE_INDICATOR);
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

}
