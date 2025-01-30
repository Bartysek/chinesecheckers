package org.example;

public class EngineFactory {
    public static Engine createEngine(int ruleNum) {
        return switch(ruleNum){
            case 0, 1 -> new NaturalEngine();
            case 2 -> new CaptureEngine();
            default -> null;
        };
    }
}
