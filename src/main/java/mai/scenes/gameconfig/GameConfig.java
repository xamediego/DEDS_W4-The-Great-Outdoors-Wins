package mai.scenes.gameconfig;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GameConfig extends VBox {

    public GameConfig(GameConfigController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FxmlFiles/Game/GameConfig.fxml"));

        loader.setController(controller);
        loader.setRoot(this);
        loader.load();
    }

}
