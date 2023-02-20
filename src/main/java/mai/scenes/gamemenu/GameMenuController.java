package mai.scenes.gamemenu;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import mai.scenes.gameconfig.GameConfigScene;

import java.net.URL;
import java.util.ResourceBundle;

public class GameMenuController implements Initializable {

    @FXML
    public VBox contentBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setContent(new GameConfigScene( null).getRoot());
    }

    public void setContent(Node root) {
        if(root != null){
            contentBox.getChildren().clear();
            contentBox.getChildren().add(root);
        }
    }

}
