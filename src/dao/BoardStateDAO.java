package dao;

import data.BoardStateData;
import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class BoardStateDAO {
    
    public static void save(BoardStateData entity) {
        String sql = "INSERT INTO board_states (game_id, current_turn, last_updated, board_layout) VALUES (?, ?, ?, ?)";
        // board_id is assigned automatically

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, entity.getGameId());
            stmt.setString(2, entity.getCurrentTurn());
            stmt.setTimestamp(3, entity.getLastUpdated());
            stmt.setString(4, entity.getBoardLayout());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static BoardStateData findById(int id) {
        String sql = "SELECT * FROM board_states WHERE board_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new BoardStateData(
                        rs.getInt("board_id"),
                        rs.getInt("game_id"),
                        rs.getString("current_turn"),
                        rs.getTimestamp("last_updated"),
                        rs.getString("board_layout")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BoardStateData findLatestByGameId(int gameId) {
        String sql = "SELECT * FROM board_states WHERE game_id = ? ORDER BY last_updated DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new BoardStateData(
                        rs.getInt("board_id"),
                        rs.getInt("game_id"),
                        rs.getString("current_turn"),
                        rs.getTimestamp("last_updated"),
                        rs.getString("board_layout")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    
    public static List<BoardStateData> findAll() {
        String sql = "SELECT * FROM board_states";
        List<BoardStateData> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new BoardStateData(
                        rs.getInt("board_id"),
                        rs.getInt("game_id"),
                        rs.getString("current_turn"),
                        rs.getTimestamp("last_updated"),
                        rs.getString("board_layout")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    
    public static void update(BoardStateData entity) {
        String sql = "UPDATE board_states SET current_turn = ?, last_updated = ?, board_layout = ? WHERE board_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entity.getCurrentTurn());
            stmt.setTimestamp(2, entity.getLastUpdated());
            stmt.setString(3, entity.getBoardLayout());
            stmt.setInt(4, entity.getBoardId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static void delete(int gameId) {
        String sql = "DELETE FROM board_states WHERE game_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
