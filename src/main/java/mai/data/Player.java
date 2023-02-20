package mai.data;

public class Player extends User {
    private int playerNumber;
    private String playerName;
    private String playerColour;
    private String profilePictureUrl;

    public Player() {

    }

    public Player(int playerNumber, String playerName, String playerColour, String profilePictureUrl) {
        super(playerName, playerColour, profilePictureUrl);
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
