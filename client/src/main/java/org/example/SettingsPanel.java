package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {
    Board board;
    final String[] serverModes = new String[]{"Play a game", "Load game", "Playback"};
    final String[] GameModes = new String[]{"Standard", "Order Out Of Chaos", "Capture"};
    final JPanel serverModePanel = new JPanel(null);
    final JPanel settingsPanel = new JPanel(null);
    final JPanel gameIDPanel = new JPanel(null);

    private int modeInt;

    //final JTextField numPlayersField = new JTextField("2");

    /**
     * Creating the panel that contains elements needed to choose the settings of the game
     */
    SettingsPanel() {
        setLayout(null);

        JLabel serverModeLabel = new JLabel("Server Mode: ");
        serverModeLabel.setBounds(20, 20, 200, 20);
        settingsPanel.add(serverModeLabel);

        JComboBox<String> serverModeList = new JComboBox<>(serverModes);
        serverModeList.setBounds(20,110,200,40);
        settingsPanel.add(serverModeList);


        JButton serverModeConfirmButton = new JButton("Confirm server mode");
        serverModeConfirmButton.setBounds(20, 70, 100, 40);
        serverModePanel.add(serverModeConfirmButton);


        serverModeConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedServerMode = (String) serverModeList.getSelectedItem();
                modeInt = 0;
                for (int i = 0; i < GameModes.length; i++) {
                    if (selectedServerMode.equals(serverModes[i])) {
                        modeInt = i;
                    }
                }
                if (modeInt == 0) board.bc.confirmServerMode(modeInt, 0);
                else setGameID();
            }
        });

        serverModePanel.setBounds(20,20,300,150);
        serverModePanel.setVisible(false);
        add(serverModePanel);

        // GameID Panel
        JLabel gameIDLabel = new JLabel("Number of players:");
        gameIDLabel.setBounds(20, 20, 200, 20);
        gameIDPanel.add(gameIDLabel);

        JTextField gameIDField = new JTextField("2");
        gameIDField.setBounds(20,40,100,20);
        gameIDPanel.add(gameIDField);

        JButton gameIDButton = new JButton("Confirm game mode");
        gameIDButton.setBounds(20, 160, 100, 40);
        settingsPanel.add(gameIDButton);

        gameIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int gameID = Integer.parseInt(gameIDField.getText());
                gameIDPanel.setVisible(false);
                board.bc.confirmSettings(modeInt, gameID);
            }
        });


        // Game settings Panel
        JLabel numPlayersLabel = new JLabel("Number of players:");
        numPlayersLabel.setBounds(20, 20, 200, 20);
        settingsPanel.add(numPlayersLabel);

        JTextField numPlayersField = new JTextField("2");
        numPlayersField.setBounds(20,40,100,20);
        settingsPanel.add(numPlayersField);

        JLabel gameModeLabel = new JLabel("Game mode:");
        gameModeLabel.setBounds(20, 80, 200, 20);
        settingsPanel.add(gameModeLabel);

        JComboBox<String> GameModeList = new JComboBox<>(GameModes);
        GameModeList.setBounds(20,110,200,40);
        settingsPanel.add(GameModeList);

        JButton settingsConfirmButton = new JButton("Confirm game mode");
        settingsConfirmButton.setBounds(20, 160, 100, 40);
        settingsPanel.add(settingsConfirmButton);

        settingsConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numOfPlayers = Integer.parseInt(numPlayersField.getText());
                String selectedGameMode = (String) GameModeList.getSelectedItem();
                modeInt = 0;
                for (int i = 0; i < GameModes.length; i++) {
                    if (selectedGameMode.equals(GameModes[i])) {
                        modeInt = i;
                    }
                }
                settingsPanel.setVisible(false);
                board.bc.confirmSettings(modeInt, numOfPlayers);
            }
        });

        settingsPanel.setBounds(20,20,300,150);
        settingsPanel.setVisible(false);
        add(settingsPanel);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Sets the visibility of the objects so that the number of players can be entered
     */
    public void setServerMode() {
        setVisible(true);
        serverModePanel.setVisible(true);
        settingsPanel.setVisible(false);
    }

    public void setGameID() {
        setVisible(true);
        serverModePanel.setVisible(false);
        settingsPanel.setVisible(false);
        gameIDPanel.setVisible(true);
    }



    /**
     * Sets the visibility of the objects so that the game mode can be entered
     */
    public void setSettings() {
        settingsPanel.setVisible(true);
        serverModePanel.setVisible(false);
    }
}
