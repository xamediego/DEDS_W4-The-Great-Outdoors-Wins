package mai;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mai.scenes.gamemenu.GameMenuController;
import mai.scenes.gamemenu.GameMenuScene;

public class JFXApplication extends Application{

    public static GameMenuController gameMenuController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        GameMenuScene gameMenuScene = new GameMenuScene();

        Scene scene = new Scene(gameMenuScene.getRoot());

        gameMenuController = gameMenuScene.getController();

        stage.setScene(scene);

        stage.getIcons().add(new Image("Images/app/icon.png"));

        stage.setTitle("Sneed-Cutter");

        stage.setResizable(false);

        stage.show();
    }

}
