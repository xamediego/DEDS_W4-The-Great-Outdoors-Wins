package mai.data;

public class User {

    private String playerName;
    private String playerColour;
    private String profilePictureUrl;

    private int playerNumber;

    public User() {

    }

    public User(String playerName, String playerColour, String profilePictureUrl) {
        this.playerName = playerName;
        this.playerColour = playerColour;
        this.profilePictureUrl = profilePictureUrl;
    }

    public User(int playerNumber, String playerName, String playerColour, String profilePictureUrl) {
        this(playerName, playerColour, profilePictureUrl);
        this.playerNumber = playerNumber;
    }

    public User(String playerName, String playerColour, String profilePictureUrl, int playerNumber) {
        this.playerName = playerName;
        this.playerColour = playerColour;
        this.profilePictureUrl = profilePictureUrl;
        this.playerNumber = playerNumber;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerColour() {
        return playerColour;
    }

    public void setPlayerColour(String playerColour) {
        this.playerColour = playerColour;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
