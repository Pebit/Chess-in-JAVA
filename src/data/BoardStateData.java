package data;

import java.sql.Timestamp;

public class BoardStateData {
    private int boardId;
    private int gameId;
    private String currentTurn; // "WHITE" or "BLACK"
    private Timestamp lastUpdated;
    private String boardLayout;

    public BoardStateData(int boardId, int gameId, String currentTurn, Timestamp lastUpdated, String boardLayout) {
        this.boardId = boardId;
        this.gameId = gameId;
        this.currentTurn = currentTurn;
        this.lastUpdated = lastUpdated;
        this.boardLayout = boardLayout;
    }

    public BoardStateData(int gameId, String currentTurn, Timestamp lastUpdated, String boardLayout) {
        this(0, gameId, currentTurn, lastUpdated, boardLayout);
    }

    // Getters and Setters
    public int getBoardId() { return boardId; }
    public void setBoardId(int boardId) { this.boardId = boardId; }

    public int getGameId() { return gameId; }
    public String getCurrentTurn() { return currentTurn; }
    public Timestamp getLastUpdated() { return lastUpdated; }
    public String getBoardLayout() { return boardLayout; }
}