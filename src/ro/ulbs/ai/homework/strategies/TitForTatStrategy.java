package ro.ulbs.ai.homework.strategies;

import ro.ulbs.ai.homework.history.History;

public class TitForTatStrategy extends Strategy {
    public TitForTatStrategy(History history, String strategyName) {
        super(history, strategyName);
    }

    @Override
    public boolean play() {
        return history.getLastRound().getPlayerChoice();
    }
}
