package org.example;

import entities.GameInfo;
import entities.StoredMove;
import entities.StoredStatePiece;

import java.util.*;

public class ExistingGameHandling {
  private static int[][] extractInitialState(TreeSet<StoredStatePiece> stored, long game_id) {
    ArrayList<StoredStatePiece> pieces = new ArrayList<>();
    int size = 0;
    for (Iterator<StoredStatePiece> it = stored.iterator(); it.hasNext(); ) {
      StoredStatePiece p = it.next();
      System.out.println(p.getGame().getId());
      System.out.println(p.getX());
      System.out.println(p.getPiece());
      if(p.getGame().getId() == game_id) {
        pieces.add(p);
        if(p.getX() > size)
          size = p.getX();
      }
    }
    int[][] state = new int[size+1][size+1];
    while(!pieces.isEmpty()) {
      StoredStatePiece p = pieces.removeLast();
      state[p.getX()][p.getY()] = p.getPiece();
    }
    return state;
  }

  public static Game LoadGame(long id, GameDAO dao) {
    try {
      GameInfo load = dao.loadGame(id);
      if (load.getWinner() != 0) return null;
      int ruleNum = load.getGameMode();
      AbstractRules rules = RulesFactory.getRules(ruleNum);
      Board b = new Board();
      b.setState(extractInitialState(new TreeSet<>(load.getInitialState()), load.getId()));
      assert rules != null;
      rules.setBoard(b, load.getNumPlayers());
      Comparator<StoredMove> comp = Comparator.comparingInt(StoredMove::getMoveNum);
      TreeSet<StoredMove> sortedMoves = new TreeSet<>(comp);
      sortedMoves.addAll(load.getMoves());
      for (Iterator<StoredMove> it = sortedMoves.iterator(); it.hasNext(); ) {
        StoredMove sm = it.next();
        rules.handleMove(sm.getY1(), sm.getX1(), sm.getY2(), sm.getX2(), sm.getPlayer());
        System.out.println(sm.getId());
      }
      Game game = new Game();
      game.setGameRules(rules);
      game.setNoPlayers(load.getNumPlayers());
      game.setPrepared(true);
      game.setSave(load);
      return game;
    } catch (Exception e) {
      return null;
    }
  }

  public static Game PlaybackGame(long id, GameDAO dao) {
    try {
      GameInfo load = dao.loadGame(id);
      int ruleNum = load.getGameMode();
      AbstractRules rules = switch (ruleNum) {
        case 0 -> new NaturalRules();
        case 1 -> new OoocRules();
        case 2 -> new CaptureRules();
        default -> null;
      };
      Board b = new Board();
      b.setState(extractInitialState(new TreeSet<>(load.getInitialState()), load.getId()));
      assert rules != null;
      rules.setBoard(b, load.getNumPlayers());
      Game game = new Game();
      game.setGameRules(rules);
      game.setNoPlayers(load.getNumPlayers());
      game.setPrepared(true);
      game.setPlayback(true);
      game.setSave(load);
      List<PlayerInterface> players = game.getPlayers();
      game.setPlaying(load.getNumPlayers() - 1);
      TreeSet<StoredMove> moves = new TreeSet<>(load.getMoves());
      for (int i = 0; i < load.getNumPlayers(); i++) {
        TreeSet<StoredMove> playerMoves = new TreeSet<>();
        for (StoredMove m : moves) {
          if (m.getPlayer() == i) {
            playerMoves.add(m);
          }
        }
        for (StoredMove m : playerMoves) {
          moves.remove(m);
        }
        players.add(new PlaybackPlayer(i, playerMoves));
      }
      return game;
    } catch (Exception e) {
      return null;
    }
  }
}
