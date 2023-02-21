package mai.scenes.gamemenu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import mai.data.User;
import mai.enums.FXMLPart;
import mai.scenes.gameconfig.GameConfigController;
import mai.scenes.gameconfig.GameConfigScene;
import mai.scenes.test.AbstractController;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameMenuController extends AbstractController implements Initializable {

    @FXML
    public VBox contentBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setContent(new GameConfigScene( new GameConfigController(Optional.empty()), FXMLPart.GAMECONFIG).getRoot());
    }

    public void setContent(Node root) {
        if(root != null){
            contentBox.getChildren().clear();
            contentBox.getChildren().add(root);
        }
    }

}
