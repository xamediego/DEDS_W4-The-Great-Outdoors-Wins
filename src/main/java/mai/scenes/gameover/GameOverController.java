package mai.scenes.gameover;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import mai.JFXApplication;
import mai.audio.MenuAudio;
import mai.data.User;
import mai.enums.FXMLPart;
import mai.enums.MatchOverType;
import mai.enums.Sound;
import mai.scenes.gameconfig.GameConfigController;
import mai.scenes.gameconfig.GameConfigScene;
import mai.scenes.test.AbstractController;
import mai.service.AudioPlayer;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameOverController extends AbstractController implements Initializable {

    private final User player1 , player2;
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

    @FXML
    private Button returnButton;

    public GameOverController(User player1, User player2, MatchOverType matchOverType, int player1Score, int player2Score) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.matchOverType = matchOverType;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configButton();
        if(matchOverType.equals(MatchOverType.P1)){
//            AudioPlayer.playAudioFile(new File(Sound.WIN.getAudio()));
            setWinner(player1, String.valueOf(player1Score), "Has won with a score of:");
        } else if(matchOverType.equals(MatchOverType.P2)){
            AudioPlayer.playAudioFile(new File(Sound.LOST.getAudio()));
            setWinner(player2, String.valueOf(player2Score), "Has won with a score of:");
        } else {
            AudioPlayer.playAudioFile(new File(Sound.LOST.getAudio()));
            User tempUser = new User();
            tempUser.setPlayerColour("WHITE");
            tempUser.setPlayerName("GOD WON");
            tempUser.setProfilePictureUrl("Images/App/Thierry.png");
            setWinner(tempUser,"" ,"Draw, you both lost");
        }
    }

    private void setWinner(User user, String score, String winText){
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

    private void configButton(){
        returnButton.setOnMouseEntered(select());
    }

    private EventHandler<? super MouseEvent> select(){
        return (EventHandler<MouseEvent>) event -> AudioPlayer.playAudioFile(MenuAudio.SELECT_AUDIO);
    }

    @FXML
    private void returnToMain(){
        AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);
        JFXApplication.gameMenuController.setContent(new GameConfigScene(new GameConfigController(Optional.empty()), FXMLPart.GAMECONFIG).getRoot());
    }

}
