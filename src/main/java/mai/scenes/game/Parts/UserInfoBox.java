package mai.scenes.game.Parts;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import mai.data.User;

public class UserInfoBox extends VBox {

    private AvatarBox avatarBox;
    private final Label playerLabel;
    private final Label scoreLabel;
    private final Label teamLabel;

    public UserInfoBox(User user, int score, String team) {
        playerLabel = new Label(user.getPlayerName());
        scoreLabel = new Label(String.valueOf(score));
        teamLabel = new Label(team);

        this.setSpacing(5);
        this.setFillWidth(true);
        this.setAlignment(Pos.CENTER);

        configBox();
    }

    public UserInfoBox(User user, int score, String team ,int size) {
        playerLabel = new Label(user.getPlayerName());
        scoreLabel = new Label(String.valueOf(score));
        teamLabel = new Label(team);

        this.setSpacing(5);
        this.setFillWidth(true);
        this.setAlignment(Pos.CENTER);

        if(user.getProfilePictureUrl() != null){
            avatarBox = new AvatarBox(size, new Image(user.getProfilePictureUrl() ), user.getPlayerColour());
        } else {
            avatarBox = new AvatarBox(size, new Image("/images/app/defaultProfImage.png"), user.getPlayerColour());
        }

        configBox();
    }

    private void configBox(){
        if(avatarBox != null){
            this.getChildren().add(avatarBox);
        }

        this.getChildren().add(playerLabel);
        this.getChildren().add(scoreLabel);
        this.getChildren().add(teamLabel);
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
