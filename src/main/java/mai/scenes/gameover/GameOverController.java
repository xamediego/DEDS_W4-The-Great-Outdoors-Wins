package mai.scenes.gameover;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import mai.audio.ButtonAudio;
import mai.audio.Sound;
import mai.data.User;
import mai.enums.FXMLPart;
import mai.enums.GameOverType;
import mai.scenes.game.Parts.AvatarBox;
import mai.scenes.gameconfig.GameConfigController;
import mai.scenes.gameconfig.GameConfigScene;
import mai.scenes.abstractscene.AbstractController;
import mai.audio.SoundPlayer;
import mai.scenes.gamemenu.GameMenuController;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController extends AbstractController implements Initializable {

    private final GameOverData gameOverData;
    private final GameMenuController gameMenuController;

    @FXML
    private HBox avatarContainer;

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

    @FXML
    private Button returnButton;

    public GameOverController(GameOverData gameOverData, GameMenuController gameMenuController) {
        this.gameOverData = gameOverData;
        this.gameMenuController = gameMenuController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configButton();

        if(gameOverData.getGameOverType().equals(GameOverType.P1)){
            setWinner(gameOverData.getUser1(), String.valueOf(gameOverData.getUser1Score()), "Has won with a score of:");
        } else if(gameOverData.getGameOverType().equals(GameOverType.P2)){
            SoundPlayer.playAudioFile(Sound.LOST.getAudio());
            setWinner(gameOverData.getUser2(), String.valueOf(gameOverData.getUser2Score()), "Has won with a score of:");
        } else {
            SoundPlayer.playAudioFile(Sound.LOST.getAudio());

            User tempUser = new User();
            tempUser.setPlayerColour("WHITE");
            tempUser.setPlayerName("GOD WON");
            tempUser.setProfilePictureUrl("Images/App/Spurdo.png");
            setWinner(tempUser,"" ,"Draw, you both lost");
        }

    }

    private void setWinner(User user, String score, String winText){
        setAvatar(user);
        setLabels(score, user.getPlayerName(), winText);
    }

    private void setAvatar(User user) {
        avatarContainer.getChildren().add(new AvatarBox(120, new Image(user.getProfilePictureUrl()), user.getPlayerColour()));
    }

    private void setLabels(String score, String username, String winText){
        scoreLabel.setText(String.valueOf(score));
        playerLabel.setText(username);
        winLabel.setText(winText);
    }

    private void configButton(){
        returnButton.setOnMouseEntered(select());
    }

    private EventHandler<? super MouseEvent> select(){
        return (EventHandler<MouseEvent>) event -> SoundPlayer.playAudioFile(ButtonAudio.SELECT.getAudio());
    }

    @FXML
    private void returnToMain(){
        SoundPlayer.playAudioFile(ButtonAudio.OK.getAudio());
        this.gameMenuController.setGameConfigScreen();
    }

}
