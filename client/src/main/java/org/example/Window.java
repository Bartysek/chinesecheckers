package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JFrame {
    final BoardPanel boardPanel = new BoardPanel();
    final SettingsPanel settingsPanel = new SettingsPanel();

    Window() {
        super("Chinese Checkers");
        setSize(700, 900);
        setLayout(null);

        add(boardPanel);
        boardPanel.setBounds(20,20,500,700);
        boardPanel.setVisible(false);

        add(settingsPanel);
        settingsPanel.setBounds(20, 20, 300, 500);
        settingsPanel.setVisible(true);

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }


}
