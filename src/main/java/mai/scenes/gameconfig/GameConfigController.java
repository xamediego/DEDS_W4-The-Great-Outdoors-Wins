package mai.scenes.gameconfig;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mai.audio.Music;
import mai.audio.MusicPlayer;
import mai.audio.SoundPlayer;
import mai.audio.ButtonAudio;
import mai.data.User;
import mai.enums.Difficulty;
import mai.enums.FXMLPart;
import mai.parts.NumberField;
import mai.scenes.abstractscene.AbstractController;
import mai.scenes.game.aigame.AIGameController;
import mai.scenes.game.aigame.AIGameScene;
import mai.scenes.game.logic.GameBoard;
import mai.scenes.game.logic.GameData;
import mai.scenes.game.logic.Space;
import mai.scenes.game.normalgame.GameController;
import mai.scenes.game.normalgame.GameScene;
import mai.scenes.gamemenu.GameMenuController;
import mai.service.AIService;
import mai.service.UserService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameConfigController extends AbstractController implements Initializable {

    private final GameMenuController gameMenuController;

    @FXML
    public Label gameInfo;

    @FXML
    private ChoiceBox<Difficulty> aITypes;

    @FXML
    private Button startButton;

    @FXML
    private Button startButtonNonAI;

    @FXML
    private TextField xSize;

    @FXML
    private TextField ySize;

    @FXML
    private TextField minSizeField;

    @FXML
    private TextField maxSizeField;

    MusicPlayer musicPlayer;

    public GameConfigController(GameMenuController gameMenuController) {
        this.gameMenuController = gameMenuController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureChoiceBox();
        configTextFields();
        config_m();
    }

    private void config_m(){
        musicPlayer = new MusicPlayer();
        musicPlayer.setMusic(Music.CONFIG.getAudio());
        musicPlayer.loopMusic();
    }

    private void configTextFields(){
        NumberField.makeNumberField(xSize, "([\\d])*?");
        xSize.setText("7");
        configAmountField(xSize);

        NumberField.makeNumberField(ySize, "([\\d])*?");
        ySize.setText("7");
        configAmountField(ySize);

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
        List<Difficulty> Difficulties = List.of(Difficulty.values());

        aITypes.getItems().addAll(Difficulties);
        aITypes.getSelectionModel().selectFirst();
    }

    @FXML
    private void startGame() {
        musicPlayer.stopMusic();

        int minSize = Integer.parseInt(minSizeField.getText());
        int maxSize = Integer.parseInt(maxSizeField.getText());

        GameBoard gameBoard = getNewBoard();

        if (UserService.user != null) {
            GameData gameData = getGameData(UserService.user, gameBoard);

            AIGameController aiGameController = new AIGameController(gameData, minSize, maxSize, gameMenuController);
            this.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        } else {
            User tempUser = getTempUser();
            GameData gameData = getGameData(tempUser, gameBoard);

            AIGameController aiGameController = new AIGameController(gameData, minSize, maxSize, gameMenuController);
            this.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        }
    }

    @FXML
    private void starNonAIGame(){
        musicPlayer.stopMusic();

        int minSize = Integer.parseInt(minSizeField.getText());
        int maxSize = Integer.parseInt(maxSizeField.getText());

        GameBoard gameBoard = getNewBoard();

        if (UserService.user != null) {
            GameData gameData = getGameData(UserService.user, gameBoard);

            AIGameController aiGameController = new AIGameController(gameData, minSize, maxSize, gameMenuController);
            this.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        } else {
            User tempUser = getTempUser();
            GameData gameData = getGameData(tempUser, gameBoard);

            GameController gameController = new GameController(gameData, minSize, maxSize, gameMenuController);
            this.gameMenuController.setContent(new GameScene(gameController, FXMLPart.GAME).getRoot());
        }
    }

    private GameBoard getNewBoard(){
        int xGrooteBord = Integer.parseInt(xSize.getText());
        int yGrooteBord = Integer.parseInt(ySize.getText());

        GameBoard gameBoard = new GameBoard(xGrooteBord, yGrooteBord, new Space[xGrooteBord][yGrooteBord]);
        gameBoard.configBoard();

        return gameBoard;
    }

    private User getTempUser(){
        User tempUser = new User();

        tempUser.setPlayerName("Anon");
        tempUser.setPlayerColour("#5ef77f");
        tempUser.setProfilePictureUrl("/images/app/defaultProfImage.png");

        tempUser.setPlayerNumber(1);
        tempUser.setAttackDropOff(2);
        tempUser.setRange(3);

        return tempUser;
    }

    private GameData getGameData(User user, GameBoard gameBoard){
        return new GameData(4, 4, 1, user, AIService.getAiPlayer(aITypes.getValue(), 2), gameBoard);
    }

    @FXML
    private void select(){
        SoundPlayer.playAudioFile(ButtonAudio.SELECT.getAudio());
    }

    @FXML
    private void start(){
        SoundPlayer.playAudioFile(ButtonAudio.START.getAudio());
    }

    @FXML
    private void move(){
        SoundPlayer.playAudioFile(ButtonAudio.MOVE.getAudio());
    }


}
