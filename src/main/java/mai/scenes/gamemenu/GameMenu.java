package mai.scenes.gamemenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GameMenu extends VBox {

    public GameMenu(GameMenuController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FxmlFiles/Game/GameMenu.fxml"));

        loader.setController(controller);
        loader.setRoot(this);
        loader.load();
    }

}
