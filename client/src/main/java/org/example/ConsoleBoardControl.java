package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class ConsoleBoardControl implements BoardControl {
    OutputStream outputStream;

    @Override
    public void setOut(int BYTES_IN_MOVE_PACKET, OutputStream out) {
        this.outputStream = out;
        byte[] content = new byte[BYTES_IN_MOVE_PACKET];

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
            sendMove(content);
        } catch (IOException e) {
            System.err.println("IOException when sending move");
        }
    }

    @Override
    public void sendMove(byte[] content) throws IOException {
        outputStream.write(content);
    }

    @Override
    public void clickSquare(Square square) {

    }


}
