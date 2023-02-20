package mai.scenes.gameover;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GameOver extends VBox {

    public GameOver(GameOverController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FxmlFiles/Game/GameOver.fxml"));

        loader.setController(controller);
        loader.setRoot(this);
        loader.load();
    }

}
