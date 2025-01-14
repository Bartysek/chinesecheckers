package org.example;

public class StrategyFactory {
  public static CommunicationStrategy getStrategy(int strategyCode) {
    System.out.println("code: " + strategyCode);
    if (strategyCode == CommunicationIndicators.BOARD_STATE_INDICATOR.getCode()) {
      return new ReceiveBoardState();
    }
    if (strategyCode == CommunicationIndicators.MESSAGE_INDICATOR.getCode()) {
      return new ReceiveMessage();
    }
    else if (strategyCode == CommunicationIndicators.QUESTION_INDICATOR.getCode()) {
      return new ReceivePlayerCountQuestion();
    }
    else if (strategyCode == CommunicationIndicators.YOUR_TURN_INDICATOR.getCode()) {
      return new ReceiveTurn();
    }
    else if (strategyCode == CommunicationIndicators.QUESTION_GAMEMODE_INDICATOR.getCode()) {
      return new ReceiveGameModeQuestion();
    }
    else if (strategyCode == CommunicationIndicators.PIECE_REMOVE_INDICATOR.getCode()) {
      return new ReceivePieceRemove();
    }
    else if (strategyCode == CommunicationIndicators.PIECE_ADD_INDICATOR.getCode()) {
      return new ReceivePieceAdd();
    }
    else if (strategyCode == CommunicationIndicators.END_MOVE_INDICATOR.getCode()) {
      return new ReceiveEndOfMove();
    }
    else if (strategyCode == CommunicationIndicators.ERROR_BYTE.getCode()) {
      return new ErrorStrategy();
    }
    else {
      return new WrongStrategy();
    }
  }
}
