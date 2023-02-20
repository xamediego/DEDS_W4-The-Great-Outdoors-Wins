package mai.scenes.gameconfig;

import mai.data.User;
import mai.scenes.gamemenu.GameMenuController;

import java.io.IOException;
import java.util.Optional;

public class GameConfigScene {

    private GameConfigController gameConfigController;
    private GameConfig gameConfig;

    public GameConfigScene(User user) {
        try {
            this.gameConfigController =  new GameConfigController(Optional.ofNullable(user));
            this.gameConfig = new GameConfig(this.gameConfigController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameConfigController getController() {
        return gameConfigController;
    }

    public GameConfig getRoot() {
        return this.gameConfig;
    }
}
