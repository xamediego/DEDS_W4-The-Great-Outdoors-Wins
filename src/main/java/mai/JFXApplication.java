package mai;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.goxr3plus.fxborderlessscene.borderless.CustomStage;
import javafx.application.Application;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mai.bootstrap.BootstrapData;
import mai.enums.FXMLPart;
import mai.gameinit.GameApplicationWindowed;
import mai.scenes.gamemenu.GameMenuController;
import mai.scenes.gamemenu.GameMenuScene;
import mai.scenes.titlebar.TitlebarScene;
import mai.service.AIService;
import mai.service.UserService;

public class JFXApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BootstrapData.loadData();

        GameApplicationWindowed gameApplicationWindowed = new GameApplicationWindowed(UserService.user, AIService.aiList);

        VBox rootBox = gameApplicationWindowed.getNewRootBox(800, 600, "Styling/app.css", "primaryBackground");

        CustomStage customStage = gameApplicationWindowed.getNewCustomStage("TGOW", "Images/app/icon.png");

        BorderlessScene borderlessScene = gameApplicationWindowed.getNewBorderlessScene(customStage, rootBox);

        TitlebarScene titlebarScene = gameApplicationWindowed.getNewTitleBar(customStage, borderlessScene);

        rootBox.getChildren().add(titlebarScene.getRoot());

        GameMenuScene gameMenuScene = new GameMenuScene(new GameMenuController(), FXMLPart.MENU);

        rootBox.getChildren().add(gameMenuScene.getRoot());
        VBox.setVgrow(gameMenuScene.getRoot(), Priority.ALWAYS);

        customStage.show();
    }

}
