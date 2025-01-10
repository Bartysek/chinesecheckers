package org.example;



public class NaturalRules implements RulesInterface {
    private Board board;
    boolean isFirstMoveInTurn = true;

    @Override
    public void setBoard(Board newBoard) {
        this.board = newBoard;
    }

    @Override
    public int handleMove(int y1, int x1, int y2, int x2) {
        int status = checkMove(y1, x1, y2, x2);
        if (status == -1) { return status; }
        else if (status == 0) {
            isFirstMoveInTurn = true;
        }
        else if (status == 1) {
            isFirstMoveInTurn = false;
        }
        board.move(y1, x1, y2, x2);

        return status;
    }

    private int checkMove(int y1, int x1, int y2, int x2) {
        int sum_distance = Math.abs(x1 - x2) + Math.abs(y1 - y2);

        if (y1 == y2 && x1 == x2) { return 0; }

        else if ((sum_distance == 1) || (sum_distance == 2 && x1 - x2 == -(y1 - y2) )) {
            if (board.getState()[x2][y2] == 7) {
                return 0;
            }
            else return -1;
        }
        else if ((sum_distance == 2 && (x1 == x2 || y1 == y2)) || (sum_distance == 4 && x1 - x2 == -(y1 - y2))) {
            int inBetween = board.getState()[(x1 + x2) / 2][(y1 + y2) / 2];
            if (inBetween < 7 && inBetween > 0) {
                return 1;
            }
            else {
                return -1;
            }
        }
        //else
        return -1;
    }

    private boolean checkWinCon() {
        return false;
    }
}
