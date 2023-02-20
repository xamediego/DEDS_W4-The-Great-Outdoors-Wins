package mai.scenes.game;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import mai.JFXApplication;
import mai.data.Player;
import mai.datastructs.Stapel;
import mai.scenes.gameover.GameOverScene;
import mai.enums.MatchOverType;

import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    // ----- Board Interface -----

    private final int xGroote, yGroote, spaceSize;

    @FXML
    private HBox GameBoardContainer;

    @FXML
    private VBox bordColumnBox;

    private HBox[] bordRows;
    private Space[][] bord;

    private Stapel<Space[][]> gameGeschiedenis;

    @FXML
    private Label player1Score;

    @FXML
    private Label player2Score;

    @FXML
    private Label turnInfo;

    @FXML
    private Label currentPlayerLabel;

    @FXML
    private Button gameOverButton;

    // ----- UserDetails -----

    @FXML
    private Label player1Label;

    @FXML
    private Label player2Label;

    @FXML
    private ImageView player1Avatar;

    @FXML
    private Circle player1AvatarCircle;

    @FXML
    private ImageView player2Avatar;

    @FXML
    private Circle player2AvatarCircle;

    // ----- GameDetails -----

    private int playerOneScore = 4, playerTwoScore = 4, turnNumber = 1;
    private Player currentPlayer;

    // ----- Users -----

    private final Player player1, player2;

    public GameController(Player player1, Player player2, int xSize, int ySize, int spaceSize) {
        this.player1 = player1;
        this.player2 = player2;

        this.xGroote = xSize;
        this.yGroote = ySize;
        this.spaceSize = spaceSize;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configPlayer1(player1);
        configPlayer2(player2);

        configBoard(xGroote, yGroote, spaceSize);
        configInitialSpaces(xGroote, yGroote, player1.getPlayerColour(), player2.getPlayerColour());

        setInitialTurn();

        gameGeschiedenis.push(bord);

        makeSelectAble(currentPlayer.getPlayerNumber());
    }

    private boolean checkGameConditions() {

        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {
                if (!bord[x][y].isTaken()) return false;
            }
        }

        return true;
    }

    private void endPlayerTurn() {
        removeSelectAble(currentPlayer.getPlayerNumber());

        if (checkGameConditions()) {
            endGame();
        } else {
            if (currentPlayer.getPlayerNumber() == 1) {
                currentPlayer = player2;
            } else {
                currentPlayer = player1;
            }

            turnNumber++;

            gameGeschiedenis.push(bord);

            setTurnGlow(currentPlayer.getPlayerNumber());
            setCurrentPlayer(currentPlayer.getPlayerName(), currentPlayer.getPlayerNumber());
            makeSelectAble(currentPlayer.getPlayerNumber());
        }
    }

    // ----- make the tiles for the current player selectable -----

    private void makeSelectAble(int playerNumber) {
        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {
                if (bord[x][y].getPlayerNumber() == playerNumber) {
                    bordRows[y].getChildren().get(x).getStyleClass().add("spaceSelect");
                    bordRows[y].getChildren().get(x).setOnMouseClicked(showPossible());
                }
            }
        }
    }

    private void removeSelectAble(int playerNumber) {
        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {
                if (bord[x][y].getPlayerNumber() == playerNumber)
                    bordRows[y].getChildren().get(x).getStyleClass().clear();
                bordRows[y].getChildren().get(x).setOnMouseClicked(null);
            }
        }
    }

    // ----- setting selectable tiles -----

    private Space selected;
    private SpaceBox selectedSpaceBox;

    private EventHandler<? super MouseEvent> showPossible() {
        return (EventHandler<MouseEvent>) event -> {
            SpaceBox space = (SpaceBox) event.getSource();


            if (selected != null && (selected.y == space.y && selected.x == space.x)) {
                space.getStyleClass().clear();

                deselectPrevPossible(new Space(space.x, space.y));

                selected = null;
            } else {
                if (selected != null) deselectPrevPossible(selected);

                space.getStyleClass().add("spaceSelected");
                selectedSpaceBox = space;

                selected = new Space(space.x, space.y);

                //add block possibility?
                for (int y = space.y - 2; y < space.y + 3; y++) {
                    if (y >= 0 && y < yGroote) {
                        for (int x = space.x - 2; x < space.x + 3; x++) {
                            if (x >= 0 && x < xGroote) {
                                if (!bord[x][y].isTaken()) {
                                    int xDif, yDif;

                                    if (space.x > x) {
                                        xDif = space.x - x;
                                    } else {
                                        xDif = x - space.x;
                                    }

                                    if (space.y > y) {
                                        yDif = space.y - y;
                                    } else {
                                        yDif = y - space.y;
                                    }

                                    if (xDif < 2 && yDif < 2) {
                                        SpaceBox spaceBox = (SpaceBox) bordRows[y].getChildren().get(x);
                                        spaceBox.getStyleClass().add("copySelect");
                                        spaceBox.setOnMouseClicked(oneSpaceAttack());
                                    } else {
                                        SpaceBox spaceBox = (SpaceBox) bordRows[y].getChildren().get(x);
                                        spaceBox.getStyleClass().add("farSelect");
                                        spaceBox.setOnMouseClicked(twoSpaceAttack(selectedSpaceBox));
                                    }

                                    // ----- diagonaal -----
                                /*
                                if (xDif + yDif < 3) {
                                    if (xDif + yDif == 1) {
                                        SpaceBox spaceBox = (SpaceBox) bordRows[y].getChildren().get(x);
                                        spaceBox.getStyleClass().add("copySelect");
                                        spaceBox.setOnMouseClicked(oneSpaceAttack());
                                    } else if (xDif + yDif == 2) {
                                        SpaceBox spaceBox = (SpaceBox) bordRows[y].getChildren().get(x);
                                        spaceBox.getStyleClass().add("farSelect");
                                        spaceBox.setOnMouseClicked(twoSpaceAttack(selectedSpaceBox));
                                    }
                                }
                                */
                                }
                            }
                        }
                    }
                }
            }

        };
    }

    private void deselectPrevPossible(Space selectedSpace) {

        selectedSpaceBox.getStyleClass().clear();
        selectedSpaceBox.getStyleClass().add("spaceSelect");

        for (int y = selectedSpace.y - 2; y < selectedSpace.y + 3; y++) {
            if (y >= 0 && y < yGroote) {
                for (int x = selectedSpace.x - 2; x < selectedSpace.x + 3; x++) {
                    if (x >= 0 && x < xGroote) {
                        if (!bord[x][y].isTaken()) {
                            bordRows[y].getChildren().get(x).getStyleClass().clear();
                            bordRows[y].getChildren().get(x).setOnMouseClicked(null);
                        }
                    }
                }
            }
        }
    }
    // ----- movement methods -----

    //adds points
    private EventHandler<? super MouseEvent> oneSpaceAttack() {
        return (EventHandler<MouseEvent>) event -> {
            SpaceBox space = (SpaceBox) event.getSource();

            space.getStyleClass().clear();

            bord[space.x][space.y].take(currentPlayer.getPlayerNumber());

            setColour(space, currentPlayer.getPlayerColour());

            deselectPrevPossible(selected);

            addPoint(currentPlayer.getPlayerNumber());

            endPlayerTurn();
        };
    }

    //doesn't add points
    private EventHandler<? super MouseEvent> twoSpaceAttack(SpaceBox oldSpace) {

        return (EventHandler<MouseEvent>) event -> {
            SpaceBox space = (SpaceBox) event.getSource();

            space.getStyleClass().clear();

            bord[space.x][space.y].take(currentPlayer.getPlayerNumber());

            bord[oldSpace.x][oldSpace.y].deselect();

            setColour(space, currentPlayer.getPlayerColour());

            removeColour(oldSpace);

            deselectPrevPossible(selected);

            endPlayerTurn();
        };
    }

    private void addPoint(int playerNumber) {
        if (playerNumber == 1) {
            playerOneScore++;
        } else {
            playerTwoScore++;
        }

        updatePointLabel();
    }

    private void updatePointLabel() {
        player1Score.setText(String.valueOf(playerOneScore));
        player2Score.setText(String.valueOf(playerTwoScore));
    }

    // ----- configuring initial user details -----

    private void configPlayer1(Player user) {
        configAvatar(user, player1Avatar, player1AvatarCircle);
        configLabel(user, player1Label);
    }

    private void configPlayer2(Player user) {
        configAvatar(user, player2Avatar, player2AvatarCircle);
        configLabel(user, player2Label);
    }

    // ----- configuring initial avatars -----

    private void configAvatar(Player user, ImageView avatarView, Circle playerAvatarCircle) {
        if (user.getProfilePictureUrl() != null) {
            setAvatar(avatarView, new Image(user.getProfilePictureUrl()), playerAvatarCircle, user.getPlayerColour());
        } else {
            setAvatar(avatarView, new Image("/images/app/defaultProfImage.png"), playerAvatarCircle, user.getPlayerColour());
        }
    }

    private void setAvatar(ImageView avatarView, Image image, Circle playerAvatarCircle, String colour) {
        avatarView.setImage(image);

        Circle circle = new Circle(avatarView.getBaselineOffset() / 2);
        circle.setLayoutX(avatarView.getFitWidth() / 2);
        circle.setLayoutY(avatarView.getFitHeight() / 2);

        avatarView.setClip(circle);

        playerAvatarCircle.setStyle("-fx-stroke: " + colour);
    }

    // ----- configuring initial player names -----

    private void configLabel(Player user, Label label) {
        label.setText(user.getPlayerName());
        label.setStyle("-fx-text-fill: " + user.getPlayerColour());
    }

    // ----- configuring initial board -----

    //insert bord for config?
    private void configBoard(int xGroote, int yGroote, int size) {

        bordRows = new HBox[yGroote];

        bord = new Space[xGroote][yGroote];

        gameGeschiedenis = new Stapel<>();

        for (int y = 0; y < yGroote; y++) {
            HBox row = new HBox();

            for (int x = 0; x < xGroote; x++) {
                SpaceBox spaceBox = new SpaceBox(x, y, size);
                setBoxBorder(x, y, spaceBox);
                row.getChildren().add(spaceBox);

                Space space = getNewSpace(x, y);
                bord[x][y] = space;
            }

            bordRows[y] = row;
        }

        bordColumnBox.getChildren().addAll(bordRows);
    }

    private Space getNewSpace(int x, int y) {
        if (x == 0 && (y == 5 || y == 6)) return new Space(x, y, true, 1);
        if (x == 1 && (y == 5 || y == 6)) return new Space(x, y, true, 1);

        if (y == 0 && (x == 5 || x == 6)) return new Space(x, y, true, 2);
        if (y == 1 && (x == 5 || x == 6)) return new Space(x, y, true, 2);

        return new Space(x, y);
    }


    private void setBoxBorder(int x, int y, Node space) {
        if (xGroote - 1 != x && y != 0) {
            space.setStyle(
                    "-fx-border-width: 2 2 0 0;\n" +
                            "-fx-border-color: #242526;");
        } else if (xGroote - 1 != x) {
            space.setStyle(
                    "-fx-border-width: 0 2 0 0;\n" +
                            "-fx-border-color: #242526;");
        } else if (xGroote - 1 == x && y != 0) {
            space.setStyle(
                    "-fx-border-width: 2 0 0 0;\n" +
                            "-fx-border-color: #242526;");
        }
    }

    // ----- configuring initial player spaces -----

    // TODO: 19/02/2023 Find way to make background color look nicer
    private void configInitialSpaces(int xGroote, int yGroote, String color1, String color2) {
        //player1
        for (int y = yGroote - 2; y <= yGroote - 1; y++) {
            for (int x = 0; x <= 1; x++) {
                SpaceBox spaceBox = (SpaceBox) bordRows[y].getChildren().get(x);
                setColour(spaceBox, color1);

                bord[x][y].take(1);
            }
        }

        //player2
        for (int y = 0; y <= 1; y++) {
            for (int x = xGroote - 2; x <= xGroote - 1; x++) {
                SpaceBox spaceBox = (SpaceBox) bordRows[y].getChildren().get(x);
                setColour(spaceBox, color2);

                bord[x][y].take(2);
            }
        }
    }

    //expecting the color code to be at index 2 of an string split with postive lookup ';'
    // TODO: 20/02/2023 fix alpha channel
    private void setColour(SpaceBox spaceBox, String color) {
        String style = spaceBox.getStyle();
        String newStyle = "";

        if (!style.isEmpty()) {
            String[] t = style.split("((?=;))");
            newStyle = t[0] + t[1] + ";";
        }

        spaceBox.setStyle(newStyle + "\n-fx-background-color: #" + "B0" + color.substring(3) + ";");
    }

    private void removeColour(SpaceBox spaceBox) {
        String style = spaceBox.getStyle();
        String newStyle = "";

        if (!style.isEmpty()) {
            String[] t = style.split("((?=;))");
            newStyle = t[0] + t[1] + ";";
        }

        spaceBox.setStyle(newStyle);
    }


    // ----- configuring initial turn -----

    private void setInitialTurn() {
        Random random = new Random();

        if (random.nextInt(2) == 0) {
            setTurn(player1.getPlayerName(), 1);
        } else {
            setTurn(player2.getPlayerName(), 2);
        }
    }

    private void setTurn(String username, int playerNumber) {
        turnInfo.setText("Turn " + turnNumber + " | ");

        setCurrentPlayer(username, playerNumber);
    }

    private void setCurrentPlayer(String username, int playerNumber) {
        currentPlayerLabel.setText(username);

        if (playerNumber == 1) {
            addTurnGlow(player1Label, player1AvatarCircle);
            currentPlayer = player1;
        } else {
            addTurnGlow(player2Label, player2AvatarCircle);
            currentPlayer = player2;
        }
    }

    // ----- set or removes a glow when it's one player's turn or not -----

    private void setTurnGlow(int playerNumber) {
        if (playerNumber == 1) {
            addTurnGlow(player1Label, player1AvatarCircle);
            removeTurnGlow(player2Label, player2AvatarCircle);
        } else {
            addTurnGlow(player2Label, player2AvatarCircle);
            removeTurnGlow(player1Label, player1AvatarCircle);
        }
    }

    private void addTurnGlow(Label playerLabel, Circle avatarCircle) {
        Bloom bloom = new Bloom();
        bloom.setThreshold(0.01);

        playerLabel.setEffect(bloom);
        avatarCircle.setEffect(bloom);
    }

    private void removeTurnGlow(Label playerLabel, Circle avatarCircle) {
        playerLabel.setEffect(null);
        avatarCircle.setEffect(null);
    }

    // ----- event that runs when the game has ended -----

    @FXML
    private void endGame() {
        if (playerOneScore > playerTwoScore) {
            JFXApplication.gameMenuController.setContent(new GameOverScene(player1, player2, MatchOverType.P1, playerOneScore, playerTwoScore).getRoot());
        } else if (playerTwoScore > playerOneScore) {
            JFXApplication.gameMenuController.setContent(new GameOverScene(player1, player2, MatchOverType.P2, playerOneScore, playerTwoScore).getRoot());
        } else {
            JFXApplication.gameMenuController.setContent(new GameOverScene(player1, player2, MatchOverType.DRAW, playerOneScore, playerTwoScore).getRoot());
        }
    }

}

