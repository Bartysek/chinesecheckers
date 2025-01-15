package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class BoardPanel extends JPanel {
    private Board board;
    private JLabel turnLabel = new JLabel("Wait for your turn...");
    private JButton endTurnButton = new JButton("End Turn");

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

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setYourTurn() {
        turnLabel.setText("It's Your turn!");
        endTurnButton.setVisible(true);
    }

    public void setNotYourTurn() {
        turnLabel.setText("Wait for your turn...");
        endTurnButton.setVisible(false);
    }
}
