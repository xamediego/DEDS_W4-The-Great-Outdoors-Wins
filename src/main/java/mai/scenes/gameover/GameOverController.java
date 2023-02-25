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
import mai.JFXApplication;
import mai.audio.AudioPlayer;
import mai.audio.ButtonAudio;
import mai.audio.Sound;
import mai.data.User;
import mai.enums.FXMLPart;
import mai.enums.GameOverType;
import mai.parts.AvatarBox;
import mai.scenes.gameconfig.GameConfigController;
import mai.scenes.gameconfig.GameConfigScene;
import mai.scenes.s_abstr.AbstractController;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController extends AbstractController implements Initializable {

    private final User player1 , player2;
    private final GameOverType gameOverType;

    private final int player1Score, player2Score;

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

    public GameOverController(User player1, User player2, GameOverType gameOverType, int player1Score, int player2Score) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.gameOverType = gameOverType;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configButton();

        if(gameOverType.equals(GameOverType.P1)){
            setWinner(player1, String.valueOf(player1Score), "Has won with a score of:");
        } else if(gameOverType.equals(GameOverType.P2)){
            AudioPlayer.playAudioFile(Sound.LOST.getAudio());
            setWinner(player2, String.valueOf(player2Score), "Has won with a score of:");
        } else {
            AudioPlayer.playAudioFile(Sound.LOST.getAudio());

            User tempUser = new User();
            tempUser.setPlayerColour("WHITE");
            tempUser.setPlayerName("GOD WON");
            tempUser.setProfilePictureUrl("Images/App/Thierry.png");
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
        return (EventHandler<MouseEvent>) event -> AudioPlayer.playAudioFile(ButtonAudio.SELECT.getAudio());
    }

    @FXML
    private void returnToMain(){
        AudioPlayer.playAudioFile(ButtonAudio.OK.getAudio());
        JFXApplication.gameMenuController.setContent(new GameConfigScene(new GameConfigController(), FXMLPart.GAMECONFIG).getRoot());
    }

}