// ----- Spacebox is het visuele element, Space zijn de coords en gebruikers container? -----

class SpaceBox extends HBox {

    public int x, y;

    public SpaceBox(int x, int y, int size) {
        this.x = x;
        this.y = y;

        Label spaceLabel = new Label("X: " + x + " | Y: " + y);
        spaceLabel.setStyle("-fx-text-fill: WHITE");

        Bloom bloom = new Bloom();
        bloom.setThreshold(.1);
        spaceLabel.setEffect(bloom);


        this.setMinSize(size, size);
        this.setPrefSize(size, size);
        this.setMaxSize(size, size);

        this.getChildren().add(spaceLabel);

        this.setAlignment(Pos.CENTER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceBox spaceBox = (SpaceBox) o;
        return x == spaceBox.x && y == spaceBox.y;
    }
}

class Space{
    public int x, y;

    private boolean isTaken;
    private int playerNumber;

    public Space(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Space(int x, int y, boolean isTaken, int playerNumber) {
        this(x, y);
        this.isTaken = isTaken;
        this.playerNumber = playerNumber;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void take(int playerNumber) {
        isTaken = true;
        this.playerNumber = playerNumber;
    }

    public void deselect() {
        isTaken = false;
        this.playerNumber = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Space space = (Space) o;
        return x == space.x && y == space.y;
    }

}

