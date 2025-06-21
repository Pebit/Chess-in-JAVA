package dao;

import data.GameData;
import db.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    
    public static void save(GameData game) {
        String sql = "INSERT INTO games (white_player_id, black_player_id, start_time, end_time, result, final_board_state, current_turn) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, game.getWhitePlayerId());
            stmt.setInt(2, game.getBlackPlayerId());
            stmt.setTimestamp(3, Timestamp.valueOf(game.getStartTime()));
            stmt.setTimestamp(4, game.getEndTime() != null ? Timestamp.valueOf(game.getEndTime()) : null);
            stmt.setString(5, game.getResult());
            stmt.setString(6, game.getFinalBoardState());
            stmt.setString(7, game.getCurrentTurn());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    game.setGameId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static GameData findById(int id) {
        String sql = "SELECT * FROM games WHERE game_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new GameData(
                            rs.getInt("game_id"),
                            rs.getInt("white_player_id"),
                            rs.getInt("black_player_id"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time") != null ? rs.getTimestamp("end_time").toLocalDateTime() : null,
                            rs.getString("result"),
                            rs.getString("final_board_state"),
                            rs.getString("current_turn")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GameData findLatestByPlayersId(int player1_id, int player2_id) {
        String sql = "SELECT * FROM games WHERE end_time IS NULL AND " +
                "((white_player_id = ? AND black_player_id = ?) OR (white_player_id = ? AND black_player_id = ?)) " +
                "ORDER BY start_time DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, player1_id);
            stmt.setInt(2, player2_id);
            stmt.setInt(3, player2_id);
            stmt.setInt(4, player1_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new GameData(
                        rs.getInt("game_id"),
                        rs.getInt("white_player_id"),
                        rs.getInt("black_player_id"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time") != null ? rs.getTimestamp("end_time").toLocalDateTime() : null,
                        rs.getString("result"),
                        rs.getString("final_board_state"),
                        rs.getString("current_turn")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    
    public static List<GameData> findAll() {
        List<GameData> games = new ArrayList<>();
        String sql = "SELECT * FROM games";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                games.add(new GameData(
                        rs.getInt("game_id"),
                        rs.getInt("white_player_id"),
                        rs.getInt("black_player_id"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time") != null ? rs.getTimestamp("end_time").toLocalDateTime() : null,
                        rs.getString("result"),
                        rs.getString("final_board_state"),
                        rs.getString("current_turn")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    
    public static void update(GameData game) {
        String sql = "UPDATE games SET white_player_id = ?, black_player_id = ?, start_time = ?, end_time = ?, result = ?, final_board_state = ?, current_turn = ? WHERE game_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, game.getWhitePlayerId());
            stmt.setInt(2, game.getBlackPlayerId());
            stmt.setTimestamp(3, Timestamp.valueOf(game.getStartTime()));
            stmt.setTimestamp(4, game.getEndTime() != null ? Timestamp.valueOf(game.getEndTime()) : null);
            stmt.setString(5, game.getResult());
            stmt.setString(6, game.getFinalBoardState());
            stmt.setString(7, game.getCurrentTurn());
            stmt.setInt(8, game.getGameId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static void delete(int id) {
        String sql = "DELETE FROM games WHERE game_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}