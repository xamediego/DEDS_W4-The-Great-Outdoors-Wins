package mai.scenes.game.logic;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.HBox;


public class SpaceBox extends HBox {
    public int x, y;
    private final Label boxLabel;

    public SpaceBox(int x, int y, int blockSize) {
        this.x = x;
        this.y = y;

        this.setMinSize(blockSize, blockSize);
        this.setPrefSize(blockSize, blockSize);
        this.setMaxSize(blockSize, blockSize);

        this.boxLabel = new Label();
        this.boxLabel.setStyle("-fx-text-fill: WHITE; -fx-font-size: 12");
        this.boxLabel.setText("X: " + x + " | Y: " + y);
        Bloom bloom = new Bloom();
        bloom.setThreshold(.1);
        this.boxLabel.setEffect(bloom);

        this.getChildren().add(this.boxLabel);
        this.setAlignment(Pos.CENTER);
    }

    public void setText(String text){
//        boxLabel.setText(text);
    }

}
