package org.example;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Player implements PlayerInterface{
  //transmission indicators
  private final static int MOVE_INDICATOR = 255;
  private final static int MESSAGE_INDICATOR = 254;
  private final static int END_OF_MESSAGE = 253;
  private final static int QUESTION_INDICATOR = 252;
  private final static int THEIR_TURN_INDICATOR = 251;


  private final static int BYTES_IN_MOVE_PACKET = 4;

  private OutputStream out;
  private InputStream in;

  /**
   * setup in and out streams
   * @param connection
   */
  public Player(Socket connection){
    try {
      this.out = connection.getOutputStream();
      this.in = connection.getInputStream();
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

  @Override
  public void sendMove(int x1, int y1, int x2, int y2) {
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
  public void sendMessage(String content) {
    try {
      byte[] contentBytes = content.getBytes();
      for(byte b : contentBytes) {
        if( b < 0 ) {
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
  public byte[] listen() throws IOException{
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
      System.err.println("IO exception");//TODO they should be handled sometime later
    }
  }

}
