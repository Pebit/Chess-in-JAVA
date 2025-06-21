package chess.pieces;

import chess.board.Position;
import constants.Colors;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece implements IPiece{

    protected int value;
    protected int color;
    protected List<Position> legalMoves = new ArrayList<>();
    protected List<Position> legalCaptures = new ArrayList<>();

    public Piece(){
        this.value = 1;
        this.color = Colors.WHITE;
    }
    public Piece(int value, int color){
        this.value = value;
        this.color = color;
    }

    public abstract String getSymbol();
    public int getValue(){
        return this.value;
    }
    public int getColor(){
        return this.color;
    }
    public List<Position> getLegalCaptures() {
        return this.legalCaptures;
    }
    public List<Position> getLegalMoves() {
        return this.legalMoves;
    }
    public void clearLegalMoves(){
        this.legalMoves.clear();
    }
    public void clearLegalCaptures(){
        this.legalCaptures.clear();
    }

    public void addLegalMove(Position position){
        this.legalMoves.add(position);
    }
    public void addLegalCapture(Position position){
        this.legalCaptures.add(position);
    }
    public void removeLegalMove(Position position){
        this.legalMoves.remove(position);
    }
    public void removeLegalCapture(Position position){
        this.legalCaptures.remove(position);
    }

    public boolean canMove(Position move){
        return this.legalMoves.contains(move);
    }
    public boolean canCapture(Position move){
        return this.legalCaptures.contains(move);
    }
}
