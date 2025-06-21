package chess.game;


import audit_service.AuditService;
import chess.board.*;
import constants.Colors;
import chess.player.Player;
import chess.pieces.Piece;
import constants.GameStates;
import dao.*;
import data.BoardStateData;
import data.GameData;
import data.MoveData;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class Game {

    private int gameId;
    private final Board board;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private int currentTurn;
    private boolean gameOver;

    public Game(Player whitePlayer, Player blackPlayer){
        GameData gameData = new GameData(whitePlayer.getPlayerId(), blackPlayer.getPlayerId(), LocalDateTime.now());
        GameDAO.save(gameData);
        gameData = GameDAO.findLatestByPlayersId(whitePlayer.getPlayerId(), blackPlayer.getPlayerId());
        assert gameData != null;
        this.gameId = gameData.getGameId();
        this.board = new Board();
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.currentTurn = Colors.WHITE;
        this.gameOver = false;
    }
    public Game(int gameId, Board board, Player whitePlayer, Player blackPlayer, int currentTurn){

        this.gameId = gameId;
        this.board = board;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.currentTurn = currentTurn;
        this.gameOver = false;
    }
    public void displayTurn(){
        System.out.println(currentTurn == Colors.WHITE ? PlayerDAO.findById(whitePlayer.getPlayerId()).getName() + "'s turn" : PlayerDAO.findById(blackPlayer.getPlayerId()).getName() + "'s turn");
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

        // after confirming the move we log it:
        int gameId = this.gameId;
        int moveNumber = MoveDAO.findLastMoveNumberByGameId(gameId) + 1;
        String fromPiece = getColoredPieceSymbol(movingPiece);
        String toPiece = getColoredPieceSymbol(captured);
        String fromPos = from.toString();
        String toPos = to.toString();
        MoveData moveData = new MoveData(gameId, moveNumber, fromPiece, toPiece, fromPos, toPos);
        MoveDAO.save(moveData);
        AuditService.getInstance().logAction("move");
        // moving the piece
        board.movePiece(from, to);

        // checking for checkmate
        int gameState = board.checkGameState(currentTurn);
        if(gameState == GameStates.CHECKMATE){
            GameData gameData = GameDAO.findById(this.gameId);
            assert gameData != null;
            gameData.setEndTime(LocalDateTime.now());
            gameData.setResult(currentTurn == Colors.WHITE ? "WHITE" : "BLACK");
            gameData.setFinalBoardState(this.board.toString());
            GameDAO.update(gameData);

            if (currentTurn == Colors.WHITE){
                PlayerDAO.incrementLosses(blackPlayer.getPlayerId());
                PlayerDAO.incrementWins(whitePlayer.getPlayerId());
            } else {
                PlayerDAO.incrementLosses(whitePlayer.getPlayerId());
                PlayerDAO.incrementWins(blackPlayer.getPlayerId());
            }

            BoardStateDAO.delete(this.gameId); // delete all redundant game saves once the game is over
            AuditService.getInstance().logAction("checkmate");

            gameOver = true;
            System.out.println("Checkmate! " + (currentTurn == Colors.WHITE ? "White": "Black") + " wins.");
            return false;
        } // checking for stalemate
        else if (gameState == GameStates.STALEMATE) {
            GameData gameData = GameDAO.findById(this.gameId);
            assert gameData != null;
            gameData.setEndTime(LocalDateTime.now());
            gameData.setResult("STALEMATE");
            gameData.setFinalBoardState(this.board.toString());
            GameDAO.update(gameData);

            PlayerDAO.incrementDraws(blackPlayer.getPlayerId());
            PlayerDAO.incrementDraws(whitePlayer.getPlayerId());

            BoardStateDAO.delete(this.gameId);

            gameOver = true;
            AuditService.getInstance().logAction("stalemate");
            System.out.println("Stalemate! It's a draw.");
            return false;
        } // otherwise we switch turns
        else {
            switchTurn();
        }
        return true;
    }

    public void resign(){
        GameData gameData = GameDAO.findById(this.gameId);
        assert gameData != null;
        gameData.setEndTime(LocalDateTime.now());
        gameData.setResult(currentTurn == Colors.WHITE ? "BLACK" : "WHITE");
        gameData.setFinalBoardState(this.board.toString());
        GameDAO.update(gameData);

        if (currentTurn == Colors.WHITE){
            PlayerDAO.incrementLosses(whitePlayer.getPlayerId());
            PlayerDAO.incrementWins(blackPlayer.getPlayerId());
        } else {
            PlayerDAO.incrementLosses(blackPlayer.getPlayerId());
            PlayerDAO.incrementWins(whitePlayer.getPlayerId());
        }

        BoardStateDAO.delete(this.gameId);

        AuditService.getInstance().logAction("resign");
        gameOver = true;
        System.out.println((currentTurn == Colors.WHITE ? "Black" : "White") + " wins by resignation.");
    }

    public void switchTurn(){
        this.currentTurn = (currentTurn == Colors.WHITE) ? Colors.BLACK : Colors.WHITE;
    }

    private Player getCurrentPlayer(){
        return currentTurn == Colors.WHITE ? whitePlayer : blackPlayer;
    }
    public int getGameId(){ return this.gameId; }
    private String getColoredPieceSymbol(Piece piece){
        if (piece == null)
            return null;
        int color = piece.getColor();
        return color == Colors.WHITE ? piece.getSymbol() : piece.getSymbol().toLowerCase();
    }
    public void saveBoardState(){
        BoardStateData boardStateData = new BoardStateData(this.gameId, this.currentTurn == Colors.WHITE ? "WHITE" : "BLACK", Timestamp.valueOf(LocalDateTime.now()), board.toString());
        BoardStateData oldBoardStateData = BoardStateDAO.findLatestByGameId(this.gameId);
        if (oldBoardStateData != null) {
            boardStateData.setBoardId(oldBoardStateData.getBoardId());
            BoardStateDAO.update(boardStateData);
            return;
        }
        BoardStateDAO.save(boardStateData);
    }
}
