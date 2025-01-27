import org.example.Board;
import org.example.CaptureRules;
import org.junit.Test;

import static org.junit.Assert.*;

public class CaptureRulesTest {
    @Test
    public void moveTest() {
        CaptureRules rules = new CaptureRules();
        Board b = new Board();
        rules.setupBoard(b, 2);
        assertEquals(0, rules.handleMove(14, 5, 12, 7, 0));
        assertEquals(1, rules.handleMove(14, 7, 14, 7, 0));
        assertFalse(rules.checkEndCon(0));
        assertEquals(0, rules.handleMove(12, 10, 12, 8, 1));
        assertEquals(0, rules.handleMove(12, 8, 12, 6, 1));
        assertEquals(1, rules.handleMove(12, 6, 12, 6, 1));
        assertFalse(rules.checkEndCon(1));
        assertEquals(0, rules.handleMove(13, 5, 11, 7, 0));
    }

    @Test
    public void endConTest() {
        CaptureRules rules = new CaptureRules();
        Board b = new Board();
        int[][] state = b.getState();
        for(int i = 0; i < state.length; i++) {
            for(int j = 0; j < state.length; j++) {
                state[i][j] = 7;
            }
        }
        b.setState(state);
        rules.setupBoard(b, 2);
        assertTrue(rules.checkEndCon(0));

        state[10][10] = 3;
        assertTrue(rules.checkEndCon(0));

        state[11][9] = 3;
        assertFalse(rules.checkEndCon(0));
    }
}
