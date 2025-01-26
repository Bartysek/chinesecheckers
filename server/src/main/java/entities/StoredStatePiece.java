package entities;

import javax.persistence.*;

import static java.lang.Long.signum;

@Entity
@Table(name="initialstate")
public class StoredStatePiece implements Comparable<StoredStatePiece>{
  public StoredStatePiece(GameInfo game, int x, int y, int piece) {
    this.game = game;
    this.x = x;
    this.y = y;
    this.piece = piece;
  }
  public StoredStatePiece(){};

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  protected long id;

  @ManyToOne
  protected GameInfo game;

  @Column(name = "x")
  protected int x;

  @Column(name = "y")
  protected int y;

  @Column(name = "piece")
  protected int piece;

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getPiece() {
    return piece;
  }

  public long getId() {
    return id;
  }

  public GameInfo getGame() {
    return game;
  }

  public void setGame(GameInfo game) {
    this.game = game;
  }

  public void setPiece(int piece) {
    this.piece = piece;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }


  @Override
  public int compareTo(StoredStatePiece o) {
    return signum(this.id - o.getId());
  }
}
