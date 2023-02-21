package mai;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.goxr3plus.fxborderlessscene.borderless.CustomStage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mai.scenes.titlebar.TitlebarController;
import mai.scenes.gamemenu.GameMenuController;
import mai.scenes.gamemenu.GameMenuScene;

public class JFXApplication extends Application{

    public static GameMenuController gameMenuController;
    public static TitlebarController titleBarController;

    public static Stage mainStage;
    public static BorderlessScene borderlessScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        CustomStage customStage = new CustomStage(StageStyle.UNDECORATED);

        mainStage = customStage;

        GameMenuScene gameMenuScene = new GameMenuScene();

        borderlessScene = customStage.craftBorderlessScene(gameMenuScene.getRoot());

        titleBarController.setBorderlessScene(borderlessScene);

        Scene scene = new Scene(gameMenuScene.getRoot());

        gameMenuController = gameMenuScene.getController();

        stage.setScene(scene);

        stage.getIcons().add(new Image("Images/app/icon.png"));

        stage.setTitle("Sneed-Cutter");

        stage.setResizable(false);

        stage.show();
    }

}
