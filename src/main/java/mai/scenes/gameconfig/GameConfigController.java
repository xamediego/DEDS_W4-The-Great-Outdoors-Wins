package mai.scenes.gameconfig;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import mai.JFXApplication;
import mai.audio.MenuAudio;
import mai.data.User;
import mai.enums.Difficulty;
import mai.enums.FXMLPart;
import mai.parts.NumberField;
import mai.scenes.game.aigame.AIGameController;
import mai.scenes.game.aigame.AIGameScene;
import mai.scenes.game.logic.GameBoard;
import mai.scenes.game.logic.GameData;
import mai.scenes.game.logic.Space;
import mai.scenes.game.normalgame.GameController;
import mai.scenes.game.normalgame.GameScene;
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

    @FXML
    private Button startButtonNonAI;

    @FXML
    private TextField xGroote;

    @FXML
    private TextField yGroote;

    @FXML
    private TextField minSizeField;

    @FXML
    private TextField maxSizeField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureChoiceBox();
        configStartButton();
        configTextFields();

        if (UserService.user != null) {
            gameInfo.setText("Game history will be recorded after the match");
        } else {
            gameInfo.setText("No game history will be recorded after the match\nPlease login if you want your game history to be recorded");
        }
    }

    private void configTextFields(){
        NumberField.makeNumberField(xGroote, "([\\d])*?");
        xGroote.setText("7");
        configAmountField(xGroote);

        NumberField.makeNumberField(yGroote, "([\\d])*?");
        yGroote.setText("7");
        configAmountField(yGroote);

        NumberField.makeNumberField(minSizeField, "([\\d])*?");
        minSizeField.setText("50");
        configSizeField(minSizeField);

        NumberField.makeNumberField(maxSizeField, "([\\d])*?");
        maxSizeField.setText("50");
        configSizeField(maxSizeField);
    }


    private void configAmountField(TextField numberField) {
        numberField.textProperty().addListener((observableValue, s, t1) -> startButton.setDisable(observableValue.getValue() == null || observableValue.getValue().isEmpty() || Integer.parseInt(numberField.getText()) < 7));
    }

    private void configSizeField(TextField numberField) {
        numberField.textProperty().addListener((observableValue, s, t1) -> startButton.setDisable(observableValue.getValue() == null || observableValue.getValue().isEmpty() || Integer.parseInt(numberField.getText()) < 10 || Integer.parseInt(numberField.getText()) > 150));
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



        int minSize = Integer.parseInt(minSizeField.getText());
        int maxSize = Integer.parseInt(maxSizeField.getText());

        GameBoard gameBord = getNewBoard();

        if (UserService.user != null) {
            GameData gameData = getGameData(UserService.user, gameBord);

            AIGameController aiGameController = new AIGameController(gameData, minSize, maxSize);
            JFXApplication.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        } else {
            User tempSpeler = getTempSpeler();
            GameData gameData = getGameData(tempSpeler, gameBord);

            AIGameController aiGameController = new AIGameController(gameData, minSize, maxSize);
            JFXApplication.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        }
    }

    @FXML
    private void starNonAIGame(){
        AudioPlayer.playAudioFile(MenuAudio.START_AUDIO);

        int minSize = Integer.parseInt(minSizeField.getText());
        int maxSize = Integer.parseInt(maxSizeField.getText());


        GameBoard gameBord = getNewBoard();

        if (UserService.user != null) {
            GameData gameData = getGameData(UserService.user, gameBord);

            AIGameController aiGameController = new AIGameController(gameData, minSize, maxSize);
            JFXApplication.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        } else {
            User tempSpeler = getTempSpeler();
            GameData gameData = getGameData(tempSpeler, gameBord);

            GameController gameController = new GameController(gameData, minSize, maxSize);
            JFXApplication.gameMenuController.setContent(new GameScene(gameController, FXMLPart.GAME).getRoot());
        }
    }

    private GameBoard getNewBoard(){
        int xGrooteBord = Integer.parseInt(xGroote.getText());
        int yGrooteBord = Integer.parseInt(yGroote.getText());

        GameBoard gameBord = new GameBoard(xGrooteBord, yGrooteBord, new Space[xGrooteBord][yGrooteBord]);
        gameBord.configBoard();

        return gameBord;
    }

    private User getTempSpeler(){
        User tempSpeler = new User();

        tempSpeler.setPlayerName("Anon");
        tempSpeler.setPlayerColour("#5ef77f");
        tempSpeler.setProfilePictureUrl("/images/app/defaultProfImage.png");

        tempSpeler.setPlayerNumber(1);
        tempSpeler.setAttackDropOff(2);
        tempSpeler.setRange(3);

        return tempSpeler;
    }

    private GameData getGameData(User speler, GameBoard gameBoard){
        return new GameData(4, 4, 1, speler, AIService.getAiPlayer(aITypes.getValue(), 2), gameBoard);
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
