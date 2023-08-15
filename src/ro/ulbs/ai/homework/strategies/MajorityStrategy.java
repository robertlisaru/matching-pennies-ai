package ro.ulbs.ai.homework.strategies;

import ro.ulbs.ai.homework.history.History;
import ro.ulbs.ai.homework.history.Round;

public class MajorityStrategy extends Strategy {

    public MajorityStrategy(History history, String strategyName) {
        super(history, strategyName);
    }

    @Override
    public boolean play() {
        int cardinality = 0;
        for (Round round : history.getRoundList()) {
            if (round.getPlayerChoice()) {
                cardinality++;
            }
        }
        return (history.getNumberOfRounds() == 0) || (((double) cardinality / history.getNumberOfRounds()) > 0.5);
    }
}
