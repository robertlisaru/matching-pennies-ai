package ro.ulbs.ai.homework.strategies;

import ro.ulbs.ai.homework.history.History;

public abstract class Strategy {
    protected History history;
    private int score = 0;
    private String strategyName;
    private int correctGuesses = 0;

    public Strategy(History history, String strategyName) {
        this.history = history;
        this.strategyName = strategyName;
    }

    public int getCorrectGuesses() {
        return correctGuesses;
    }

    public void setCorrectGuesses(int correctGuesses) {
        this.correctGuesses = correctGuesses;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public abstract boolean play();

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
