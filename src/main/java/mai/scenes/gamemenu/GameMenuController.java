package mai.scenes.gamemenu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import mai.enums.FXMLPart;
import mai.scenes.game.aigame.AIGameController;
import mai.scenes.game.logic.GameData;
import mai.scenes.game.normalgame.GameController;
import mai.scenes.game.normalgame.GameScene;
import mai.scenes.gameconfig.GameConfigController;
import mai.scenes.gameconfig.GameConfigScene;
import mai.scenes.abstractscene.AbstractController;
import mai.scenes.gameover.GameOverController;
import mai.scenes.gameover.GameOverData;
import mai.scenes.gameover.GameOverScene;

import java.net.URL;
import java.util.ResourceBundle;

public class GameMenuController extends AbstractController implements Initializable {

    @FXML
    public VBox contentBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setContent(new GameConfigScene( new GameConfigController(this), FXMLPart.GAMECONFIG).getRoot());
    }

    public void setContent(Node root) {
        if(root != null){
            contentBox.getChildren().clear();
            contentBox.getChildren().add(root);
        }
    }

    public void setGameOverScreen(GameOverData gameOverData){
        GameOverController gameOverController = new GameOverController(gameOverData, this);
        setContent(new GameOverScene(gameOverController, FXMLPart.GAMEOVER).getRoot());
    }

    public void setGameConfigScreen(){
        setContent(new GameConfigScene(new GameConfigController(this), FXMLPart.GAMECONFIG).getRoot());
    }

    public void setAiGame(GameData gameData, int xSize, int ySize){
        AIGameController gameController = new AIGameController(gameData, xSize, ySize, this);

        setContent(new GameScene(gameController, FXMLPart.GAME).getRoot());
    }

    public void setPlayerGame(GameData gameData, int xSize, int ySize){
        GameController gameController =  new GameController(gameData, xSize, ySize, this);

        setContent(new GameScene(gameController, FXMLPart.GAME).getRoot());
    }

}
