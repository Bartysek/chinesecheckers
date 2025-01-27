package org.example;

import java.io.IOException;
import java.util.ArrayList;

public class BotPlayer implements PlayerInterface {
    private Board board;
    //private AbstractRules rules;
    private Engine engine;
    private int playerNum;
    private int numOfPlayers;

    private boolean turnPlanned = false;

    ArrayList<int[]> plan;

    BotPlayer(Engine engine, int playerNum) {
        this.board = new Board();
        this.engine = engine;
        this.playerNum = playerNum;
        this.numOfPlayers = 2; // TODO
    }

    @Override
    public void closeSocket() {

    }

    @Override
    public void sendBoardState(int size, int[][] state) {
        this.board.setState(state);
    }

    @Override
    public void removePiece(int[] pieceInfo) {
        board.remove(pieceInfo[0],pieceInfo[1]);
    }

    @Override
    public void addPiece(int[] pieceInfo) {
        board.add(pieceInfo[0], pieceInfo[1], pieceInfo[2]);
    }

    @Override
    public void sendMessage(String content) {

    }

    @Override
    public byte[] listen() throws IOException {
        if (plan.size() > 1) {
            byte[] move = new byte[4];
            move[0] = (byte) plan.get(0)[1];
            move[1] = (byte) plan.get(0)[0];
            move[2] = (byte) plan.get(1)[1];
            move[3] = (byte) plan.get(1)[0];
            plan.removeFirst();
            //System.out.println("From bot: sending move " + move);
            //System.out.println("From bot: plan now is: " + plan);
            return move;
        } else {
            //System.out.println("From bot: sending end of move");
            turnPlanned = false;
            return new byte[]{0,0,0,0};
        }
    }

    @Override
    public int queryNumPlayers() {
        return 0;
    }

    @Override
    public AbstractRules queryGameRules() {
        return null;
    }

    @Override
    public void sendTheirTurn() {
        if (!turnPlanned) {
            plan  = engine.planTurn(board, numOfPlayers, playerNum);
            String planStr = "";
            for (int[] move : plan) {
                planStr += move[0] + "," + move[1] + " -> ";
            }
            System.out.println("From bot: my turn plan: " + planStr);
            turnPlanned = true;
        }
    }

    @Override
    public void sendEndOfMove(int playerNum) {

    }


}
