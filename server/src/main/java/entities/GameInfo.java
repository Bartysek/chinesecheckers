package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name="GameInfo")
@Table(name="gameinfo")
public class GameInfo{

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "game_mode")
  private int gameMode;

  @Column(name = "num_players")
  private int numPlayers;

  @Column(name = "winning_player")
  private int winner;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name="game_id")
  private Set<StoredMove> moves;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "game_id")
  private Set<StoredStatePiece> initialState;

  public GameInfo() {
    this.moves = new HashSet<>();
  }

  public GameInfo(int mode, int numPlayers){
    this.gameMode = mode;
    this.numPlayers = numPlayers;
    this.moves = new HashSet<>();
    this.initialState = null;
  }

  public long getId() {
    return this.id;
  }

  public int getMode() {
    return gameMode;
  }

  public int getNumPlayers(){
    return numPlayers;
  }

  public Set<StoredMove> getMoves() {
    return moves;
  }

  public Set<StoredStatePiece> getInitialState() {
    return initialState;
  }

  public void setWinner(int winner) {
    this.winner = winner;
  }

  public int getWinner(){
    return winner;
  }

  public void setInitialState(HashSet<StoredStatePiece> state) {
    this.initialState = state;
  }


}
