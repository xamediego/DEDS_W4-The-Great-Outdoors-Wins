package mai.scenes.game.Parts;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class AvatarBox extends HBox {

    private final StackPane rootPane;
    private final ImageView avatar;
    private final Circle avatarCircle;

    public AvatarBox(int size, Image image) {
        rootPane = new StackPane();
        avatar = new ImageView(image);

        avatarCircle = new Circle();
        avatarCircle.getStyleClass().add("avatarCircle");
        avatarCircle.setRadius((float) size / 2);

        avatar.setFitWidth(size);
        avatar.setFitHeight(size);
        avatar.getStyleClass().add("avatarImage");

        Circle circle = new Circle(avatar.getBaselineOffset() / 2);
        circle.setLayoutX(avatar.getFitWidth() / 2);
        circle.setLayoutY(avatar.getFitHeight() / 2);

        avatar.setClip(circle);

        rootPane.getChildren().add(avatarCircle);
        rootPane.getChildren().add(avatar);

        this.getChildren().add(rootPane);

        this.getStylesheets().add("Styling/avatarbox.css");
        this.getStyleClass().add("avatarBox");

        this.setMaxSize(size, size);
        this.setMinSize(size, size);
    }

    public AvatarBox(int size, Image image, String colour) {
        this(size, image);

        avatarCircle.setStyle("-fx-stroke: " + colour);
    }

    public StackPane getRootPane() {
        return rootPane;
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public Circle getAvatarCircle() {
        return avatarCircle;
    }
}
