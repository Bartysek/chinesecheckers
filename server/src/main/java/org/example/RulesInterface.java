package org.example;

interface RulesInterface {
    void setBoard(Board newBoard);

    int handleMove(int y1, int x1, int y2, int x2); //-1 -> ruch nieudany 0 -> ruch udany, tura zakończona 1 -> ruch udany, tura trwa 2 -> ruch udany, gracz zakończył rozgrywkę
}
