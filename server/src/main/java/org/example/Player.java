package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Player {
  //transmission indicators
  private final static byte BOARD_STATE_INDICATOR = (byte)255;
  private final static byte MESSAGE_INDICATOR = (byte)254;
  private final static byte END_OF_MESSAGE = (byte)253;

  private final static int BYTES_IN_MOVE_PACKET = 4; //TODO zamienić na ile move packet rzeczywiście ma bajtów

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

  /**
   * @param content state of the board
   * @param isHisTurn signals the client to send his move
   */
  public void sendBoardState(byte[] content, boolean isHisTurn) {
    byte turnByte = isHisTurn ? (byte)1 : (byte)0;
    byte[] magicBytes = {BOARD_STATE_INDICATOR, turnByte};
    try {
      out.write(magicBytes);
      out.write(content);
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

  /**
   *
   * @param content
   */
  public void sendMessage(char[] content){
    byte[] startOfMsg = {MESSAGE_INDICATOR};
    byte[] sentMessage = new byte[content.length];
    byte[] endOfMsg = {END_OF_MESSAGE};
    for(int i = 0; i < content.length; i++){
      sentMessage[i] = (byte)content[i];
    }
    try {
      out.write(startOfMsg);
      out.write(sentMessage);
      out.write(endOfMsg);
    } catch (IOException e) {
      System.err.println("IO exception");
    }
  }

  /**
   * @return move sent by a client
   * @throws IOException
   */
  public byte[] listen() throws IOException{
    return in.readNBytes(BYTES_IN_MOVE_PACKET); //TODO być może klasa reprezentująca move byłaby na miejscu, gdybyśmy chcieli zrobić większą planszę albo inną grę.
  }

}
