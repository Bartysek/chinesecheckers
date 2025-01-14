package org.example;

import java.util.Iterator;

public interface RulesInterface {
    int[] getNextRemovedPiece();

    int[] getNextAddedPiece();

    void setBoard(Board newBoard, int numPlayers);

    int handleMove(int y1, int x1, int y2, int x2, int playerNumber); //-1 -> ruch nieudany 0 -> ruch udany, tura trwa 1 -> ruch udany, tura zakończona

    boolean checkEndCon(int player);

    int getWinner();
}
