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

    /**
     * Prepares the application to get the move from the user
     * @param bytesInPacket bytes that should be in the packet sent to the server with the information about the move
     * @param out the output stream to which the information should be sent
     */
    @Override
    public void requestMove(int bytesInPacket, OutputStream out) {
        outputStream = out;
        this.bytesInPacket = bytesInPacket;
        moveMode = true;
    }

    /**
     * Prepares the application to get the number of players from the user
     * @param bytesInPacket bytes that should be in the packet sent to the server with the information about the number of players
     * @param out the output stream to which the information should be sent
     */
    @Override
    public void requestServerMode(int bytesInPacket, OutputStream out) {
        outputStream = out;
        this.bytesInPacket = bytesInPacket;
        settingsPanel.setVisible(true);
        settingsPanel.setServerMode();
    }

    /**
     * Prepares the application to get the game mode from the user
     * @param bytesInPacket bytes that should be in the packet sent to the server with the information about the game mode
     * @param out the output stream to which the information should be sent
     */
    @Override
    public void requestSettings(int bytesInPacket, OutputStream out) {
        outputStream = out;
        this.bytesInPacket = bytesInPacket;
        settingsPanel.setVisible(true);
        settingsPanel.setSettings();
    }

    /**
     * Sends a byte table to the server
     * @param content
     * @throws IOException
     */
    @Override
    public void sendOut(byte[] content) throws IOException {
        outputStream.write(content);
    }

    /**
     * If the board control is in move mode, this function sets the clicked square as the first chosen square,
     * or if the first square is already chosen, it selects the second square and sends the move to the server
     * @param square
     */
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

    /**
     * Sends to the server the number of players chosen
     * @param serverMode an integer indicating the number of players
     */
    @Override
    public void confirmServerMode(int serverMode, int gameID) {
        byte[] content;
        if (serverMode == 0) {
            content = new byte[1];
            content[0] = (byte)serverMode;
        }
        else {
            content = new byte[bytesInPacket];
            content[0] = (byte)serverMode;
            content[1] = (byte)((gameID)/256/256/256);
            content[2] = (byte)((gameID/256/256)%256);
            content[3] = (byte)((gameID/256)%256);
            content[4] = (byte)((gameID)%256);
        }
        try {
            sendOut(content);
        } catch (IOException e) {
            System.err.println("IOException when sending number of players");
        }
    }

    /**
     * Sends to the server the game mode chosen
     * @param gameMode game mode as an integer
     */
    @Override
    public void confirmSettings(int gameMode, int numOfPlayers, int numOfBots) {
        byte[] content = new byte[bytesInPacket];
        content[0] = (byte)numOfPlayers;
        content[1] = (byte)gameMode;
        content[2] = (byte)numOfBots;
        try {
            sendOut(content);
        } catch (IOException e) {
            System.err.println("IOException when sending game mode");
        }
    }
}
