package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.TreeSet;
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

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name="game_id")
  private Set<StoredMove> moves;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "game_id")
  private Set<StoredStatePiece> initialState;

  @Column(name = "last_move_num")
  private int lastMoveNum = 0;

  public GameInfo() {
    this.moves = new HashSet<>();
    this.initialState = new HashSet<>();
  }

  public GameInfo(int mode, int numPlayers){
    this.gameMode = mode;
    this.numPlayers = numPlayers;
    this.moves = new HashSet<>();
    this.initialState = new HashSet<>();
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

  public int getGameMode() {
    return gameMode;
  }

  public int progressMoveNum() {
    return lastMoveNum++;
  }

  public void setInitialState(TreeSet<StoredStatePiece> state) {
    this.initialState = state;
  }


}
