package ro.ulbs.ai.homework.game;

import ro.ulbs.ai.homework.history.History;
import ro.ulbs.ai.homework.history.Round;
import ro.ulbs.ai.homework.strategies.DecisionTreeStrategy;
import ro.ulbs.ai.homework.strategies.RandomStrategy;
import ro.ulbs.ai.homework.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;

public class ComputerOpponent implements Opponent {
    private History history = new History();
    private Strategy bestStrategy;
    private List<Strategy> strategyList = new ArrayList<>();
    private int maxStrategyScore = 15;
    private int playerPoints = 0;
    private int opponentPoints = 0;

    public ComputerOpponent() {
        Strategy randomStrategy = new RandomStrategy(history, "Random");
        randomStrategy.setScore(maxStrategyScore);
        Strategy decisionTree = new DecisionTreeStrategy(history, "Decision tree");
        bestStrategy = decisionTree;
        strategyList.add(decisionTree);
        //strategyList.add(new FlipMajorityStrategy(history, "Flip majority"));
        //strategyList.add(new MajorityStrategy(history, "Majority"));
        //strategyList.add(new TitForTatStrategy(history, "Tit for tat"));
        //strategyList.add(randomStrategy);
    }

    public Strategy getBestStrategy() {
        return bestStrategy;
    }

    public int getMaxStrategyScore() {
        return maxStrategyScore;
    }

    public List<Strategy> getStrategyList() {
        return strategyList;
    }

    @Override
    public boolean play(boolean playerChoice) {
        Round currentRound;
        currentRound = new Round();
        currentRound.setPrediction(bestStrategy.play());
        currentRound.setPlayerChoice(playerChoice);
        if (playerChoice == currentRound.getPrediction()) {
            opponentPoints++;
        } else {
            playerPoints++;
        }
        if (currentRound.getPrediction() == playerChoice) {
            bestStrategy.setScore(bestStrategy.getScore() == maxStrategyScore ? maxStrategyScore : bestStrategy.getScore() + 1);
            bestStrategy.setCorrectGuesses(bestStrategy.getCorrectGuesses() + 1);
        } else {
            bestStrategy.setScore(bestStrategy.getScore() == 0 ? 0 : bestStrategy.getScore() - 1);
        }
        updateScores(playerChoice);
        history.addRound(currentRound);
        return currentRound.getPrediction();
    }

    @Override
    public int getPlayerPoints() {
        return playerPoints;
    }

    @Override
    public int getOpponentPoints() {
        return opponentPoints;
    }

    private void updateScores(boolean playerChoice) {
        for (Strategy strategy : strategyList) {
            if (strategy != bestStrategy) {
                if (strategy.play() == playerChoice) {
                    strategy.setScore(strategy.getScore() == maxStrategyScore ? maxStrategyScore : strategy.getScore() + 1);
                    strategy.setCorrectGuesses(strategy.getCorrectGuesses() + 1);
                } else {
                    strategy.setScore(strategy.getScore() == 0 ? 0 : strategy.getScore() - 1);
                }
            }
        }
        int bestScore = -1;
        for (Strategy strategy : strategyList) {
            if (strategy.getScore() > bestScore) {
                bestScore = strategy.getScore();
                bestStrategy = strategy;
            }
        }
    }

    @Override
    public History getHistory() {
        return history;
    }
}
