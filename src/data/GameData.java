package data;

import java.time.LocalDateTime;

public class GameData {
    private int gameId;
    private int whitePlayerId;
    private int blackPlayerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String result;
    private String finalBoardState;
    private String currentTurn;

    public GameData(int gameId, int whitePlayerId, int blackPlayerId, LocalDateTime startTime,
                    LocalDateTime endTime, String result, String finalBoardState, String currentTurn) {
        this.gameId = gameId;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.result = result;
        this.finalBoardState = finalBoardState;
        this.currentTurn = currentTurn;
    }

    public GameData(int whitePlayerId, int blackPlayerId, LocalDateTime startTime) {
        this(0, whitePlayerId, blackPlayerId, startTime, null, null, null, "WHITE");
    }

    // Getters and setters
    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }

    public int getWhitePlayerId() { return whitePlayerId; }
    public int getBlackPlayerId() { return blackPlayerId; }
    public LocalDateTime getStartTime() { return startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getFinalBoardState() { return finalBoardState; }
    public void setFinalBoardState(String finalBoardState) { this.finalBoardState = finalBoardState; }

    public String getCurrentTurn() { return currentTurn; }
    public void setCurrentTurn(String currentTurn) { this.currentTurn = currentTurn; }
}
