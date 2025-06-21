package chess.pieces;

import constants.Pieces;

public class Pawn extends Piece{
    boolean enPassant = false;

    @Override
    public String getSymbol() {
        return Pieces.PAWN.getSymbol();
    }
    public Pawn(int value, int color) {
        super(value, color);
    }
    public void setEnPassant(boolean state){
        this.enPassant = state;
    }
    public boolean getEnPassant(){
        return this.enPassant;
    }
}
