package org.example;

public class StrategyFactory {
  public static CommunicationStrategy getStrategy(int strategyCode) {
    if(strategyCode == CommunicationIndicators.MOVE_INDICATOR.getCode()) {
      return new ReceiveMove();
    }
    else if (strategyCode == CommunicationIndicators.MESSAGE_INDICATOR.getCode()) {
      return new ReceiveMessage();
    }
    else if (strategyCode == CommunicationIndicators.QUESTION_INDICATOR.getCode()) {
      return new ReceiveQuestion();
    }
    else if (strategyCode == CommunicationIndicators.YOUR_TURN_INDICATOR.getCode()) {
      return new ReceiveTurn();
    }
    else if (strategyCode == CommunicationIndicators.ERROR_BYTE.getCode()) {
      return new ErrorStrategy();
    }
    else {
      return new WrongStrategy();
    }
  }
}
