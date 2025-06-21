package data;

public class MoveData {
    private int moveId;
    private int gameId;
    private int moveNumber;
    private String movedPiece;
    private String capturedPiece; // could be null
    private String fromPosition;
    private String toPosition;

    public MoveData(int moveId, int gameId, int moveNumber, String movedPiece, String capturedPiece, String fromPosition, String toPosition) {
        this.moveId = moveId;
        this.gameId = gameId;
        this.moveNumber = moveNumber;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }

    public MoveData(int gameId, int moveNumber, String movedPiece, String capturedPiece, String fromPosition, String toPosition) {
        this(0, gameId, moveNumber, movedPiece, capturedPiece, fromPosition, toPosition);
    }

    // Getters and setters
    public int getMoveId() { return moveId; }
    public void setMoveId(int moveId) { this.moveId = moveId; }

    public int getGameId() { return gameId; }
    public int getMoveNumber() { return moveNumber; }
    public String getMovedPiece() { return movedPiece; }
    public String getCapturedPiece() { return capturedPiece; }
    public String getFromPosition() { return fromPosition; }
    public String getToPosition() { return toPosition; }
}