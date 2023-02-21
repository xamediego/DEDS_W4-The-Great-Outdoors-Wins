package mai.scenes.gameconfig;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import mai.JFXApplication;
import mai.data.Player;
import mai.data.User;
import mai.enums.DIFFICULTY;
import mai.enums.FXMLPart;
import mai.scenes.game.aigame.AIGameController;
import mai.scenes.game.aigame.AIGameScene;
import mai.scenes.game.logic.GameBoard;
import mai.scenes.game.logic.GameData;
import mai.scenes.game.logic.Space;
import mai.scenes.test.AbstractController;
import mai.service.AIService;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameConfigController extends AbstractController implements Initializable {

    @FXML
    public Label gameInfo;

    @FXML
    private ChoiceBox<DIFFICULTY> aITypes;

    private final Optional<User> user;

    public GameConfigController(Optional<User> user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureChoiceBox();

        if (user.isPresent()) {
            gameInfo.setText("Game history will be recorded after the match");
        } else {
            gameInfo.setText("No game history will be recorded after the match\nPlease login if you want your game history to be recorded");
        }
    }

    @FXML
    private void configureChoiceBox() {
        List<DIFFICULTY> DIFFICULTIES = List.of(DIFFICULTY.values());

        aITypes.getItems().addAll(DIFFICULTIES);
        aITypes.getSelectionModel().selectFirst();
    }

    @FXML
    private void startGame() {
        GameBoard gameBoard = new GameBoard(7, 7, new Space[7][7]);
        gameBoard.configBoard();

        if (user.isPresent()) {
            GameData gameData = new GameData(4, 4, 1, (Player) user.get(), AIService.getAiPlayer(aITypes.getValue()), gameBoard);
            AIGameController aiGameController = new AIGameController(gameData, 75);

            JFXApplication.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        } else {
            Player tempUser = new Player();
            tempUser.setPlayerName("Anon");
            tempUser.setPlayerColour("#5ef77f");
            tempUser.setProfilePictureUrl("/images/app/defaultProfImage.png");
            tempUser.setPlayerNumber(1);

            GameData gameData = new GameData(4, 4, 1, tempUser, AIService.getAiPlayer(aITypes.getValue()), gameBoard);
            AIGameController aiGameController = new AIGameController(gameData, 75);

            JFXApplication.gameMenuController.setContent(new AIGameScene(aiGameController, FXMLPart.GAME).getRoot());
        }
    }
}
