import Board.Board;
import Game.Game;
import pieces.Piece;
import Board.Position;
import java.util.*;
public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        Position from = null;
        Position to = null;

        System.out.println("Chess Game Started!");
        do {
            game.displayTurn();
            game.displayBoard();
            System.out.print("Enter your move (e.g., e2 e4) or 'resign': ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("resign")) {
                game.resign();
                break;
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
        } while(game.makeMove(from, to));

        scanner.close();
    }
}