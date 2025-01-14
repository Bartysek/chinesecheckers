package org.example;

import java.io.IOException;
import java.io.OutputStream;

public interface BoardControl {

    public void setOut(int BYTES_IN_MOVE_PACKET, OutputStream out);
    public void sendMove(byte[] content) throws IOException;
    public void clickSquare(Square square);
}
