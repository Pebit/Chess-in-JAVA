package pieces;

import Constants.Pieces;

public class Knight extends Piece{
    @Override
    public String getSymbol() {
        return Pieces.KNIGHT.getSymbol();
    }
    public Knight(int value, int color) {
        super(value, color);
    }
}
