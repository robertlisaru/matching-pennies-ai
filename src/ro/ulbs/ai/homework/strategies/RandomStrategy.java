package ro.ulbs.ai.homework.strategies;

import ro.ulbs.ai.homework.history.History;

import java.util.Random;

public class RandomStrategy extends Strategy {
    public RandomStrategy(History history, String strategyName) {
        super(history, strategyName);
    }

    @Override
    public boolean play() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
