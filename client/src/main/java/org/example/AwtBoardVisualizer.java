package org.example;

import static java.lang.Math.min;

public class AwtBoardVisualizer implements BoardVisualizer {

    private final BoardPanel boardPanel;
    private boolean initialized = false;
    private int size;
    private Square[][] squares;

    private final double squareSizeRatio = 0.8;

    AwtBoardVisualizer(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    /**
     * Initializes the board if necessary, otherwise only updates the squares to match the values in the board
     * @param board
     */
    @Override
    public void showBoard(Board board) {
        if (!initialized) {
            this.boardPanel.setBoard(board);
            initBoard(board);
            initialized = true;
        } else {
            int[][] s = board.getState();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (squares[i][j] != null) squares[i][j].setValue(s[i][j]);
                }
            }
        }
        boardPanel.setVisible(true);
    }

    /**
     * Initializes the visual representation of the board
     * @param board
     */
    private void initBoard(Board board) {
        int[][] s = board.getState();
        size = s.length;
        int contSize = min(boardPanel.getWidth(), boardPanel.getHeight());
        int squareSize = ((contSize/size));

        squares = new Square[size][size];

        int curx;
        int cury = 0;

        for (int i = 0; i < size; i++) {
            curx = (i-size/2)*squareSize/2;
            for (int j = 0; j < size; j++) {
                curx += squareSize;
                if (s[i][j] > 0) {
                    squares[i][j] = new Square(i,j,s[i][j], board);
                    boardPanel.add(squares[i][j]);
                    squares[i][j].init(curx, cury, (int)(squareSize*squareSizeRatio), (int)(squareSize*squareSizeRatio));
                }
            }
            cury += squareSize;
        }
    }

    /**
     * Informs the board panel that it's the player's turn
     */
    @Override
    public void yourTurn() {
        boardPanel.setYourTurn();
    }

    /**
     * Informs the board panel that it is no longer the player's turn
     */
    @Override
    public void notYourTurn() {
        boardPanel.setNotYourTurn();
    }
}
