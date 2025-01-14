package org.example;

public enum CommunicationIndicators {
  MESSAGE_INDICATOR(254),
  END_OF_MESSAGE(253),
  QUESTION_INDICATOR(252),
  QUESTION_GAMEMODE_INDICATOR(250),
  YOUR_TURN_INDICATOR(251),
  PIECE_REMOVE_INDICATOR(249),
  PIECE_ADD_INDICATOR(248),
  ERROR_BYTE(-1);

  private final int code;

  CommunicationIndicators(final int c) {
    this.code = c;
  }

  public int getCode() {
    return this.code;
  }
}
