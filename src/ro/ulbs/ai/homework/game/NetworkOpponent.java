package ro.ulbs.ai.homework.game;

import ro.ulbs.ai.homework.history.History;
import ro.ulbs.ai.homework.history.Round;
import ro.ulbs.ai.homework.network.Connection;

import java.io.IOException;

public class NetworkOpponent implements Opponent {
    private Connection connection;
    private History history = new History();
    private int playerScore = 0;
    private int opponentScore = 0;

    public NetworkOpponent(Connection connection) {
        this.connection = connection;
    }

    @Override
    public History getHistory() {
        return history;
    }

    @Override
    public int getPlayerPoints() {
        return playerScore;
    }

    @Override
    public int getOpponentPoints() {
        return opponentScore;
    }

    @Override
    public boolean play(boolean playerChoice) {
        Round currentRound;
        currentRound = new Round();
        currentRound.setPlayerChoice(playerChoice);
        connection.send(String.valueOf(playerChoice));
        try {
            currentRound.setPrediction(Boolean.parseBoolean(connection.receive()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (playerChoice == currentRound.getPrediction()) {
            opponentScore++;
        } else {
            playerScore++;
        }
        history.addRound(currentRound);
        return currentRound.getPrediction();
    }
}
