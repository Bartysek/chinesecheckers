package org.example;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * rules of the game
 */
public abstract class AbstractRules {


    protected int winner;
    protected Board board;
    protected boolean isFirstMoveInTurn = true;
    protected int[] currentPiece = new int[2];

    protected ArrayList<Integer> removedPieces = new ArrayList<>(); //x, y, x, y, x, y, ...
    protected ArrayList<Integer> addedPieces = new ArrayList<>(); //x, y, piece, x, y, piece, ...


    /**
     * fetch and delete the information on a removed piece
     * @return x and y coords of the piece
     */
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

    /**
     * fetch and delete the information on an added piece
     * @return x and y coords of the piece
     */
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

    /**
     * set up the board and ownership of the pieces (if eligible)
     * @param newBoard board for this game
     * @param numPlayers players to split pieces between
     */
    abstract void setBoard(Board newBoard, int numPlayers);

    /**
     * get the board attached to this game
     * @return the board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * handle a move a player sent
     * @param y1 coords of starting position
     * @param x1 coords of starting position
     * @param y2 coords of ending position
     * @param x2 coords of ending position
     * @param playerNumber the player which issued this move
     * @return move status. -1 - invalid move, 0 - valid move that doesn't end the turn, 1 - valid move that ends the turn
     */
    abstract int handleMove(int y1, int x1, int y2, int x2, int playerNumber);

    /**
     * checks if the board is in a state that ends the game and determines the winner
     * @param player player that just moved
     * @return if the game has ended
     */
    abstract boolean checkEndCon(int player);

    /**
     * defined only if checkEndCon() returns true
     * @return the winner
     */
    public int getWinner() {
        return winner;
    }
}
