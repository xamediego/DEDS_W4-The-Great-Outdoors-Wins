package mai.scenes.gameconfig;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import mai.JFXApplication;
import mai.audio.MenuAudio;
import mai.bootstrap.BootstrapData;
import mai.data.User;
import mai.enums.Difficulty;
import mai.enums.FXMLPart;
import mai.scenes.game.aigame.AIGameController;
import mai.scenes.game.aigame.AIGameScene;
import mai.scenes.game.logic.GameBoard;
import mai.scenes.game.logic.GameData;
import mai.scenes.game.logic.Space;
import mai.scenes.test.AbstractController;
import mai.service.AIService;
import mai.audio.AudioPlayer;
import mai.service.UserService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameConfigController extends AbstractController implements Initializable {

    @FXML
    public Label gameInfo;

    @FXML
    private ChoiceBox<Difficulty> aITypes;

    @FXML
    private Button startButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureChoiceBox();
        configStartButton();

        if (UserService.user != null) {
            gameInfo.setText("Game history will be recorded after the match");
        } else {
            gameInfo.setText("No game history will be recorded after the match\nPlease login if you want your game history to be recorded");
        }
    }

    @FXML
    private void configureChoiceBox() {
        List<Difficulty> DIFFICULTIES = List.of(Difficulty.values());

        aITypes.getItems().addAll(DIFFICULTIES);
        aITypes.getSelectionModel().selectFirst();

        aITypes.setOnMouseEntered(select());
        aITypes.setOnMouseClicked(move());
    }

    @FXML
    private void startGame() {
        AudioPlayer.playAudioFile(MenuAudio.START_AUDIO);

        GameBoard gameBoard = new GameBoard(7, 7, new Space[7][7]);
        gameBoard.configBoard();

        if (UserService.user != null) {
            GameData gameData = new GameData(4, 4, 1,  UserService.user, AIService.getAiPlayer(aITypes.getValue(), 2), gameBoard);
            AIGameController aiGameController = new AIGameController(gameData, 75);

            JFXApplication.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        } else {
            System.out.println("AI GAME");
            User tempUser = new User();

            tempUser.setPlayerName("Anon");
            tempUser.setPlayerColour("#5ef77f");
            tempUser.setProfilePictureUrl("/images/app/defaultProfImage.png");
            tempUser.setPlayerNumber(1);

            tempUser.setAttackDropOff(2);
            tempUser.setRange(3);

            GameData gameData = new GameData(4, 4, 1, tempUser, AIService.getAiPlayer(aITypes.getValue(), 2), gameBoard);
            AIGameController aiGameController = new AIGameController(gameData, 75);

            JFXApplication.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        }
    }

    private void configStartButton(){
        startButton.setOnMouseEntered(select());
    }

    private EventHandler<? super MouseEvent> select(){
        return (EventHandler<MouseEvent>) event -> AudioPlayer.playAudioFile(MenuAudio.SELECT_AUDIO);
    }

    private EventHandler<? super MouseEvent> move(){
        return (EventHandler<MouseEvent>) event -> AudioPlayer.playAudioFile(MenuAudio.MOVE_AUDIO);
    }


}
