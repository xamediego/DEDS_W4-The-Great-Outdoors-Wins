package mai.scenes.game;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Game extends VBox {

    public Game(GameController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FxmlFiles/Game/Game.fxml"));

        loader.setController(controller);
        loader.setRoot(this);
        loader.load();
    }

}
