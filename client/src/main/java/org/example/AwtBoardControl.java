package org.example;

import java.io.IOException;
import java.io.OutputStream;

public class AwtBoardControl implements BoardControl {
    OutputStream outputStream;
    int BYTES_IN_MOVE_PACKET;
    private boolean moveMode = false;

    private Square firstChosen;

    @Override
    public void setOut(int BYTES_IN_MOVE_PACKET, OutputStream out) {
        outputStream = out;
        this.BYTES_IN_MOVE_PACKET = BYTES_IN_MOVE_PACKET;
        moveMode = true;
    }

    @Override
    public void sendMove(byte[] content) throws IOException {
        outputStream.write(content);
        moveMode = false;
    }

    @Override
    public void clickSquare(Square square) {
        if (moveMode) {
            if (firstChosen == null) {
                firstChosen = square;
                firstChosen.markChosen();
            } else {
                byte[] content = new byte[BYTES_IN_MOVE_PACKET];
                content[0] = (byte) firstChosen.getI();
                content[1] = (byte) firstChosen.getJ();
                content[2] = (byte) square.getI();
                content[3] = (byte) square.getJ();
                try {
                    sendMove(content);
                } catch (IOException e) {
                    System.err.println("IOException when sending move");
                }
                firstChosen.unmarkChosen();
                firstChosen = null;
            }
        }
    }
}
