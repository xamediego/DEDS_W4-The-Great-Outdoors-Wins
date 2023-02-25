package mai;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mai.bootstrap.BootstrapData;
import mai.enums.FXMLPart;
import mai.scenes.gamemenu.GameMenuController;
import mai.scenes.gamemenu.GameMenuScene;

public class JFXApplication extends Application {
    public static GameMenuController gameMenuController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        VBox rootBox = new VBox();
        rootBox.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        rootBox.setPrefSize(600, 600);
        rootBox.getStylesheets().add("Styling/app.css");
        rootBox.getStyleClass().add("primaryBackground");

        GameMenuScene gameMenuScene = new GameMenuScene(new GameMenuController(), FXMLPart.MENU);
        VBox.setVgrow(gameMenuScene.getRoot(), Priority.ALWAYS);

        gameMenuController = gameMenuScene.getController();

        rootBox.getChildren().add(gameMenuScene.getRoot());

        stage.setScene(new Scene(rootBox));
        stage.show();


        BootstrapData.loadData();
    }

}
