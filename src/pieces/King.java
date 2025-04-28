package pieces;

import Constants.Pieces;

public class King extends Piece{
    private boolean moved = false;

    @Override
    public String getSymbol() {
        return Pieces.KING.getSymbol();
    }
    public King(int value, int color) {
        super(value, color);
    }

    public void moved(){
        this.moved = true;
    }
    public boolean hasMoved(){
        return this.moved;
    }
}
