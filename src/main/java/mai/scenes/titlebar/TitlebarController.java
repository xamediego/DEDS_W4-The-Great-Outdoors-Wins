package mai.scenes.titlebar;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import mai.JFXApplication;
import mai.scenes.sceneconstructor.AbstractController;
import org.kordamp.ikonli.javafx.FontIcon;

public class TitlebarController extends AbstractController {

    @FXML
    private VBox titleBox;

    @FXML
    private FontIcon minMaxIcon;

    private BorderlessScene borderlessScene;

    public void setBorderlessScene(BorderlessScene borderlessScene) {
        this.borderlessScene = borderlessScene;
    }

    public void init(){
        borderlessScene.setMoveControl(titleBox);
    }

    @FXML
    private void minimize(){
        JFXApplication.mainStage.setIconified(true);
    }

    @FXML
    private void exit(){
        JFXApplication.mainStage.close();
    }

    @FXML
    private void minMax(){
        JFXApplication.borderlessScene.maximizeStage();
        iconCheck();
    }

    private void iconCheck(){
        if(JFXApplication.borderlessScene.isMaximized()){
            minMaxIcon.setIconLiteral("far-window-maximize");
        } else {
            minMaxIcon.setIconLiteral("far-window-restore");
        }
    }

}
