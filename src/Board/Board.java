package Board;
import Constants.Colors;
import Constants.GameStates;
import pieces.*;
import Constants.Pieces;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Board {
    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;

    private final Map<Position, Piece> board = new HashMap<>();
    
    public Board() {
        // the chess table is read from left to right, down to up.
        // kings
        board.put(new Position(6, 7), new King(Pieces.KING.getValue(), Colors.WHITE));
        board.put(new Position(2, 4), new King(Pieces.KING.getValue(), Colors.BLACK));
        // black pieces
        board.put(new Position(3, 5), new Rook(Pieces.ROOK.getValue(), Colors.BLACK));
        board.put(new Position(0, 7), new Rook(Pieces.ROOK.getValue(), Colors.BLACK));
        // white defending queen
        board.put(new Position(5, 7), new Queen(Pieces.QUEEN.getValue(), Colors.WHITE));


    }

    public void showBoard() {
        // we want to look at the board from white's perspective so we show the board from last row to first
        for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
            System.out.print((row + 1) + " ");
            for (int col = 0; col < BOARD_WIDTH; col++) {
                Piece piece = board.get(new Position(row, col));
                if (piece == null) {
                    System.out.print("O");
                } else {
                    if(piece.getColor() == Colors.WHITE){ System.out.print(piece.getSymbol());}
                    else System.out.print(piece.getSymbol().toLowerCase());
                }
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.print("  ");
        for (int col = 0; col < BOARD_WIDTH; col++) {
            System.out.print((char)('a' + col) + " ");
        }
        System.out.println();
    }

    private boolean inBounds(int row, int col){
        return col >= 0 && col < BOARD_WIDTH &&
               row >= 0 && row < BOARD_HEIGHT;
    }
    private boolean CheckVerify(Piece piece, Position currentPosition, Position destination){
        int color = piece.getColor();
        Piece capturedPiece = board.get(destination);
        board.put(destination, piece);
        board.remove(currentPosition);

        // we check the board if the king is in check after the move
        if(isKingInCheck(color)){
            // if it is we return the board to the original state and give answer true
            board.put(currentPosition, piece);
            board.put(destination, capturedPiece);
            return true;
        }
        // if it's not in check
        // we return the board to the original state and give answer false
        board.put(currentPosition, piece);
        if (capturedPiece != null) {
            board.put(destination, capturedPiece);
        } else {
            board.remove(destination);
        }
        return false;

    };
    private void calculatePawnMoves(Piece pawn, Position position){
        int forward = pawn.getColor() == Colors.WHITE ? 1 : -1;
        int row = position.getRow();
        int col = position.getCol();

        // moves
        if(inBounds(row+forward, col)){
            // forward 1 step
            Position new_pos = new Position(row+forward, col);
            Piece target = board.get(new_pos);
            if(target == null){
                pawn.addLegalMove(new_pos);

                // forward 2 step if pawn hasn't moved
                if(pawn.getColor() == Colors.WHITE && row == 1 || pawn.getColor() == Colors.BLACK && row == 6){
                    new_pos = new Position(row + 2 * forward, col);
                    target = board.get(new_pos);
                    if (target == null){
                        pawn.addLegalMove(new_pos);
                    }
                }
            }
        }

        // captures
        if(inBounds(row+forward, col-1)){
            // normal capture
            Position new_pos = new Position(row+forward, col-1);
            Piece target = board.get(new_pos);
            if(target != null && target.getColor() != pawn.getColor()){
                pawn.addLegalCapture(new_pos);
            }
            // en passant
            if(inBounds(row, col-1)){
                new_pos = new Position(row+forward, col-1);
                target = board.get(new Position(row, col-1));
                if(target != null && target.getColor() != pawn.getColor() && target.getSymbol().equals(Pieces.PAWN.getSymbol())){
                    Pawn enemy_pawn = (Pawn) target;
                    if (enemy_pawn.getEnPassant()){
                        pawn.addLegalCapture(new_pos);
                    }
                }
            }
        }

        if(inBounds(row+forward, col+1)){
            // normal capture
            Position new_pos = new Position(row+forward, col+1);
            Piece target = board.get(new_pos);
            if(target != null && target.getColor() != pawn.getColor()){
                pawn.addLegalCapture(new_pos);
            }
            // en passant
            if(inBounds(row, col+1)){
                // new_pos = new Position(row+forward, col+1);
                target = board.get(new Position(row, col+1));
                if(target != null && target.getColor() != pawn.getColor() && target.getSymbol().equals(Pieces.PAWN.getSymbol())){
                    Pawn enemy_pawn = (Pawn) target;
                    if (enemy_pawn.getEnPassant()){
                        pawn.addLegalCapture(new_pos);
                    }
                }
            }
        }
    }

    private void calculateKnightMoves(Piece knight, Position position){
        int row = position.getRow();
        int col = position.getCol();

        // moves & captures
        for(int indexRow : new int[]{-2, 2}){
            for(int indexCol : new int[]{-2, 2}){
                int new_row = row + indexRow;
                int new_col = col + indexCol;
                Position new_pos = new Position(new_row - indexRow/2, new_col);
                Piece target = board.get(new_pos);
                if(target == null) {
                    knight.addLegalMove(new_pos);
                } else if (target.getColor() != knight.getColor()){
                    knight.addLegalCapture(new_pos);
                }
                new_pos = new Position(new_row, new_col - indexCol/2);
                target = board.get(new_pos);
                if(target == null) {
                    knight.addLegalMove(new_pos);
                } else if (target.getColor() != knight.getColor()){
                    knight.addLegalCapture(new_pos);
                }
            }
        }
        // that's all
    }

    private void calculateBishopMoves(Piece bishop, Position position){
        int row = position.getRow();
        int col = position.getCol();

        // moves & captures
        for(int incRow : new int[]{-1, 1}){
            for(int incCol : new int[]{-1, 1}){
                int current_row = row + incRow;
                int current_col = col + incCol;
                boolean hit_piece = false;
                while(inBounds(current_row, current_col) && !hit_piece) {
                    Position current_position = new Position(current_row, current_col);
                    Piece current_piece = board.get(current_position);

                    // if square is free => add to moves
                    if (current_piece == null){
                        bishop.addLegalMove(current_position);
                    }
                    // if square is enemy piece => add to captures, hit_piece = true
                    else if(current_piece.getColor() != bishop.getColor()){
                        bishop.addLegalCapture(current_position);
                        hit_piece = true;
                    }
                    // if square is friendly piece => hit_piece = true
                    else{
                        hit_piece = true;
                    }
                    current_row += incRow;
                    current_col += incCol;
                }
            }
        }
    }

    private void calculateRookMoves(Piece rook, Position position){
        int row = position.getRow();
        int col = position.getCol();

        // moves & captures
        for(int forward : new int[]{-1, 1}){
            // left and right
            int current_col = col + forward;
            boolean hit_piece = false;
            while(inBounds(row, current_col) && !hit_piece) {
                Position current_position = new Position(row, current_col);
                Piece current_piece = board.get(current_position);

                // if square is free => add to moves
                if (current_piece == null){
                    rook.addLegalMove(current_position);
                }
                // if square is enemy piece => add to captures, hit_piece = true
                else if(current_piece.getColor() != rook.getColor()){
                    rook.addLegalCapture(current_position);
                    hit_piece = true;
                }
                // if square is friendly piece => hit_piece = true
                else{
                    hit_piece = true;
                }
                current_col += forward;
            }

            //
            int current_row = row + forward;
            hit_piece = false;
            while(inBounds(current_row, col) && !hit_piece) {
                Position current_position = new Position(current_row, col);
                Piece current_piece = board.get(current_position);

                // if square is free => add to moves
                if (current_piece == null){
                    rook.addLegalMove(current_position);
                }
                // if square is enemy piece => add to captures, hit_piece = true
                else if(current_piece.getColor() != rook.getColor()){
                    rook.addLegalCapture(current_position);
                    hit_piece = true;
                }
                // if square is friendly piece => hit_piece = true
                else{
                    hit_piece = true;
                }
                current_row += forward;
            }
        }
    }

    private void calculateQueenMoves(Piece queen, Position position){
        calculateRookMoves(queen, position);
        calculateBishopMoves(queen, position);
    }

    private void calculateKingMoves(Piece king, Position position){
        int row = position.getRow();
        int col = position.getCol();
        Position new_position;
        Piece new_piece;

        for(int forward : new int[]{-1, 1}){
            // north, south:
            if(inBounds(row + forward, col)){
                new_position = new Position(row + forward, col);
                new_piece = board.get(new_position);
                if (new_piece == null) {
                    king.addLegalMove(new_position);
                } else if (new_piece.getColor() != king.getColor()){
                    king.addLegalCapture(new_position);
                }
            }
            // east, west
            if(inBounds(row, col+forward)){
                new_position = new Position(row, col + forward);
                new_piece = board.get(new_position);
                if (new_piece == null) {
                    king.addLegalMove(new_position);
                } else if (new_piece.getColor() != king.getColor()){
                    king.addLegalCapture(new_position);
                }
            }
            // north-east, south-west
            if(inBounds(row + forward, col+forward)){
                new_position = new Position(row + forward, col + forward);
                new_piece = board.get(new_position);
                if (new_piece == null) {
                    king.addLegalMove(new_position);
                } else if (new_piece.getColor() != king.getColor()){
                    king.addLegalCapture(new_position);
                }
            }
            // north-west, south-east
            if(inBounds(row+forward, col-forward)){
                new_position = new Position(row + forward, col - forward);
                new_piece = board.get(new_position);
                if (new_piece == null) {
                    king.addLegalMove(new_position);
                } else if (new_piece.getColor() != king.getColor()){
                    king.addLegalCapture(new_position);
                }
            }
        }
        // moves & captures

    }

    // we update all legal moves and captures for the piece at the selected position
    // update Legal checks that the moves don't result in king getting checked
    public void updateLegalFor(Position position) {
        Piece piece = board.get(position);
        if (piece == null) {
            return;
        }
        piece.clearLegalMoves();
        piece.clearLegalCaptures();

        String symbol = piece.getSymbol();
        int color = piece.getColor();
        if (symbol.equals(Pieces.PAWN.getSymbol())) {
            calculatePawnMoves(piece, position);
        } else if (symbol.equals(Pieces.KNIGHT.getSymbol())) {
            calculateKnightMoves(piece, position);
        } else if (symbol.equals(Pieces.BISHOP.getSymbol())) {
            calculateBishopMoves(piece, position);
        } else if (symbol.equals(Pieces.ROOK.getSymbol())) {
            calculateRookMoves(piece, position);
        } else if (symbol.equals(Pieces.QUEEN.getSymbol())) {
            calculateQueenMoves(piece, position);
        } else if (symbol.equals(Pieces.KING.getSymbol())) {
            calculateKingMoves(piece, position);
        }

        // if the move or capture leads in check it is illegal so we remove it
        List<Position> toRemove = new ArrayList<>();
        for(Position move : piece.getLegalMoves()){
            if(CheckVerify(piece, position, move)){
                toRemove.add(move);
            }
        }
        piece.getLegalMoves().removeAll(toRemove);
        toRemove.clear();
        for(Position capture : piece.getLegalCaptures()){
            if(CheckVerify(piece, position, capture)){
                toRemove.add(capture);
            }
        }
        piece.getLegalMoves().removeAll(toRemove);

    }

    // update Possible doesn't check if their king might get checked
    public void updatePossibleFor(Position position) {
        Piece piece = board.get(position);
        if (piece == null) {
            return;
        }
        piece.clearLegalMoves();
        piece.clearLegalCaptures();

        String symbol = piece.getSymbol();
        int color = piece.getColor();
        if (symbol.equals(Pieces.PAWN.getSymbol())) {
            calculatePawnMoves(piece, position);
        } else if (symbol.equals(Pieces.KNIGHT.getSymbol())) {
            calculateKnightMoves(piece, position);
        } else if (symbol.equals(Pieces.BISHOP.getSymbol())) {
            calculateBishopMoves(piece, position);
        } else if (symbol.equals(Pieces.ROOK.getSymbol())) {
            calculateRookMoves(piece, position);
        } else if (symbol.equals(Pieces.QUEEN.getSymbol())) {
            calculateQueenMoves(piece, position);
        } else if (symbol.equals(Pieces.KING.getSymbol())) {
            calculateKingMoves(piece, position);
        }

    }
    public Piece getPiece(Position position){
        return board.get(position);
    }
    public void movePiece(Position from, Position to){
        board.put(to, board.get(from));
        board.remove(from);
    }
    public Position findKingPosition(int color) {
        for (Map.Entry<Position, Piece> entry : board.entrySet()) {
            Piece piece = entry.getValue();
            if (piece == null) continue;
            if (piece.getSymbol().equals(Pieces.KING.getSymbol()) && piece.getColor() == color) {
                return entry.getKey();
            }
        }
        return null; // Shouldn't happen unless the king is missing
    }
    public List<Position> getAllPositionOfColor(int color){
        List<Position> result = new ArrayList<>();
        for (Map.Entry<Position, Piece> entry : board.entrySet()){
            if (entry.getValue() != null && entry.getValue().getColor() == color){
                result.add(entry.getKey());
            }
        }
        return result;
    }

    private boolean isKingInCheck(int color){
        Position kingPosition = findKingPosition(color);
        if (kingPosition == null) {
            throw new IllegalStateException("King of color " + (color == Colors.WHITE ? "WHITE" : "BLACK") + " is missing!");
        }
        for(Position enemyPiecePosition : getAllPositionOfColor(color == Colors.WHITE ? Colors.BLACK : Colors.WHITE)){
            updatePossibleFor(enemyPiecePosition);
            Piece enemyPiece = board.get(enemyPiecePosition);
            if (enemyPiece != null && enemyPiece.canCapture(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    // taking into account the current color's turn we check if the opposing color is checkmated
    public int checkGameState(int currentTurn){
        int enemyColor = (currentTurn == Colors.WHITE) ? Colors.BLACK : Colors.WHITE;

        for(Position enemyPiecePosition : getAllPositionOfColor(enemyColor)){
            updateLegalFor(enemyPiecePosition);
            Piece enemyPiece = board.get(enemyPiecePosition);
            if (enemyPiece != null && (!enemyPiece.getLegalCaptures().isEmpty() || !enemyPiece.getLegalMoves().isEmpty())) {
                // if we find any legal move, game continues
                System.out.println("found_move" + enemyPiece.getLegalMoves() + " " + enemyPiece.getLegalCaptures());
                return GameStates.ONGOING;
            }
        }
        if(isKingInCheck(enemyColor)){
            // no legal moves + king in check = checkmate
            return GameStates.CHECKMATE;
        }
        // no legal moves + king NOT in check = checkmate
        return GameStates.STALEMATE;
    }

}
