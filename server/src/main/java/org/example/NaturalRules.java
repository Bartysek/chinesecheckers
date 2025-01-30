package org.example;



/**
 * standard rules
 */
public class NaturalRules extends AbstractRules {

    protected int[] pieceOwnership = new int[6];

    public NaturalRules(){
        ruleNum = 0;
    }

    int[] getPieceOwnership(){
        return pieceOwnership;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    /**
     *
     * @param newBoard board for this game
     * @param numPlayers players to split pieces between
     */
    @Override
    public void setupBoard(Board newBoard, int numPlayers) {
        setBoard(newBoard);
        if (numPlayers != 4) {
            for (int i = 0; i < 6; i++) {
                pieceOwnership[i] = i % numPlayers;
            }
        }
        else {
            int[][] state = newBoard.getState();
            int size = newBoard.getHexagonSide();
            for (int i = 0; i < 4 * size - 3; i++) {
                for (int j = 0; j < 4 * size - 3; j++) {
                    if(state[i][j] == 3) state[i][j] = 7;
                    if(state[i][j] == 6) state[i][j] = 7;
                }
            }
            pieceOwnership[0] = 0;
            pieceOwnership[1] = 1;
            pieceOwnership[2] = -1;
            pieceOwnership[3] = 2;
            pieceOwnership[4] = 3;
            pieceOwnership[5] = -1;
        }
    }

    /**
     *
     * @param y1 coords of starting position
     * @param x1 coords of starting position
     * @param y2 coords of ending position
     * @param x2 coords of ending position
     * @param playerNumber the player which issued this move
     * @return
     */
    @Override
    public int handleMove(int y1, int x1, int y2, int x2, int playerNumber) {
        int status = checkMove(y1, x1, y2, x2, playerNumber, isFirstMoveInTurn);
        if (status == -1) { return status; }
        else if (status == 1) {
            isFirstMoveInTurn = true;
        }
        else if (status == 0) {
            isFirstMoveInTurn = false;
            currentPiece[0] = x2;
            currentPiece[1] = y2;
        }
        doMove(y1, x1, y2, x2);


        return status;
    }

    /**
     * check if the move is valid
     * @param y1 coords of starting position
     * @param x1 coords of starting position
     * @param y2 coords of ending position
     * @param x2 coords of ending position
     * @param playerNumber player that issued the move
     * @return move status
     */
    public int checkMove(int y1, int x1, int y2, int x2, int playerNumber, boolean isFirst) {
        int sum_distance = Math.abs(x1 - x2) + Math.abs(y1 - y2);


        if (y1 == y2 && x1 == x2) {
            return 1;
        }
        if (board.getState()[y1][x1] == 0 || board.getState()[y2][x2] != 7 ){
            return -1;
        }
        if (board.getState()[y1][x1] == 7) {
            return -1;
        }
        if (pieceOwnership[board.getState()[y1][x1] - 1] != playerNumber) {
            return -1;
        }

        if ((sum_distance == 1) || (sum_distance == 2 && x1 - x2 == -(y1 - y2) )) {
            if (board.getState()[y2][x2] == 7 && isFirst) {
                return 1;
            }
            else return -1;
        }
        else if ((sum_distance == 2 && (x1 == x2 || y1 == y2)) || (sum_distance == 4 && x1 - x2 == -(y1 - y2))) {
            if (isFirst || (currentPiece[0] == x1 && currentPiece[1] == y1)) {
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
     *
     * @param PlayerNumber player that just moved
     * @return if the game has ended
     */
    public boolean checkEndCon(int PlayerNumber) {
        int size = board.getHexagonSide();
        int[] lookupChange = {1, 2, 1, -1, -2, -1}; //getting to the peak of the triangle to go along the left and right edge of it
        int[] lookupLeftX = {0, -1, -1, 0, 1, 1}; //RightX[i] = LeftX[i+1]; LeftY[i] = -LeftX[i+1]; RightY[i] = -LeftX[i+2]
        for(int i = 0, x = 3 * size - 3, y = 0;
            i < 6;
            x += (size - 1) * lookupChange[(i + 2) % 6], y += (size - 1) * lookupChange[i % 6], i++) {
            if (pieceOwnership[i] == PlayerNumber) {
                int topX = x;
                int topY = y;
                for (int j = 0; j < size - 1; j++) {
                    int currentX = topX;
                    int currentY = topY;
                    for (int k = 0; k < size - j - 1; k++) {
                        //System.out.println(currentX + " " + currentY + " = " + board.getState()[currentY][currentX] + " ? " + (i+1));
                        //what the actual fuck is happening
                        if (board.getState()[currentY][currentX] != i + 1) {
                            return false;
                        }
                        //go 1 left on the triangle
                        currentX += lookupLeftX[i];
                        currentY -= lookupLeftX[(i + 1) % 6];
                    }
                    //go 1 right on the triangle
                    topX += lookupLeftX[(i + 1) % 6];
                    topY -= lookupLeftX[(i + 2) % 6];
                }
            }
        }
        winner = PlayerNumber;
        return true;
    }

}
