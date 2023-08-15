package ro.ulbs.ai.homework.strategies;

import ro.ulbs.ai.homework.history.History;

public class FlipMajorityStrategy extends Strategy {
    private int flipCount = 0;

    public FlipMajorityStrategy(History history, String strategyName) {
        super(history, strategyName);
    }

    @Override
    public boolean play() {
        if (hasFlipped(history.getRoundList().size() - 1)) {
            flipCount++;
        }
        if (history.getNumberOfRounds() == 0) {
            return false;
        } else {
            return (((double) flipCount / history.getNumberOfRounds()) > 0.5) ? !history.getLastRound().getPlayerChoice() :
                    history.getLastRound().getPlayerChoice();
        }
    }

    private boolean hasFlipped(int roundNr) {
        if (roundNr < 1) {
            return false;
        } else {
            return history.getRoundList().get(roundNr).getPlayerChoice() !=
                    history.getRoundList().get(roundNr - 1).getPlayerChoice();
        }
    }
}
