package entities;

import javax.persistence.*;

@Entity
@Table(name="initialstate")
public class StoredStatePiece{
  public StoredStatePiece(GameInfo game, int x, int y, int piece) {
    this.game = game;
    this.x = x;
    this.y = y;
    this.piece = piece;
  }

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

}
