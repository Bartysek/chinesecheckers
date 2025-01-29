package org.example;

import java.io.IOException;
import java.io.OutputStream;

public interface BoardControl {

    void requestServerMode(int bytesInPackage, OutputStream out);
    void requestSettings(int bytesInPackage, OutputStream out);
    void requestMove(int bytesInPackage, OutputStream out);

    void sendOut(byte[] content) throws IOException;

    void clickSquare(Square square);
    void confirmServerMode(int serverMode, int gameID);
    void confirmSettings(int gameMode, int numOfPlayers, int numOfBots);
}
