package mai.scenes.game.logic;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.HBox;


public class SpaceBox extends HBox {
    public int x, y;
    private final Label boxLabel;

    public SpaceBox(int x, int y, int minSize, int maxSize) {
        this.x = x;
        this.y = y;
        this.setMinSize(minSize, minSize);
        this.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        this.setMaxSize(maxSize, maxSize);

        this.boxLabel = new Label();
        this.boxLabel.setStyle("-fx-text-fill: WHITE; -fx-font-size: 15");
//        this.boxLabel.setText("X: " + x + " | Y: " + y);
        Bloom bloom = new Bloom();
        bloom.setThreshold(.1);
        this.boxLabel.setEffect(bloom);

        this.getChildren().add(this.boxLabel);
        this.setAlignment(Pos.CENTER);
    }

    public void setText(String text){
        boxLabel.setText(text);
    }

    @Override
    public String toString() {
        return "SpaceBox{" +
                "x=" + x +
                ", y=" + y +
                ", boxLabel=" + boxLabel +
                '}';
    }
}
