package mai.scenes.gameover;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import mai.JFXApplication;
import mai.data.Player;
import mai.enums.MatchOverType;
import mai.scenes.gameconfig.GameConfigScene;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {

    private final Player player1;
    private final Player player2;
    private final MatchOverType matchOverType;

    private final int player1Score, player2Score;

    @FXML
    private Circle playerAvatarCircle;

    @FXML
    private ImageView avatarView;

    @FXML
    private Label playerLabel;

    @FXML
    private Label winLabel;

    @FXML
    private Label scoreLabel;

    public GameOverController(Player player1, Player player2, MatchOverType matchOverType, int player1Score, int player2Score) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.matchOverType = matchOverType;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(matchOverType.equals(MatchOverType.P1)){
            setWinner(player1, String.valueOf(player1Score), "Has won with a score of:");
        } else if(matchOverType.equals(MatchOverType.P2)){
            setWinner(player2, String.valueOf(player2Score), "Has won with a score of:");
        } else {
            Player tempUser = new Player();
            tempUser.setPlayerColour("WHITE");
            tempUser.setPlayerName("GOD WON");
            tempUser.setProfilePictureUrl("https://i.warosu.org/data/jp/img/0101/78/1354881517355.png");
            setWinner(tempUser,"" ,"Draw, you both lost");
        }
    }

    private void setWinner(Player user, String score, String winText){
        setAvatar(new Image(user.getProfilePictureUrl()), user.getPlayerColour());
        setLabels(score, user.getPlayerName(), winText);
    }

    private void setAvatar(Image image, String colour) {
        avatarView.setImage(image);

        Circle circle = new Circle(avatarView.getBaselineOffset() / 2);
        circle.setLayoutX(avatarView.getFitWidth() / 2);
        circle.setLayoutY(avatarView.getFitHeight() / 2);

        avatarView.setClip(circle);

        playerAvatarCircle.setStyle("-fx-stroke: " + colour);
    }

    private void setLabels(String score, String username, String winText){
        scoreLabel.setText(String.valueOf(score));
        playerLabel.setText(username);
        winLabel.setText(winText);
    }

    @FXML
    private void returnToMain(){
        JFXApplication.gameMenuController.setContent(new GameConfigScene(null).getRoot());
    }

}
