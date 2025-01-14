import org.example.Board;
import org.example.NaturalRules;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NaturalRulesTest {
    int[][] getSolvedState(int n) {
        int size = 4 * n - 3;
        int[][] state = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i < n - 1) {
                    if (j <= size - n && j >= size - n - i) state[i][j] = 1;
                } else if (i < 2 * n - 2) {
                    if (j <= size - n && j >= size - n - i) state[i][j] = 7;
                    else if (j > size - n && j < size - i + n - 1) state[i][j] = 2;
                    else if (j >= n-1 && j < size-n) state[i][j] = 6;
                } else if (i == size/2) {
                    if (j >= n-1 && j <= size - n) state[i][j] = 7;
                } else {
                    if (state[size-i-1][size-j-1] == 1) state[i][j] = 4;
                    else if (state[size-i-1][size-j-1] == 6) state[i][j] = 3;
                    else if (state[size-i-1][size-j-1] == 2) state[i][j] = 5;
                    else state[i][j] = state[size-i-1][size-j-1];
                }
            }
        }
        return state;
    }

    @Test
    public void testEndCon(){
        NaturalRules tested = new NaturalRules();
        Board b = new Board();
        b.setState(getSolvedState(5));
        tested.setBoard(b, 2);
        int[][] s = b.getState();
        int size = s.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(s[i][j]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
        assertTrue(tested.checkEndCon(0));
        assertTrue(tested.checkEndCon(1));
    }

    @Test
    public void testNoEnd(){
        NaturalRules tested = new NaturalRules();
        Board b = new Board();
        b.setState(getSolvedState(5));
        tested.setBoard(b, 2);
        b.remove(10, 3);
        int[][] s = b.getState();
        int size = s.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(s[i][j]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
        assertFalse(tested.checkEndCon(0));
    }
}
