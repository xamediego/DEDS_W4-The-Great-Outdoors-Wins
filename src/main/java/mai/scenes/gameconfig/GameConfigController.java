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
import mai.data.Speler;
import mai.enums.AIType;
import mai.enums.FXMLPart;
import mai.parts.NumberField;
import mai.scenes.game.aigame.AIGameController;
import mai.scenes.game.aigame.AIGameScene;
import mai.scenes.game.logic.GameBord;
import mai.scenes.game.logic.GameData;
import mai.scenes.game.logic.Plek;
import mai.scenes.game.normalgame.GameController;
import mai.scenes.game.normalgame.GameScene;
import mai.scenes.sceneconstructor.AbstractController;
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
    private ChoiceBox<AIType> aITypes;

    @FXML
    private Button startButton;

    @FXML
    private Button startButtonNonAI;

    @FXML
    private TextField xGroote;

    @FXML
    private TextField yGroote;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureChoiceBox();
        configStartButton();
        configTextFields();

        if (UserService.speler != null) {
            gameInfo.setText("Game history will be recorded after the match");
        } else {
            gameInfo.setText("No game history will be recorded after the match\nPlease login if you want your game history to be recorded");
        }
    }

    private void configTextFields(){
        NumberField.makeNumberField(xGroote, "([\\d])*?");
        xGroote.setText("7");
        configNumberField(xGroote);

        NumberField.makeNumberField(yGroote, "([\\d])*?");
        yGroote.setText("7");
        configNumberField(yGroote);
    }


    private void configNumberField(TextField numberField) {
        numberField.textProperty().addListener((observableValue, s, t1) -> {
            startButton.setDisable(observableValue.getValue() == null || observableValue.getValue().isEmpty() || Integer.parseInt(numberField.getText()) < 7);
        });
    }

    @FXML
    private void configureChoiceBox() {
        List<AIType> DIFFICULTIES = List.of(AIType.values());

        aITypes.getItems().addAll(DIFFICULTIES);
        aITypes.getSelectionModel().selectFirst();

        aITypes.setOnMouseEntered(select());
        aITypes.setOnMouseClicked(move());
    }

    @FXML
    private void startGame() {
        AudioPlayer.playAudioFile(MenuAudio.START_AUDIO);

        int xGrooteBord = Integer.parseInt(xGroote.getText());
        int yGrooteBord = Integer.parseInt(yGroote.getText());

        GameBord gameBord = new GameBord(xGrooteBord, yGrooteBord, new Plek[xGrooteBord][yGrooteBord]);
        gameBord.configBoard();

        if (UserService.speler != null) {
            GameData gameData = getGameData(UserService.speler, gameBord);

            AIGameController aiGameController = new AIGameController(gameData, 75);
            JFXApplication.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        } else {
            Speler tempSpeler = getTempSpeler();
            GameData gameData = getGameData(tempSpeler, gameBord);

            AIGameController aiGameController = new AIGameController(gameData, 75);
            JFXApplication.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        }
    }

    @FXML
    private void starNonAIGame(){
        AudioPlayer.playAudioFile(MenuAudio.START_AUDIO);

        GameBord gameBord = getNieuwGameBord();

        if (UserService.speler != null) {
            GameData gameData = getGameData(UserService.speler, gameBord);

            AIGameController aiGameController = new AIGameController(gameData, 75);
            JFXApplication.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        } else {
            Speler tempSpeler = getTempSpeler();
            GameData gameData = getGameData(tempSpeler, gameBord);

            GameController gameController = new GameController(gameData, 75);
            JFXApplication.gameMenuController.setContent(new GameScene(gameController, FXMLPart.GAME).getRoot());
        }
    }

    private GameBord getNieuwGameBord(){
        int xGrooteBord = Integer.parseInt(xGroote.getText());
        int yGrooteBord = Integer.parseInt(yGroote.getText());

        GameBord gameBord = new GameBord(xGrooteBord, yGrooteBord, new Plek[xGrooteBord][yGrooteBord]);
        gameBord.configBoard();

        return gameBord;
    }

    private Speler getTempSpeler(){
        Speler tempSpeler = new Speler();

        tempSpeler.setNaam("Anon");
        tempSpeler.setSpelerKleur("#5ef77f");
        tempSpeler.setProfilePictureUrl("/images/app/defaultProfImage.png");

        tempSpeler.setSpelerNummer(1);
        tempSpeler.setMinBereik(2);
        tempSpeler.setBereik(3);

        return tempSpeler;
    }

    private GameData getGameData(Speler speler, GameBord gameBord){
        return new GameData(4, 4, 1, speler, AIService.getAiPlayer(aITypes.getValue(), 2), gameBord);
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
