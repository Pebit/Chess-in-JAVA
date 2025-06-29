package chess.pieces;

import constants.Pieces;

public class Bishop extends Piece{

    @Override
    public String getSymbol() {
        return Pieces.BISHOP.getSymbol();
    }
    public Bishop(int value, int color) {
        super(value, color);
    }
}
