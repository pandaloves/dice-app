package se.jensen.meiying.controller;

import org.springframework.web.bind.annotation.*;
import se.jensen.meiying.dice.Game;
import se.jensen.meiying.dice.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    private Game currentGame;

    @PostMapping("/start")
    public Map<String, Object> startGame(
            @RequestParam String player1First,
            @RequestParam String player1Last,
            @RequestParam String player2First,
            @RequestParam String player2Last,
            @RequestParam String gameMode) {

        currentGame = new Game(player1First, player1Last, player2First, player2Last, gameMode);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Game started");
        response.put("currentPlayer", currentGame.getCurrentPlayer().getFullname());
        response.put("currentPlayerIndex", currentGame.getCurrentPlayerIndex()); // <-- Add this
        response.put("players", getPlayersInfo());
        response.put("gameState", currentGame.getGameState());
        response.put("gameStatus", currentGame.getGameState().equals("playing") ? "playing"
                : currentGame.getGameState().equals("end") ? "finished" : "setup");
        return response;
    }

    @PostMapping("/roll")
    public Map<String, Object> rollDice() {
        Map<String, Object> response = new HashMap<>();

        if (currentGame == null) {
            response.put("error", "No game started");
            return response;
        }

        int rollResult = currentGame.rollForCurrentPlayer();

        response.put("roll", rollResult);
        response.put("currentPlayer", currentGame.getCurrentPlayer().getFullname());
        response.put("currentPlayerIndex", currentGame.getCurrentPlayerIndex());
        response.put("rollsLeft", currentGame.getCurrentPlayer().getRollsLeft());
        response.put("totalScore", currentGame.getCurrentPlayer().getTotalScore());
        response.put("gameState", currentGame.getGameState());
        response.put("gameStatus", currentGame.getGameState().equals("playing") ? "playing"
                : currentGame.getGameState().equals("end") ? "finished" : "setup");
        response.put("players", getPlayersInfo());

        if ("end".equals(currentGame.getGameState())) {
            response.put("winner", currentGame.getWinner());
            response.put("finalScores", getFinalScores());
        }

        return response;
    }

    @GetMapping("/status")
    public Map<String, Object> getGameStatus() {
        Map<String, Object> response = new HashMap<>();

        if (currentGame == null) {
            response.put("gameState", "not_started");
            return response;
        }

        response.put("gameState", currentGame.getGameState());
        response.put("gameStatus", currentGame.getGameState().equals("playing") ? "playing"
                : currentGame.getGameState().equals("end") ? "finished" : "setup");
        response.put("currentPlayer", currentGame.getCurrentPlayer().getFullname());
        response.put("currentPlayerIndex", currentGame.getCurrentPlayerIndex());
        response.put("players", getPlayersInfo());

        if ("end".equals(currentGame.getGameState())) {
            response.put("winner", currentGame.getWinner());
        }

        return response;
    }

    @PostMapping("/reset")
    public Map<String, Object> resetGame() {
        if (currentGame != null) {
            currentGame.resetGame();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Game reset");
        response.put("gameState", currentGame != null ? currentGame.getGameState() : "not_started");
        response.put("gameStatus", currentGame.getGameState().equals("playing") ? "playing"
                : currentGame.getGameState().equals("end") ? "finished" : "setup");
        return response;
    }

    private List<Map<String, Object>> getPlayersInfo() {
        List<Map<String, Object>> playersInfo = new ArrayList<>();
        for (Player player : currentGame.getPlayers()) {
            Map<String, Object> playerInfo = new HashMap<>();
            playerInfo.put("name", player.getFullname());
            playerInfo.put("score", player.getTotalScore());
            playerInfo.put("scores", player.getScores());
            playerInfo.put("rollsLeft", player.getRollsLeft());
            playerInfo.put("isComputer", player.isComputer());
            playersInfo.add(playerInfo);
        }
        return playersInfo;
    }

    private Map<String, Integer> getFinalScores() {
        Map<String, Integer> scores = new HashMap<>();
        for (Player player : currentGame.getPlayers()) {
            scores.put(player.getFullname(), player.getTotalScore());
        }
        return scores;
    }
}