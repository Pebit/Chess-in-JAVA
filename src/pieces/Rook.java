package pieces;

import Constants.Pieces;

public class Rook extends Piece{
    private boolean moved = false;

    @Override
    public String getSymbol() {
        return Pieces.ROOK.getSymbol();
    }
    public Rook(int value, int color) {
        super(value, color);
    }
    public void moved(){
        this.moved = true;
    }

    public boolean hasMoved(){
        return this.moved;
    }
}
