package entities;

import javax.persistence.*;

import static java.lang.Long.signum;

@Entity
@Table(name = "moves")
public class StoredMove implements Comparable<StoredMove>{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "move_number")
  private int moveNum;

  @Column(name = "x1")
  protected int x1;

  @Column(name = "y1")
  protected int y1;

  @Column(name = "x2")
  protected int x2;

  @Column(name = "y2")
  protected int y2;

  @Column(name = "player")
  protected int player;

  @ManyToOne
  protected GameInfo game;

  public StoredMove(GameInfo game, int moveNum, int x1, int y1, int x2, int y2, int player) {
    this.game = game;
    this.moveNum = moveNum;
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.player = player;
  }

  public StoredMove(){}

  public int getMoveNum() {
    return moveNum;
  }

  public long getId() {
    return this.id;
  }

  public int getX1() {
    return x1;
  }

  public int getX2() {
    return x2;
  }

  public int getY1() {
    return y1;
  }

  public int getY2() {
    return y2;
  }

  public int getPlayer() {
    return player;
  }

  public GameInfo getGame() {
    return game;
  }

  @Override
  public int compareTo(StoredMove o) {
     if(player > o.getPlayer()) return 1;
     else if(player == o.getPlayer()) return moveNum - o.getMoveNum();
     else return -1;
  }
}
