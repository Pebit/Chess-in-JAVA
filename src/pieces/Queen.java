package pieces;

import Constants.Pieces;

public class Queen extends Piece{
    @Override
    public String getSymbol() {
        return Pieces.QUEEN.getSymbol();
    }
    public Queen(int value, int color) {
        super(value, color);
    }
}
