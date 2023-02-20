package mai.scenes.gamemenu;

import java.io.IOException;

public class GameMenuScene {

    private GameMenuController gameConfigController;
    private GameMenu gameMenu;

    public GameMenuScene() {
        try {
            this.gameConfigController =  new GameMenuController();
            this.gameMenu = new GameMenu(this.gameConfigController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameMenuController getController() {
        return gameConfigController;
    }

    public GameMenu getRoot() {
        return this.gameMenu;
    }

}
