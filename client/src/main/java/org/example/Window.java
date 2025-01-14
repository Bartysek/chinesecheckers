package org.example;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JFrame {
    final BoardPanel boardPanel = new BoardPanel();
    final JLabel turnLabel = new JLabel("Wait for your turn...");
    //final BoardVisualizer bv = new AwtBoardVisualizer(boardPanel);

    Window() {
        super("Chineese Checkers");
        setSize(700, 800);

        add(boardPanel);
        boardPanel.setBounds(20,20,500,700);
        //bv.showBoard(new Board());

        turnLabel.setBounds(720,50,100,50);
        turnLabel.setVisible(true);

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
