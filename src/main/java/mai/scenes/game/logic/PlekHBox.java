package mai.scenes.game.logic;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.HBox;


public class PlekHBox extends HBox {

    public int x, y;
    private final Label plekLabel;

    public PlekHBox(int x, int y, int groote) {
        this.x = x;
        this.y = y;

        this.setMinSize(groote, groote);
        this.setPrefSize(groote, groote);
        this.setMaxSize(groote, groote);

        this.plekLabel = new Label();
        this.plekLabel.setStyle("-fx-text-fill: WHITE; -fx-font-size: 20");
//        this.boxLabel.setText("X: " + x + " | Y: " + y);
        Bloom bloom = new Bloom();
        bloom.setThreshold(.1);
        this.plekLabel.setEffect(bloom);

        this.getChildren().add(this.plekLabel);
        this.setAlignment(Pos.CENTER);
    }

    public void setText(String text){
        plekLabel.setText(text);
    }

}
