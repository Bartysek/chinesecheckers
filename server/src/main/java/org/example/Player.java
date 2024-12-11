package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Player implements PlayerInterface {
  //transmission indicators
  private static final int MOVE_INDICATOR = 255;
  private static final int MESSAGE_INDICATOR = 254;
  private static final int END_OF_MESSAGE = 253;
  private static final int QUESTION_INDICATOR = 252;
  private static final int THEIR_TURN_INDICATOR = 251;

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

  @Override
  public void closeSocket() {
      try {
          socket.close();
      } catch (IOException e) {
          System.err.println("Socket close error");
      }
  }

  @Override
  public void sendMove(final int x1, final int y1, final int x2, final int y2) {
    try {
      out.write(MOVE_INDICATOR);
      out.write(x1);
      out.write(y1);
      out.write(x2);
      out.write(y2);
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

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

  @Override
  public int queryNumPlayers() {
    try {
      out.write(QUESTION_INDICATOR);
      int pick = in.read();
      return switch (pick) { //"enchanced switch statement" wg intellij
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

  @Override
  public void sendTheirTurn() {
    try {
      out.write(THEIR_TURN_INDICATOR);
    } catch (IOException e) {
      System.err.println("IO exception");
      //TODO they should be handled sometime later
    }
  }

}
