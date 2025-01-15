package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class BoardPanel extends JPanel {
    private Board board;
    private JLabel turnLabel = new JLabel("Wait for your turn...");
    private JButton endTurnButton = new JButton("End Turn");


    /**
     * Constructing the panel that would contain all elements of the board,
     * as well as The turn indicator and an "End turn" button.
     */
    public BoardPanel() {
        super(null);
        //turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnLabel.setBounds(20,600, 300, 30);
        turnLabel.setVisible(true);
        add(turnLabel);

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    board.bc.sendOut(new byte[]{0, 0, 0, 0});
                    board.bv.notYourTurn();
                } catch (IOException ex) {
                    System.err.println("IOException when sending move end signal");
                }
            }
        });

        endTurnButton.setBounds(20,640, 200, 40);
        endTurnButton.setVisible(false);
        add(endTurnButton);
    }

    /**
     * Sets the Board object which would use this Panel.
     * @param board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Changes the indicator to tell the user that it's their turn and makes the "End turn" button visible.
     */
    public void setYourTurn() {
        turnLabel.setText("It's Your turn!");
        endTurnButton.setVisible(true);
    }

    /**
     * Changes the indicator to tell the user that it's not their turn and makes the "End turn" button invisible.
     */
    public void setNotYourTurn() {
        turnLabel.setText("Wait for your turn...");
        endTurnButton.setVisible(false);
    }
}
