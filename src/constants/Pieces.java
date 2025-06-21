package constants;

public enum Pieces {
    PAWN(1, "P"), KNIGHT(3, "N"), BISHOP(3, "B"), ROOK(5, "R"),
    QUEEN(9, "Q"), KING(0, "K");
    private final int value;
    private final String symbol;
    private Pieces(int value, String symbol) {
        this.value = value;
        this.symbol = symbol;
    }
    public int getValue(){
        return this.value;
    }
    public String getSymbol(){
        return symbol;
    }
}
