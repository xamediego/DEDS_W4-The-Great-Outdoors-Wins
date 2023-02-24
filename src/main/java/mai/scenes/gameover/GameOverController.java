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
import mai.data.Speler;
import mai.enums.FXMLPart;
import mai.enums.GameOverType;
import mai.scenes.gameconfig.GameConfigController;
import mai.scenes.gameconfig.GameConfigScene;
import mai.scenes.sceneconstructor.AbstractController;
import mai.audio.AudioPlayer;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController extends AbstractController implements Initializable {

    private final Speler player1 , player2;
    private final GameOverType gameOverType;

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

    public GameOverController(Speler player1, Speler player2, GameOverType gameOverType, int player1Score, int player2Score) {
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
            AudioPlayer.playAudioFile(MenuAudio.LOST);
            setWinner(player2, String.valueOf(player2Score), "Has won with a score of:");
        } else {
            AudioPlayer.playAudioFile(MenuAudio.LOST);
            Speler tempSpeler = new Speler();
            tempSpeler.setSpelerKleur("WHITE");
            tempSpeler.setNaam("GOD WON");
            tempSpeler.setProfilePictureUrl("Images/App/Thierry.png");
            setWinner(tempSpeler,"" ,"Draw, you both lost");
        }
    }

    private void setWinner(Speler speler, String score, String winText){
        setAvatar(new Image(speler.getProfilePictureUrl()), speler.getSpelerKleur());
        setLabels(score, speler.getNaam(), winText);
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
        JFXApplication.gameMenuController.setContent(new GameConfigScene(new GameConfigController(), FXMLPart.GAMECONFIG).getRoot());
    }

}
