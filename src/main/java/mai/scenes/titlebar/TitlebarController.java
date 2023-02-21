package mai.scenes.titlebar;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import mai.JFXApplication;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class TitlebarController implements Initializable {

    @FXML
    private HBox titleBox;

    @FXML
    private FontIcon minMaxIcon;

    private BorderlessScene borderlessScene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JFXApplication.titleBarController = this;
        borderlessScene.setMoveControl(titleBox);
    }

    public void setBorderlessScene(BorderlessScene borderlessScene) {
        this.borderlessScene = borderlessScene;
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
