package mai;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.goxr3plus.fxborderlessscene.borderless.CustomStage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mai.audio.MenuAudio;
import mai.bootstrap.BootstrapData;
import mai.enums.FXMLPart;
import mai.scenes.gamemenu.GameMenuController;
import mai.scenes.gamemenu.GameMenuScene;
import mai.scenes.titlebar.TitlebarController;
import mai.scenes.titlebar.TitlebarScene;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;

public class JFXApplication extends Application {
    public static GameMenuController gameMenuController;

    public static Stage mainStage;
    public static BorderlessScene borderlessScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
//        CustomStage customStage = new CustomStage(StageStyle.UNDECORATED);
//        mainStage = customStage;

        VBox rootBox = new VBox();
        rootBox.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        rootBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        rootBox.setMinSize(800, 950);
        rootBox.getStylesheets().add("Styling/app.css");
        rootBox.getStyleClass().add("primaryBackground");

//        borderlessScene = customStage.craftBorderlessScene(rootBox);

//        TitlebarScene titlebarScene = new TitlebarScene(new TitlebarController(), FXMLPart.TITLEBAR);
//        titlebarScene.getController().setBorderlessScene(borderlessScene);
//        titlebarScene.getController().init();
//        rootBox.getChildren().add(titlebarScene.getRoot());
//
//        borderlessScene.removeDefaultCSS();

        try {
            MenuAudio.loadSounds();
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        GameMenuScene gameMenuScene = new GameMenuScene(new GameMenuController(), FXMLPart.MENU);
        VBox.setVgrow(gameMenuScene.getRoot(), Priority.ALWAYS);

        gameMenuController = gameMenuScene.getController();

        rootBox.getChildren().add(gameMenuScene.getRoot());

//        customStage.setScene(borderlessScene);
//        customStage.getIcons().add(new Image("Images/app/icon.png"));
//        customStage.setTitle("Sneed-Cutter");
//        customStage.setResizable(false);
//        customStage.show();

        stage.setScene(new Scene(rootBox));
        stage.show();


        BootstrapData.loadData();
    }

}
