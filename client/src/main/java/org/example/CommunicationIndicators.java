package org.example;

public enum CommunicationIndicators {
  MESSAGE_INDICATOR(254),
  MOVE_INDICATOR(255),
  END_OF_MESSAGE(253),
  QUESTION_INDICATOR(252),
  YOUR_TURN_INDICATOR(251);

  private final int code;

  CommunicationIndicators(int code) {
    this.code = code;
  }

  public int getCode(){
    return this.code;
  }
}
