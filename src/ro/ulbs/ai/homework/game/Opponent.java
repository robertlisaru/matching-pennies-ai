package ro.ulbs.ai.homework.game;

import ro.ulbs.ai.homework.history.History;

public interface Opponent {
    History getHistory();

    int getPlayerPoints();

    int getOpponentPoints();

    boolean play(boolean playerChoice);
}
