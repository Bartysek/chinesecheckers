package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

import static java.lang.Math.floor;
import static java.lang.Math.min;

public class AwtBoardVisualizer implements BoardVisualizer {

    private final Container container;
    private boolean initialized = false;
    private int size;
    private Square[][] squares;

    private final double squareSizeRatio = 0.8;

    AwtBoardVisualizer(Container container) {
        this.container = container;
    }

    @Override
    public void showBoard(Board board) {
        if (!initialized) {
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
    }

    private void initBoard(Board board) {
        int[][] s = board.getState();
        size = s.length;
        int contSize = min(container.getWidth(), container.getHeight());
        int squareSize = ((contSize/size));

        squares = new Square[size][size];

        int curx;
        int cury = 0;

        for (int i = 0; i < size; i++) {
            curx = (i-size/2)*squareSize/2;
            for (int j = 0; j < size; j++) {
                curx += squareSize;
                if (s[i][j] > 0) {
                    squares[i][j] = new Square(i,j,s[i][j]);
                    container.add(squares[i][j]);
                    squares[i][j].init(curx, cury, (int)(squareSize*squareSizeRatio), (int)(squareSize*squareSizeRatio));
                }
            }
            cury += squareSize;
        }
    }
}
