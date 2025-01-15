package org.example;

/**
 * an interface for rules of the game
 */
public interface RulesInterface {
    /**
     * @return one of the pieces removed since last checked
     */
    int[] getNextRemovedPiece();

    /**
     * @return one of the pieces added since last checked
     */
    int[] getNextAddedPiece();

    /**
     * set up the board and ownership of the pieces (if eligible)
     * @param newBoard board for this game
     * @param numPlayers players to split pieces between
     */
    void setBoard(Board newBoard, int numPlayers);

    /**
     * handle a move a player sent
     * @param y1 coords of starting position
     * @param x1 coords of starting position
     * @param y2 coords of ending position
     * @param x2 coords of ending position
     * @param playerNumber the player which issued this move
     * @return move status. -1 - invalid move, 0 - valid move that doesn't end the turn, 1 - valid move that ends the turn
     */
    int handleMove(int y1, int x1, int y2, int x2, int playerNumber);

    /**
     * checks if the board is in a state that ends the game and determines the winner
     * @param player player that just moved
     * @return if the game has ended
     */
    boolean checkEndCon(int player);

    /**
     * checks the winning player. Defined if checkEndCon returned true
     * @return the winning player
     */
    int getWinner();
}
