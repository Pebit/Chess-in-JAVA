package Player;

import Board.Position;
import pieces.Piece;

import java.util.*;

public class Player {
    private static final Set<Integer> takenColors = new HashSet<>();
    private int color;
    private final List<Piece> capturedPieces = new ArrayList<>();

    public Player(int color){
        if(takenColors.contains(color)){
            throw new IllegalArgumentException("Color already taken: " + color);
        }
        this.color = color;
        takenColors.add(color);
    }
    public void addCapturedPiece(Piece piece){
        capturedPieces.add(piece);
        capturedPieces.sort(Comparator.comparingInt(Piece::getValue).reversed());
    }
    public int getColor(){
        return this.color;
    }
    public List<Piece> getCapturedPieces(){
        return capturedPieces;
    }

    public static void resetColors() {
        takenColors.clear();
    }
}
