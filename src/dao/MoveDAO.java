package dao;

import data.MoveData;
import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoveDAO {
    
    public static void save(MoveData move) {
        String sql = "INSERT INTO moves (game_id, move_number, moved_piece, captured_piece, from_position, to_position) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, move.getGameId());
            stmt.setInt(2, move.getMoveNumber());
            stmt.setString(3, move.getMovedPiece());
            stmt.setString(4, move.getCapturedPiece());
            stmt.setString(5, move.getFromPosition());
            stmt.setString(6, move.getToPosition());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    move.setMoveId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static MoveData findById(int id) {
        String sql = "SELECT * FROM moves WHERE move_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new MoveData(
                            rs.getInt("move_id"),
                            rs.getInt("game_id"),
                            rs.getInt("move_number"),
                            rs.getString("moved_piece"),
                            rs.getString("captured_piece"),
                            rs.getString("from_position"),
                            rs.getString("to_position")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<MoveData> findByGameId(int gameId) {
        String sql = "SELECT * FROM moves WHERE game_id = ? ORDER BY move_number ASC";
        List<MoveData> moves = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    moves.add(new MoveData(
                            rs.getInt("move_id"),
                            rs.getInt("game_id"),
                            rs.getInt("move_number"),
                            rs.getString("moved_piece"),
                            rs.getString("captured_piece"),
                            rs.getString("from_position"),
                            rs.getString("to_position"))
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return moves;
    }

    public static int findLastMoveNumberByGameId(int gameId) {
        String sql = "SELECT * FROM moves WHERE game_id = ? ORDER BY move_number DESC LIMIT 1";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("move_number");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    
    public static List<MoveData> findAll() {
        List<MoveData> list = new ArrayList<>();
        String sql = "SELECT * FROM moves";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new MoveData(
                        rs.getInt("move_id"),
                        rs.getInt("game_id"),
                        rs.getInt("move_number"),
                        rs.getString("moved_piece"),
                        rs.getString("captured_piece"),
                        rs.getString("from_position"),
                        rs.getString("to_position"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    
    public static void update(MoveData move) {
        String sql = "UPDATE moves SET game_id = ?, move_number = ?, moved_piece = ?, captured_piece = ?, from_position = ?, to_position = ? WHERE move_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, move.getGameId());
            stmt.setInt(2, move.getMoveNumber());
            stmt.setString(3, move.getMovedPiece());
            stmt.setString(4, move.getCapturedPiece());
            stmt.setString(5, move.getFromPosition());
            stmt.setString(6, move.getToPosition());
            stmt.setInt(7, move.getMoveId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static void delete(int id) {
        String sql = "DELETE FROM moves WHERE move_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
