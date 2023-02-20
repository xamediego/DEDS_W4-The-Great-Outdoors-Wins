package mai.data;

public class User {

    private String playerName;
    private String playerColour;
    private String profilePictureUrl;

    public User() {

    }

    public User(String playerName, String playerColour, String profilePictureUrl) {
        this.playerName = playerName;
        this.playerColour = playerColour;
        this.profilePictureUrl = profilePictureUrl;
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
}
