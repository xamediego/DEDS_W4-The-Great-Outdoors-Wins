package mai.scenes.gameover;

import mai.data.Player;
import mai.enums.FXMLPart;
import mai.enums.MatchOverType;
import mai.scenes.test.AbstractScene;

import java.io.IOException;

public class GameOverScene extends AbstractScene<GameOverController> {


    public GameOverScene(GameOverController controller, FXMLPart fxmlPart) {
        super(controller, fxmlPart);
    }
}
