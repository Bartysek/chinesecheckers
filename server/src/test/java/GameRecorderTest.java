import entities.GameInfo;
import org.example.GameDAO;
import org.example.GameRecorder;
import org.junit.Test;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GameRecorderTest {


  @Test
  public void testWinner() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
    GameDAO gd = (GameDAO) ctx.getBean("Dao");

    GameInfo gi = new GameInfo();
    GameRecorder gr = new GameRecorder(gi, gd);
    gr.sendMessage("Player 5 won!");
    Assert.assertEquals(5, gi.getWinner());
  }
}
