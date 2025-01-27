
import org.example.Board;
import org.example.OoocRules;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OoocRulesTest {
    @Test
    public void testScramble() {
        OoocRules r = new OoocRules();
        r.setupBoard(new Board(), 2);
        assertTrue(true);
    }
}
