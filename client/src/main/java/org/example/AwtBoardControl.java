package org.example;

import java.io.IOException;
import java.io.OutputStream;

public class AwtBoardControl implements BoardControl {
    OutputStream outputStream;
    final private SettingsPanel settingsPanel;
    int bytesInPacket;
    private boolean moveMode = false;
    //private boolean settingsMode = false;

    private Square firstChosen;

    AwtBoardControl(SettingsPanel settingsPanel) {
        this.settingsPanel = settingsPanel;
    }

    @Override
    public void requestMove(int bytesInPacket, OutputStream out) {
        outputStream = out;
        this.bytesInPacket = bytesInPacket;
        moveMode = true;
    }

    @Override
    public void requestNumPlayers(int bytesInPacket, OutputStream out) {
        outputStream = out;
        this.bytesInPacket = bytesInPacket;
        settingsPanel.setVisible(true);
        settingsPanel.setNumPlayers();
    }

    @Override
    public void requestGameMode(int bytesInPacket, OutputStream out) {
        outputStream = out;
        this.bytesInPacket = bytesInPacket;
        settingsPanel.setGameMode();
    }

    @Override
    public void sendOut(byte[] content) throws IOException {
        outputStream.write(content);
    }

    @Override
    public void clickSquare(Square square) {
        if (moveMode) {
            if (firstChosen == null) {
                if (square.getValue() < 7) {
                    firstChosen = square;
                    firstChosen.markChosen();
                }
            } else {
                byte[] content = new byte[bytesInPacket];
                content[0] = (byte) firstChosen.getI();
                content[1] = (byte) firstChosen.getJ();
                content[2] = (byte) square.getI();
                content[3] = (byte) square.getJ();
                try {
                    moveMode = false;
                    sendOut(content);
                } catch (IOException e) {
                    System.err.println("IOException when sending move");
                }
                firstChosen.unmarkChosen();
                firstChosen = null;
            }
        }
    }

    @Override
    public void confirmNumPlayers(int numPlayers) {
        byte[] content = new byte[bytesInPacket];
        content[0] = (byte)numPlayers;
        try {
            sendOut(content);
        } catch (IOException e) {
            System.err.println("IOException when sending number of players");
        }
    }

    @Override
    public void confirmGameMode(int gameMode) {
        byte[] content = new byte[bytesInPacket];
        content[0] = (byte)gameMode;
        try {
            sendOut(content);
        } catch (IOException e) {
            System.err.println("IOException when sending game mode");
        }
    }
}
