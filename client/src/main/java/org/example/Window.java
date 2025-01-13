package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JFrame {
    final JPanel boardPanel = new JPanel(null);
    //final BoardVisualizer bv = new AwtBoardVisualizer(boardPanel);

    Window() {
        super("Chineese Checkers");
        setSize(700, 700);


        //boardPanel.setBackground(new Color(0,0,0,0));
        //boardPanel.setVisible(true);
        add(boardPanel);
        boardPanel.setBounds(20,20,500,500);
        //bv.showBoard(new Board());

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
