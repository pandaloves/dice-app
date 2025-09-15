package se.jensen.meiying.dice;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private int currentPlayerIndex;
    private Dice dice;
    private String gameMode;
    private String gameState;
    private String winner;

    public Game(String player1First, String player1Last, String player2First, String player2Last, String gameMode) {
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameState = "waiting";
        this.dice = new Dice();
        this.gameMode = gameMode;
        setPlayers(player1First, player1Last, player2First, player2Last, gameMode);
    }

    private void setPlayers(String player1First, String player1Last, String player2First, String player2Last, String gameMode) {
        if ("human vs computer".equalsIgnoreCase(gameMode)) {
            players.add(new Player(player1First, player1Last, "player", false));
            players.add(new Player("Computer", "", "computer", true));
        } else if ("human vs human".equalsIgnoreCase(gameMode)) {
            players.add(new Player(player1First, player1Last, "player", false));
            players.add(new Player(player2First, player2Last, "player", false));
        }
        this.gameState = "playing";
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public boolean isGameOver() {
        return players.stream().allMatch(player -> player.getRollsLeft() == 0);
    }

    public int rollForCurrentPlayer() {
        if (isGameOver()) {
            endGame();
            return -1;
        }

        Player current = getCurrentPlayer();
        if (current.getRollsLeft() > 0) {
            int roll = dice.roll();
            current.addToScore(roll);

            if (current.getRollsLeft() == 0 && !isGameOver()) {
                nextPlayer();
            }

            if (isGameOver()) {
                endGame();
            }

            return roll;
        }
        return -1;
    }

    private String chooseWinner() {
        int maxScore = players.stream().mapToInt(Player::getTotalScore).max().orElse(0);
        List<Player> winners = players.stream()
                .filter(player -> player.getTotalScore() == maxScore)
                .toList();

        if (winners.size() == 1) {
            return winners.get(0).getFullname();
        } else {
            return "Draw";
        }
    }

    public void endGame() {
        this.gameState = "end";
        this.winner = chooseWinner();
    }

    public void resetGame() {
        this.players.forEach(Player::resetPlayer);
        this.currentPlayerIndex = 0;
        this.gameState = "playing";
        this.winner = null;
    }

    public String getWinner() {
        return winner;
    }

    public String getGameState() {
        return gameState;
    }
}