package org.example;

public class RulesFactory {
  public static AbstractRules getRules(int ruleNum) {
    return switch(ruleNum){
      case 0 -> new NaturalRules();
      case 1 -> new OoocRules();
      case 2 -> new CaptureRules();
      default -> null;
    };
  }
}
