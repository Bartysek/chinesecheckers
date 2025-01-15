package org.example;

import java.util.NoSuchElementException;

/**
 * rules for the capture variant
 */
public class CaptureRules extends AbstractRules {

    protected int[] capturedPieces;

    /**
     * set up the board
     * @param newBoard board for this game
     * @param numPlayers players to split pieces between
     */
    @Override
    public void setBoard(Board newBoard, int numPlayers) {
        this.board = newBoard;
        this.capturedPieces = new int[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            capturedPieces[i] = 0;
        }
    }

    /**
     * get the board attached to this game
     * @return the board
     */
    @Override
    public Board getBoard() {
        return this.board;
    }

    /**
     *
     * @param y1 coords of starting position
     * @param x1 coords of starting position
     * @param y2 coords of ending position
     * @param x2 coords of ending position
     * @param playerNumber the player which issued this move
     * @return move status
     */
    @Override
    public int handleMove(int y1, int x1, int y2, int x2, int playerNumber) {
        int status = checkMove(y1, x1, y2, x2);
        if (status == -1) { return status; }
        else if (status == 1) {
            isFirstMoveInTurn = true;
        }
        else if (status == 0) {
            isFirstMoveInTurn = false;
            currentPiece[0] = x2;
            currentPiece[1] = y2;
            board.remove((x1 + x2)/2, (y1 + y2)/2);
            removedPieces.add((x1 + x2)/2);
            removedPieces.add((y1 + y2)/2);
            capturedPieces[playerNumber]++;
        }
        int piece = board.getState()[y1][x1];
        board.remove(x1, y1);
        board.add(x2, y2, piece);
        removedPieces.add(x1);
        removedPieces.add(y1);
        addedPieces.add(x2);
        addedPieces.add(y2);
        addedPieces.add(piece);

        return status;
    }

    /**
     * checks if the move is valid
     * @param y1 coords of starting position
     * @param x1 coords of starting position
     * @param y2 coords of ending position
     * @param x2 coords of ending position
     * @return move status
     */
    private int checkMove(int y1, int x1, int y2, int x2) {
        int sum_distance = Math.abs(x1 - x2) + Math.abs(y1 - y2);

        if (y1 == y2 && x1 == x2) {
            return 1;
        }
        if (board.getState()[y1][x1] == 7) {
            return -1;
        }
        else if ((sum_distance == 2 && (x1 == x2 || y1 == y2)) || (sum_distance == 4 && x1 - x2 == -(y1 - y2))) {
            if (isFirstMoveInTurn || (currentPiece[0] == x1 && currentPiece[1] == y1)) {
                int inBetween = board.getState()[(y1 + y2) / 2][(x1 + x2) / 2];
                if (inBetween < 7 && inBetween > 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }
        //else
        return -1;
    }

    /**
     * checks if a tile is a piece
     * @param x
     * @param y
     * @return
     */
    private boolean isPiece(int x, int y) {
        return board.getState()[y][x] > 0 && board.getState()[y][x] < 7;
    }

    /**
     * checks if a tile is empty
     * @param x
     * @param y
     * @return
     */
    private boolean isEmptySpace(int x, int y) {
        return board.getState()[y][x] == 7;
    }

    /**
     * checks if the board is in a state that ends the game and determines the winner
     * @param player player that just moved
     * @return if the game has ended
     */
    @Override
    public boolean checkEndCon(int player) {
        for(int i = 0; i < 4 * board.getHexagonSide() - 3; i++) {
            for(int j = 0; j < 4 * board.getHexagonSide() - 3; j++) {
                if (isPiece(i, j)) {
                    if (i + 2 < board.getState().length && isPiece(i+1, j) && isEmptySpace(i + 2, j)) {
                        return false;
                    }
                    else if (i - 2 > 0 && isPiece(i - 1, j) && isEmptySpace(i - 2, j)) {
                        return false;
                    }
                    else if (j - 2 > 0 && isPiece(i, j - 1) && isEmptySpace(i, j - 2)) {
                        return false;
                    }
                    else if (j + 2 < board.getState().length && isPiece(i, j + 1) && isEmptySpace(i, j + 2)) {
                        return false;
                    }
                    else if (i - 2 > 0 && j + 2 < board.getState().length &&
                            isPiece(i - 1, j + 1) && isEmptySpace(i - 2, j + 2)) {
                        return false;
                    }
                    else if (j - 2 > 0 && i + 2 < board.getState().length &&
                            isPiece(i + 1, j - 1) && isEmptySpace(i + 2, j - 2)) {
                        return false;
                    }
                }
            }
        }
        winner = 0;
        int winnerCaptures = capturedPieces[0];
        for(int i = 1; i < capturedPieces.length; i++) {
            if(capturedPieces[i] > winnerCaptures) {
                winner = i;
                winnerCaptures = capturedPieces[i];
            }
        }
        return true;
    }

    /**
     * defined only if checkEndCon() is true
     * @return the winner
     */
    @Override
    public int getWinner() {
        return winner;
    }
}
