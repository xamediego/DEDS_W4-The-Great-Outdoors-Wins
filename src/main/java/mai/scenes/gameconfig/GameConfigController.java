package mai.scenes.gameconfig;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import mai.JFXApplication;
import mai.data.Player;
import mai.data.User;
import mai.enums.AIType;
import mai.scenes.gamemenu.GameMenuController;
import mai.scenes.game.GameScene;
import mai.service.AIService;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameConfigController implements Initializable {

    @FXML
    public Label gameInfo;

    @FXML
    private ChoiceBox<AIType> aITypes;

    private final Optional<User> user;

    public GameConfigController(Optional<User> user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureChoiceBox();

        if(user.isPresent()){
            gameInfo.setText("Game history will be recorded after the match");
        } else {
            gameInfo.setText("No game history will be recorded after the match\nPlease login if you want your game history to be recorded");
        }
    }

    @FXML
    private void configureChoiceBox() {
        List<AIType> aiTypes = List.of(AIType.values());

        aITypes.getItems().addAll(aiTypes);

        aITypes.getSelectionModel().selectFirst();
    }

    @FXML
    private void startGame(){
        if(user.isPresent()){
            JFXApplication.gameMenuController.setContent(new GameScene((Player) user.get(), (Player) user.get(), 7,7 , 75).getRoot());
        } else {
            Player tempUser = new Player();
            tempUser.setPlayerName("Anon");
            tempUser.setPlayerColour("#5ef77f");
            tempUser.setProfilePictureUrl("/images/app/defaultProfImage.png");
            tempUser.setPlayerNumber(1);

            JFXApplication.gameMenuController.setContent(new GameScene(tempUser, AIService.getAiPlayer(aITypes.getValue()), 7,7 , 75).getRoot());
        }
    }
}
