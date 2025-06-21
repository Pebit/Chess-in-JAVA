package data;

public class PlayerData {
    private int playerId;
    private String name;
    private String password;
    private int wins;
    private int losses;
    private int draws;

    public PlayerData(int playerId, String name, String password, int wins, int losses, int draws) {
        this.playerId = playerId;
        this.name = name;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    public PlayerData(String name, String password) {
        this(0, name, password, 0, 0, 0); // default wins and losses to 0
    }

    // Getters and Setters
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    public int getDraws(){ return draws; }
    public void setDraws(int draws){ this.draws = draws; }
}