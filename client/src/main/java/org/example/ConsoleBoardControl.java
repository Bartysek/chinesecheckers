package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class ConsoleBoardControl implements BoardControl {
    OutputStream outputStream;

    @Override
    public void requestMove(int bytesInPacket, OutputStream out) {
        this.outputStream = out;
        byte[] content = new byte[bytesInPacket];

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter start position row number:");
        content[0] = (byte) scanner.nextInt();
        System.out.println("Enter start position diagonal number:");
        content[1] = (byte) scanner.nextInt();
        System.out.println("Enter end position row number:");
        content[2] = (byte) scanner.nextInt();
        System.out.println("Enter end position diagonal number:");
        content[3] = (byte) scanner.nextInt();

        try {
            sendOut(content);
        } catch (IOException e) {
            System.err.println("IOException when sending move");
        }
    }
/*
    @Override
    public void requestNumPlayers(int bytesInPacket, OutputStream out) {
        this.outputStream = out;
        byte[] content = new byte[bytesInPacket];
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter number of players:");
        content[0] = (byte)scanner.nextInt();

        try {
            sendOut(content);
        } catch (IOException e) {
            System.err.println("IOException when sending move");
        }
    } */

    @Override
    public void requestServerMode(int bytesInPackage, OutputStream out) {

    }

    @Override
    public void requestSettings(int bytesInPacket, OutputStream out) {
        this.outputStream = out;
        byte[] content = new byte[bytesInPacket];
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter game mode:");
        content[0] = (byte)scanner.nextInt();

        try {
            sendOut(content);
        } catch (IOException e) {
            System.err.println("IOException when sending move");
        }
    }


    @Override
    public void sendOut(byte[] content) throws IOException {
        outputStream.write(content);
    }

    @Override
    public void clickSquare(Square square) {

    }

    @Override
    public void confirmServerMode(int serverMode, int gameID) {

    }

    /* @Override
     public void confirmNumPlayers(int numPlayers) {} */
    @Override
    public void confirmSettings(int gameMode, int numOfPlayers, int numOfBots) {}

}
