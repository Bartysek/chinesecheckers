package org.example;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Square extends JButton {
    static ImageIcon[] icons;
    static {
        icons = new ImageIcon[13];
        for (int i=0; i<7; i++) {
            icons[i] = new ImageIcon(Square.class.getClassLoader().getResource("img/sq"+(i+1)+".png"));
        }
        for (int i=7; i<13; i++) {
            icons[i] = new ImageIcon(Square.class.getClassLoader().getResource("img/sq"+(i-6)+"c.png"));
        }
    }

    private int i;
    private int j;
    private int value;
    boolean chosen = false;
    private Board board;

    int getI() { return this.i; }
    int getJ() { return this.j; }
    int getValue() { return this.value; }

    Square(int i, int j, int value, Board board) {
        this.i = i;
        this.j = j;
        this.value = value;
        this.board = board;

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.bc.clickSquare((Square)e.getSource());
            }
        });
    }

    public void init(int x, int y, int width, int height) {
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        this.setBounds(x, y, width, height);

        setImg();


        setVisible(true);
    }

    private void setImg() {
        ImageIcon icon;
        if (chosen) {
            icon = icons[value+6];
        } else {
            icon = icons[value-1];
        }
        Image scaledImage = icon.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        setIcon(scaledIcon);
    }

    public void setValue(int value) {
        this.value = value;
        setImg();
    }

    public void markChosen() {
        chosen = true;
        setImg();
    }

    public void unmarkChosen() {
        chosen = false;
        setImg();
    }

}
