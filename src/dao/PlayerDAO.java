package dao;

import data.PlayerData;
import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {

    public static void save(PlayerData player) {
        String sql = "INSERT INTO players (name, password, wins, losses, draws) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getName());
            stmt.setString(2, player.getPassword());
            stmt.setInt(3, player.getWins());
            stmt.setInt(4, player.getLosses());
            stmt.setInt(5, player.getDraws());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PlayerData findById(int id) {
        String sql = "SELECT * FROM players WHERE player_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PlayerData(
                        rs.getInt("player_id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getInt("draws")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static PlayerData findByName(String name) {
        String sql = "SELECT * FROM players WHERE name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PlayerData(
                        rs.getInt("player_id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getInt("draws")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<PlayerData> findAll() {
        String sql = "SELECT * FROM players";
        List<PlayerData> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new PlayerData(
                        rs.getInt("player_id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("wins"),
                        rs.getInt("losses"),
                        rs.getInt("draws")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void update(PlayerData player) {
        String sql = "UPDATE players SET name = ?, password = ?, wins = ?, losses = ?, draws = ? WHERE player_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, player.getName());
            stmt.setString(2, player.getPassword());
            stmt.setInt(3, player.getWins());
            stmt.setInt(4, player.getLosses());
            stmt.setInt(5, player.getDraws());
            stmt.setInt(6, player.getPlayerId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void incrementWins(int playerId) {
        String sql = "UPDATE players SET wins = wins + 1 WHERE player_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void incrementLosses(int playerId) {
        String sql = "UPDATE players SET losses = losses + 1 WHERE player_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void incrementDraws(int playerId) {
        String sql = "UPDATE players SET draws = draws + 1 WHERE player_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        String sql = "DELETE FROM players WHERE player_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
