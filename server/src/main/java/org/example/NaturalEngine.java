package org.example;

import java.util.ArrayList;
import java.util.Random;

public class NaturalEngine implements Engine {
    protected AbstractRules rules;
    private Board privBoard;
    protected int pleyerNum;

    ArrayList<Plan> candidatePlans = new ArrayList<>();

    @Override
    public int[] getPieceOwnership() {
        return ((NaturalRules)rules).getPieceOwnership();
    }

    @Override
    public void createRules() {
        rules = new NaturalRules();
    }

    @Override
    public ArrayList<int[]> planTurn(Board board, int numOfPlayers, int playerNum) {
        this.pleyerNum = playerNum;
        candidatePlans.clear();

        int size = board.getHexagonSide();
        int[][] state = board.getState();

        createRules();
        privBoard = new Board();
        privBoard.setState(state);
        rules.setupBoard(privBoard, numOfPlayers);

        for (int i = 0; i < 4 * size - 3; i++) {
            for (int j = 0; j < 4 * size - 3; j++) {
                if (state[i][j] > 0 && state[i][j] < 7 && playerNum == getPieceOwnership()[state[i][j]-1]) {
                    candidatePlans.add(new Plan(j, i));
                    findMove(candidatePlans.getLast(), playerNum);
                }
            }
        }

        int maxRating = 0;
        for (Plan plan : candidatePlans) {
            plan.rating = evaluateRating(plan);
            if (maxRating < plan.rating) maxRating = plan.rating;
            //System.out.println(plan);
        }

        System.out.println(maxRating);

        ArrayList<Plan> maxPlans = new ArrayList<>();
        for (Plan plan : candidatePlans) {
            if (plan.rating == maxRating) maxPlans.add(plan);
            System.out.println(plan);
        }

        return maxPlans.get(new Random().nextInt(maxPlans.size())).getAsArray();
    }

    void findMove(Plan plan, int playerNum) {

        int isPossible;
        int x2 = plan.getLastMove()[0];
        int y2 = plan.getLastMove()[1];

        int size = rules.getBoard().getHexagonSide();

        for (int k = y2 - 2; k <= y2 + 2; k++) {
            for (int l = x2 - 2; l <= x2 + 2; l++) {
                if ((k >=0) && (k < 4*size-3) && (l >= 0) && (l < 4*size-3)) {
                    boolean visited = false;
                    for (int[] move : plan.getAsArray()) {
                        if (k == move[1] && l == move[0]){
                            visited = true;
                            break;
                        }
                    }
                    if (visited) continue;
                    isPossible = rules.handleMove(y2, x2, k, l, playerNum);
                    if (isPossible == 1) {
                        candidatePlans.add(new Plan(plan));
                        candidatePlans.getLast().addMove(l, k);
                        //System.out.println("Added candidate plan: " + candidatePlans.getLast().toString());
                        rules.doMove(k, l, y2, x2);

                    } else if (isPossible == 0) {
                        candidatePlans.add(new Plan(plan));
                        candidatePlans.getLast().addMove(l, k);
                        //System.out.println("Added candidate plan: " + candidatePlans.getLast().toString());
                        //rules.doMove(y2, x2, k, l);
                        findMove(candidatePlans.getLast(), playerNum);
                        rules.doMove(k, l, y2, x2);
                        if (plan.getSize() == 1) rules.restartMove();
                    }

                }
            }
        }
    }

    @Override
    public int evaluateRating(Plan plan) {
        int rating = 0;
        int x1 = plan.getFirstMove()[0];
        int y1 = plan.getFirstMove()[1];
        int x2 = plan.getLastMove()[0];
        int y2 = plan.getLastMove()[1];
        rating = switch (privBoard.getState()[y1][x1]) {
            case 1 -> y1 - y2;
            case 4 -> y2 - y1;
            case 2 -> x2 - x1;
            case 5 -> x1 - x2;
            case 3 -> x2 - x1 + y2 - y1;
            case 6 -> x1 - x2 + y1 - y2;
            default -> rating;
        };

        return rating;
    }
}
