package org.example;

/**
 * rules for the "Order out of chaos" variant
 */
public class OoocRules extends NaturalRules{ //order out of chaos

    private void scrambleBoard(Board b) {
        int[][] state = b.getState();
        int size = b.getHexagonSide();
        int maxPieces = (size)*(size-1)/2;
        int[] availablePieces = {maxPieces, maxPieces, maxPieces, maxPieces, maxPieces, maxPieces};
        for (int i = 0; i < 4 * size - 3; i++) {
            for (int j = 0; j < 4 * size - 3; j++) {
                if (i == 2 * size - 2 && j == 2 * size - 2) {
                    continue;
                }
                if(state[i][j] == 7) {
                    int newPiece;
                    do{
                        newPiece = (int)(Math.random()*6);
                    } while(availablePieces[newPiece] == 0);
                    availablePieces[newPiece]--;
                    state[i][j] = newPiece + 1;
                }
                else if (state[i][j] > 0) state[i][j] = 7;
            }
        }
        b.setState(state);
    }

    @Override
    public void setBoard(Board newBoard, int numPlayers) {
        scrambleBoard(newBoard);
        super.setBoard(newBoard, numPlayers);
    }
}
