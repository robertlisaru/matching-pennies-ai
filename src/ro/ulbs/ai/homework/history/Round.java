package ro.ulbs.ai.homework.history;

public class Round {
    private boolean prediction;
    private boolean playerChoice;

    public Round(boolean prediction, boolean playerChoice) {
        this.prediction = prediction;
        this.playerChoice = playerChoice;
    }

    public Round() {
    }

    public boolean getPrediction() {
        return prediction;
    }

    public void setPrediction(boolean prediction) {
        this.prediction = prediction;
    }

    public boolean getPlayerChoice() {
        return playerChoice;
    }

    public void setPlayerChoice(boolean playerChoice) {
        this.playerChoice = playerChoice;
    }
}
