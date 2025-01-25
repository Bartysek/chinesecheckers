package entities;

import javax.persistence.*;

@Entity
@Table(name = "moves")
public class StoredMove{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

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
    this.game.getMoves().add(this);
  }
  public int getMoveNum() {
    return moveNum;
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

}
