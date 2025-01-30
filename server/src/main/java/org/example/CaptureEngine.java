package org.example;

import java.util.ArrayList;

public class CaptureEngine extends NaturalEngine implements Engine {
    @Override
    public int[] getPieceOwnership() {
        return new int[]{pleyerNum, pleyerNum, pleyerNum, pleyerNum, pleyerNum, pleyerNum};
    }

    @Override
    public void createRules() {
        rules = new CaptureRules();
    }

    @Override
    public int evaluateRating(Plan plan) {
        return plan.getSize();
    }
}
