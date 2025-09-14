package se.jensen.meiying.dice;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String firstname;
    private String lastname;
    private String playerRole;
    private int totalScore;
    private int rollsLeft;
    private boolean isComputer;
    private List<Integer> scores;

    public Player(String firstname, String lastname, String playerRole, boolean isComputer) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.playerRole = playerRole;
        this.totalScore = 0;
        this.rollsLeft = 2;
        this.isComputer = isComputer;
        this.scores = new ArrayList<>();
    }

    public String getFullname() { return firstname + " " + lastname; }
    public int getTotalScore() { return totalScore; }
    public List<Integer> getScores() { return scores; }

    public void addToScore(int score) {
        this.totalScore += score;
        this.rollsLeft--;
        this.scores.add(score);
    }

    public int getRollsLeft() { return rollsLeft; }
    public boolean isComputer() { return isComputer; }

    public void resetPlayer() {
        this.totalScore = 0;
        this.rollsLeft = 2;
        this.scores.clear();
    }
}
