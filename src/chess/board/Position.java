package chess.board;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }

    public static Position fromString(String notation) {
        if (notation == null || notation.length() != 2) return null;

        char file = notation.charAt(0);
        char rank = notation.charAt(1);

        int x = rank - '1';     // '1' -> 0, '2' -> 1, ..., '8 -> 7
        int y = file - 'a';   // 'a' -> 0, 'b' -> 1, ..., 'h' -> 7

        if (x < 0 || x > 7 || y < 0 || y > 7) return null;

        return new Position(x, y);
    }

    @Override
    public String toString() {
        char column = (char) ('a' + this.col);  // 0 -> 'a', 1 -> 'b', ..., 7 -> 'h'
        char rank = (char) ('1' + this.row);    // 0 -> '1', 1 -> '2', ..., 7 -> '8'
        return "" + column + rank;
    }

    @Override
    public int hashCode() {
        return row * Board.BOARD_WIDTH + col;
    }
}
