import audit_service.AuditService;
import chess.board.Board;
import chess.game.Game;
import chess.board.Position;

import chess.player.Player;
import constants.Colors;
import dao.BoardStateDAO;
import dao.GameDAO;
import dao.MoveDAO;
import dao.PlayerDAO;
import data.BoardStateData;
import data.GameData;
import data.MoveData;
import data.PlayerData;
import db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.util.*;
public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static Player login() {
        while (true) {
            System.out.println("➽ please \"/login\" or \"/create_player\"");
            String command = scanner.nextLine().trim();

            if (command.equals("/login")) {
                while (true) {
                    System.out.println("➽ please enter name (or type \"/back\"):");
                    String name = scanner.nextLine().trim();
                    if (name.equals("/back")) break;

                    PlayerData playerData = PlayerDAO.findByName(name);
                    if (playerData == null) {
                        System.out.println("(player name not found)");
                        continue;
                    }

                    int tries = 5;
                    while (tries > 0) {
                        System.out.println("➽ please enter password for " + name + ":");
                        String password = scanner.nextLine().trim();
                        if (playerData.getPassword().equals(password)) {
                            AuditService.getInstance().logAction("login");
                            return new Player(playerData.getPlayerId());
                        } else {
                            tries--;
                            if (tries == 0) {
                                System.out.println("(too many failed attempts, going back)");
                                AuditService.getInstance().logAction("login_fail");
                                break;
                            }
                            System.out.println("(" + tries + " tries left)");
                        }
                    }
                    break; // after failed password attempts
                }

            } else if (command.equals("/create_player")) {
                while (true) {
                    System.out.println("➽ enter new player name (or type \"/back\"):");
                    String name = scanner.nextLine().trim();
                    if (name.equals("/back")) break;

                    PlayerData playerData = PlayerDAO.findByName(name);
                    if (playerData != null) {
                        System.out.println("(name already taken, try another)");
                        continue;
                    }

                    System.out.println("➽ enter password:");
                    String password = scanner.nextLine().trim();

                    PlayerData newData = new PlayerData(name, password);
                    PlayerDAO.save(newData);
                    AuditService.getInstance().logAction("create_player");
                    // retrieve playerId from DB
                    PlayerData saved = PlayerDAO.findByName(name);
                    if (saved == null) {
                        System.out.println("(error retrieving new player, please try again)");
                        continue;
                    }

                    return new Player(saved.getPlayerId());
                }
            }
        }
    }
    public static Game loadFromData(int gameId, Player whitePlayer, Player blackPlayer){
        BoardStateData boardStateData = BoardStateDAO.findLatestByGameId(gameId);
        assert boardStateData != null;
        return new Game(gameId, Board.fromString(boardStateData.getBoardLayout()), whitePlayer, blackPlayer,
                boardStateData.getCurrentTurn().equals("WHITE") ? Colors.WHITE : Colors.BLACK);
    }

    public static String playGame(Game game){

        Position to = null;
        Position from = null;

        System.out.println("<< Chess Game Started >>");
        do {
            game.displayTurn();
            game.displayBoard();
            System.out.print("➽ enter move: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("resign")) {
                game.resign();
                break;
            }
            if (input.equalsIgnoreCase("/quit")) { // save and quit the game
                game.saveBoardState();
                AuditService.getInstance().logAction("save_and_quit_game");
                return "/quit";
            }

            String[] tokens = input.split(" ");
            if (tokens.length != 2) {
                System.out.println("Invalid input format. Please enter moves like 'e2 e4'.");
                continue;
            }
            from = Position.fromString(tokens[0]);
            to = Position.fromString(tokens[1]);

            if (from == null || to == null) {
                System.out.println("Invalid positions. Please enter valid board coordinates (e.g., 'e2').");
                continue;
            }
        } while (game.makeMove(from, to));
        return "/exit_game";
    }

    public static void main(String[] args) {
        AuditService.getInstance().logAction("some_event");
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println(" DB connection successful!");
        } catch (SQLException e) {
            System.err.println(" DB Connection failed: " + e.getMessage());
        }

        String playChoice = "";
        do {
            Player player1 = login();
            Player player2 = login();

            GameData previousGame = GameDAO.findLatestByPlayersId(player1.getPlayerId(), player2.getPlayerId());

            if (previousGame != null) {
                while(true) {
                    System.out.println("➽ input \"/new_game\" or \"/continue\"");
                    String command = scanner.nextLine().trim();
                    if (command.equals("/new_game")){
                        AuditService.getInstance().logAction("new_game");
                        Game game = new Game(player1, player2);
                        playChoice = playGame(game);
                     } else if (command.equals("/continue")) {
                        Game game;
                        if (previousGame.getWhitePlayerId() == player1.getPlayerId()){
                            game = loadFromData(previousGame.getGameId(), player1, player2);
                        } else {
                            game = loadFromData(previousGame.getGameId(), player2, player1);
                        }
                        playChoice = playGame(game);
                        break;
                    } else if (command.equals("/back")){
                        System.out.println("(backing out to player login)");
                        break;
                    }
                }
            } else {
                while(true) {
                    System.out.println("(no previous games found)\n➽ input \"/new_game\" to play");
                    String command = scanner.nextLine().trim();
                    if (command.equals("/new_game")) {
                        AuditService.getInstance().logAction("new_game");
                        Game game = new Game(player1, player2);
                        playChoice = playGame(game);
                        break;
                    } else if (command.equals("/back")){
                        System.out.println("(backing out to player login)");
                        break;
                    }

                }
            }
        } while (!playChoice.equals("/quit"));
        scanner.close();
    }

}