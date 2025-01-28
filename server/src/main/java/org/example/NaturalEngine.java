package org.example;

import java.util.ArrayList;
import java.util.Random;

public class NaturalEngine implements Engine {
    private NaturalRules rules;
    private boolean isFirstMoveInTurn;
    private Board privBoard;

    ArrayList<Plan> candidatePlans = new ArrayList<>();

    private class Plan implements Cloneable {
        private ArrayList<int[]> moves;
        public int rating;

        Plan(int x, int y) {
            moves = new ArrayList<>();
            addMove(x, y);
        }

        Plan(Plan parent) {
            moves = new ArrayList<>();
            for (int[] move : parent.moves) {
                addMove(move[0], move[1]);
            }
        }

        public void evaluateRating() {
            int x1 = moves.getFirst()[0];
            int y1 = moves.getFirst()[1];
            int x2 = moves.getLast()[0];
            int y2 = moves.getLast()[1];
            switch (privBoard.getState()[y1][x1]) {
                case 1:
                    rating = y1 - y2; break;
                case 4:
                    rating = y2 - y1; break;
                case 2:
                    rating = x2 - x1; break;
                case 5:
                    rating = x1 - x2; break;
                case 3:
                    rating = x2 - x1 + y2 - y1; break;
                case 6:
                    rating = x1 - x2 + y1 - y2; break;
            }
        }

        void addMove(int x, int y) {
            moves.add(new int[] {x, y} );
        }

        int[] getLastMove() {
            return moves.getLast();
        }

        int getSize() {
            return moves.size();
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        ArrayList<int[]> getAsArray() {
            return moves;
        }

        @Override
        public String toString() {
            String string = "";
            for (int[] move : moves) {
                string += move[0] + ", " + move[1] + " -> ";
            }
            string += "rating " + rating;
            return string;
        }

    }

    @Override
    public ArrayList<int[]> planTurn(Board board, int numOfPlayers, int playerNum) {
        candidatePlans.clear();

        int size = board.getHexagonSide();
        int[][] state = board.getState();

        rules = new NaturalRules();
        privBoard = new Board();
        privBoard.setState(state);
        rules.setupBoard(privBoard, numOfPlayers);

        for (int i = 0; i < 4 * size - 3; i++) {
            for (int j = 0; j < 4 * size - 3; j++) {
                if (state[i][j] > 0 && state[i][j] < 7 && playerNum == rules.getPieceOwnership()[state[i][j]-1]) {
                    isFirstMoveInTurn = true;
                    candidatePlans.add(new Plan(j, i));
                    findMove(candidatePlans.getLast(), playerNum);
                }
            }
        }

        int maxRating = 0;
        for (Plan plan : candidatePlans) {
            plan.evaluateRating();
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
                    for (int[] move : plan.moves) {
                        if (k == move[1] && l == move[0]){
                            visited = true;
                            break;
                        }
                    }
                    if (visited) continue;
                    isPossible = rules.handleMove(y2, x2, k, l, playerNum);
                    if (isPossible == 1 && isFirstMoveInTurn) {
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
}
