package org.example;

import java.io.IOException;
import java.io.OutputStream;

public interface BoardControl {

    void requestNumPlayers(int bytesInPackage, OutputStream out);
    void requestGameMode(int bytesInPackage, OutputStream out);
    void requestMove(int bytesInPackage, OutputStream out);

    void sendOut(byte[] content) throws IOException;

    void clickSquare(Square square);
    void confirmNumPlayers(int numPlayers);
    void confirmGameMode(int gameMode);
}
