package mai.gameinit;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import com.goxr3plus.fxborderlessscene.borderless.CustomStage;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mai.data.AI;
import mai.data.User;
import mai.enums.FXMLPart;
import mai.scenes.titlebar.TitlebarController;
import mai.scenes.titlebar.TitlebarScene;

import java.util.List;

public class GameApplicationWindowed extends GameApplication{

    private CustomStage customStage;
    private BorderlessScene borderlessScene;

    private VBox rootBox;
    private TitlebarScene titlebarScene;

    public GameApplicationWindowed(User user, List<AI> aiUsers) {
        super(user, aiUsers);
    }

    public VBox getNewRootBox(int initialXSize , int initialYSize, String appCssUrl, String classname){
        rootBox = new VBox();
        rootBox.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        rootBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        rootBox.setMinSize(initialXSize, initialYSize);
        rootBox.getStylesheets().add(appCssUrl);
        rootBox.getStyleClass().add(classname);

        return rootBox;
    }

    public TitlebarScene getNewTitleBar(Stage mainStage, BorderlessScene borderlessScene){
        titlebarScene = new TitlebarScene(new TitlebarController(mainStage, borderlessScene), FXMLPart.TITLEBAR);
        titlebarScene.getController().init();

        return titlebarScene;
    }

    public BorderlessScene getNewBorderlessScene(CustomStage customStage ,VBox rootBox){
        borderlessScene = customStage.craftBorderlessScene(rootBox);
        borderlessScene.removeDefaultCSS();
        customStage.setScene(borderlessScene);

        return borderlessScene;
    }

    public CustomStage getNewCustomStage(String title, String iconUrl){
        customStage = new CustomStage(StageStyle.UNDECORATED);

        customStage.getIcons().add(new Image(iconUrl));
        customStage.setTitle(title);
        customStage.setResizable(false);

        return customStage;
    }
}
