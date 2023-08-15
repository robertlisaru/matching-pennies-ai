package ro.ulbs.ai.homework.history;

import java.util.ArrayList;
import java.util.List;

public class History {
    private List<Round> roundList = new ArrayList<>();

    public List<Round> getRoundList() {
        return roundList;
    }

    public void addRound(Round round) {
        roundList.add(round);
    }

    public int getNumberOfRounds() {
        return roundList.size();
    }

    public Round getLastRound() {
        if (roundList.isEmpty()) {
            return new Round(false, false);
        }
        return roundList.get(roundList.size() - 1);
    }
}
