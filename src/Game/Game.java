package Game;

import Board.*;
import Constants.Colors;
import Player.Player;
import pieces.Piece;
import Constants.GameStates;

public class Game {



    private final Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private int currentTurn;
    private boolean gameOver;

    public Game(){
        this.board = new Board();
        this.whitePlayer = new Player(Colors.WHITE);
        this.blackPlayer = new Player(Colors.BLACK);
        this.currentTurn = Colors.WHITE;
        this.gameOver = false;
    }
    public void displayTurn(){
        System.out.println(currentTurn == Colors.WHITE ? "WHITE's turn" : "BLACK's turn");
    }
    public void displayBoard(){
        this.board.showBoard();
    }
    public boolean makeMove(Position from, Position to){
        if (gameOver) return false;

        Piece movingPiece = board.getPiece(from);
        // if we're trying to move an empty square or an opponent's piece nothing happens
        if (movingPiece == null || movingPiece.getColor() != currentTurn) return true;

        board.updateLegalFor(from);
        // if the move is illegal nothing happens
        if (!movingPiece.canMove(to) && !movingPiece.canCapture(to)) return true;

        Piece captured = board.getPiece(to);
        // if we are capturing a piece this move we add it to the current player's captures list
        if (captured != null){
            getCurrentPlayer().addCapturedPiece(captured);
        }
        // moving the piece
        board.movePiece(from, to);

        // checking for checkmate
        int gameState = board.checkGameState(currentTurn);
        if(gameState == GameStates.CHECKMATE){
            gameOver = true;
            System.out.println("Checkmate! " + (currentTurn == Colors.WHITE ? "White": "Black") + " wins.");
            return false;
        } // checking for stalemate
        else if (gameState == GameStates.STALEMATE) {
            gameOver = true;
            System.out.println("Stalemate! It's a draw.");
            return false;
        } // otherwise we switch turns
        else {
            switchTurn();
        }
        return true;
    }

    public void resign(){
        gameOver = true;
        System.out.println((currentTurn == Colors.WHITE ? "Black" : "White") + " wins by resignation.");
    }

    private Player getCurrentPlayer(){
        return currentTurn == Colors.WHITE ? whitePlayer : blackPlayer;
    }

    private void switchTurn(){
        this.currentTurn = (currentTurn == Colors.WHITE) ? Colors.BLACK : Colors.WHITE;
    }
}
