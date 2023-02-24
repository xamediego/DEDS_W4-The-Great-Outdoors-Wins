package mai.scenes.game.Parts;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class UserInfoBox extends VBox {

    private AvatarBox avatarBox;
    private final Label playerLabel;
    private final Label scoreLabel;
    private final Label teamLabel;

    public UserInfoBox(String playerName, String score, String team) {
        playerLabel = new Label(playerName);
        scoreLabel = new Label(score);
        teamLabel = new Label(team);

        this.setSpacing(5);
        this.setFillWidth(true);
    }

    public UserInfoBox(String playerName, String score, String team, Image image, String color ,int size) {
        this(playerName, score, team);
        avatarBox = new AvatarBox(size, image, color);
    }

    public AvatarBox getAvatarBox() {
        return avatarBox;
    }

    public Label getPlayerLabel() {
        return playerLabel;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public Label getTeamLabel() {
        return teamLabel;
    }

}
