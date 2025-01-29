package org.example;

public enum CommunicationIndicators {
  BOARD_STATE_INDICATOR(255),
  MESSAGE_INDICATOR(254),
  END_OF_MESSAGE(253),
  QUESTION_SETTINGS_INDICATOR(252),
  YOUR_TURN_INDICATOR(251),
  QUESTION_SERVER_MODE_INDICATOR(250),
  PIECE_REMOVE_INDICATOR(249),
  PIECE_ADD_INDICATOR(248),
  END_MOVE_INDICATOR(247),
  ERROR_BYTE(-1);

  private final int code;

  CommunicationIndicators(final int c) {
    this.code = c;
  }

  public int getCode() {
    return this.code;
  }
}
