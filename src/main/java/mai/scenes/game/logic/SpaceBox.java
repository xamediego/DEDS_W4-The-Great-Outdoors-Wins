package mai.scenes.game.logic;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class SpaceBox extends HBox {

    public int x, y;

    public SpaceBox(int x, int y, int blockSize) {
        this.x = x;
        this.y = y;

        this.setMinSize(blockSize, blockSize);
        this.setPrefSize(blockSize, blockSize);
        this.setMaxSize(blockSize, blockSize);

        this.setAlignment(Pos.CENTER);
    }

}
