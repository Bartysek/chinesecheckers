package org.example;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class CaptureRules implements RulesInterface{

    protected Board board;
    protected boolean isFirstMoveInTurn = true;
    protected int[] currentPiece = new int[2];
    protected int[] capturedPieces;
    protected int winner;

    protected ArrayList<Integer> removedPieces = new ArrayList<>(); //x, y, x, y, x, y, ...
    protected ArrayList<Integer> addedPieces = new ArrayList<>(); //x, y, piece, x, y, piece, ...

    @Override
    public int[] getNextRemovedPiece() {
        try {
            int[] piece = new int[2];
            piece[0] = removedPieces.removeFirst();
            piece[1] = removedPieces.removeFirst();
            return piece;
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public int[] getNextAddedPiece() {
        try {
            int[] piece = new int[3];
            piece[0] = addedPieces.removeFirst();
            piece[1] = addedPieces.removeFirst();
            piece[2] = addedPieces.removeFirst();
            return piece;
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public void setBoard(Board newBoard, int numPlayers) {
        this.board = newBoard;
        this.capturedPieces = new int[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            capturedPieces[i] = 0;
        }
    }

    @Override
    public int handleMove(int y1, int x1, int y2, int x2, int playerNumber) {
        int status = checkMove(y1, x1, y2, x2, playerNumber);
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

    private int checkMove(int y1, int x1, int y2, int x2, int playerNumber) {
        int sum_distance = Math.abs(x1 - x2) + Math.abs(y1 - y2);

        if (y1 == y2 && x1 == x2) {
            return 1;
        }
        else if ((sum_distance == 2 && (x1 == x2 || y1 == y2)) || (sum_distance == 4 && x1 - x2 == -(y1 - y2))) {
            if (isFirstMoveInTurn || (currentPiece[0] == x1 && currentPiece[1] == y1)) {
                int inBetween = board.getState()[(x1 + x2) / 2][(y1 + y2) / 2];
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

    private boolean isPiece(int x, int y) {
        return board.getState()[y][x] > 0 && board.getState()[y][x] < 7;
    }

    private boolean isEmptySpace(int x, int y) {
        return board.getState()[y][x] == 7;
    }

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

    @Override
    public int getWinner() {
        return winner;
    }
}