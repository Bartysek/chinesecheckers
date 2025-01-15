package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {
    Board board;
    final String[] GameModes = new String[]{"Standard", "Order Out Of Chaos", "Capture"};
    final JPanel numPlayersPanel = new JPanel(null);
    final JPanel gameModesPanel = new JPanel(null);

    //final JTextField numPlayersField = new JTextField("2");

    /**
     * Creating the panel that contains elements needed to choose the settings of the game
     */
    SettingsPanel() {
        setLayout(null);

        JLabel numPlayersLabel = new JLabel("Number of players:");
        numPlayersLabel.setBounds(20, 20, 200, 20);
        numPlayersPanel.add(numPlayersLabel);

        JTextField numPlayersField = new JTextField("2");
        numPlayersField.setBounds(20,40,100,20);
        numPlayersPanel.add(numPlayersField);

        JButton numPlayersConfirmButton = new JButton("Confirm number of players");
        numPlayersConfirmButton.setBounds(20, 70, 100, 40);
        numPlayersPanel.add(numPlayersConfirmButton);

        numPlayersConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.bc.confirmNumPlayers(Integer.parseInt(numPlayersField.getText()));
            }
        });

        numPlayersPanel.setBounds(20,20,300,150);
        numPlayersPanel.setVisible(false);
        add(numPlayersPanel);


        JLabel gameModeLabel = new JLabel("Game mode:");
        gameModeLabel.setBounds(20, 20, 200, 20);
        gameModesPanel.add(gameModeLabel);

        JComboBox<String> GameModeList = new JComboBox<>(GameModes);
        GameModeList.setBounds(20,50,200,40);
        gameModesPanel.add(GameModeList);

        JButton gameModeConfirmButton = new JButton("Confirm game mode");
        gameModeConfirmButton.setBounds(20, 100, 100, 40);
        gameModesPanel.add(gameModeConfirmButton);

        gameModeConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedGameMode = (String) GameModeList.getSelectedItem();
                int modeInt = 0;
                for (int i = 0; i < GameModes.length; i++) {
                    if (selectedGameMode.equals(GameModes[i])) {
                        modeInt = i;
                    }
                }
                gameModesPanel.setVisible(false);
                board.bc.confirmGameMode(modeInt);
            }
        });

        gameModesPanel.setBounds(20,20,300,150);
        gameModesPanel.setVisible(false);
        add(gameModesPanel);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Sets the visibility of the objects so that the number of players can be entered
     */
    public void setNumPlayers() {
        setVisible(true);
        numPlayersPanel.setVisible(true);
        gameModesPanel.setVisible(false);
    }

    /**
     * Sets the visibility of the objects so that the game mode can be entered
     */
    public void setGameMode() {
        gameModesPanel.setVisible(true);
        numPlayersPanel.setVisible(false);
    }
}
