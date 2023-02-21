package mai.scenes.game.normalgame;

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
import mai.enums.ButtonType;
import mai.enums.FXMLPart;
import mai.enums.MatchOverType;
import mai.exceptions.UnderflowException;
import mai.scenes.game.logic.*;
import mai.scenes.gameover.GameOverController;
import mai.scenes.gameover.GameOverScene;
import mai.scenes.test.AbstractController;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController extends AbstractController implements Initializable {

    // ----- Game Data -----

    private final int spaceSize;
    private final Stapel<GameData> gameGeschiedenis;
    private HBox[] bordRows;

    //move gameData to stack?
    private GameData gameData;
    private GameData oldGameData;

    // ----- Board Interface -----

    @FXML
    private HBox GameBoardContainer;

    @FXML
    private VBox bordColumnBox;

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

    @FXML
    private Button resetTurnButton;

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

    public GameController(GameData gameData, int spaceSize) {
        this.gameData = gameData;
        this.gameGeschiedenis = new Stapel<>();
        this.spaceSize = spaceSize;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configPlayer1(gameData.getPlayer1());
        configPlayer2(gameData.getPlayer2());

        configBoard(spaceSize);
        setInitialTurn();

        setSelectAble();

        setOldGameData();
    }

    // ----- methods when player ends his move -----

    private void endTurn() {
        gameData.increaseTurnNumber();
        gameData.player1Finished = false;
        gameData.player2Finished = false;

        addGameHistory(oldGameData);
        setTurnInfo();

        setOldGameData();
    }

    private void setOldGameData() {
        Space[][] bord = gameData.gameBoard.copyBord();
        GameBoard oldGameBoard = new GameBoard(7, 7, bord);

        oldGameData = new GameData(gameData.getPlayerOneScore(), gameData.getPlayerTwoScore(), gameData.getTurnNumber(), gameData.getPlayer1(), gameData.getPlayer2(), oldGameBoard);

    }

    private void endPlayerMove() {
        removeSelectAble(gameData.currentPlayer.getPlayerNumber());

        if (checkGameConditions()) {
            endGame();
        } else {
            if (gameData.currentPlayer.getPlayerNumber() == 1) {
                gameData.player1Finished = true;
                gameData.currentPlayer = gameData.getPlayer2();
            } else {
                gameData.player2Finished = true;
                gameData.currentPlayer = gameData.getPlayer1();
            }

            if (gameData.player1Finished && gameData.player2Finished) endTurn();


            setNewPlayerMove();
        }
    }

    private void setNewPlayerMove() {
        setTurnGlow(gameData.currentPlayer.getPlayerNumber());

        setCurrentPlayer();
        setSelectAble();
    }

    private boolean checkGameConditions() {
        for (int y = 0; y < gameData.gameBoard.yGroote; y++) {
            for (int x = 0; x < gameData.gameBoard.xGroote; x++) {
                if (!gameData.gameBoard.getBord()[x][y].isTaken()) return false;
            }
        }
        return true;
    }

    private void addGameHistory(GameData data) {
        data.currentPlayer = gameData.currentPlayer;

        System.out.println("ADD TURN: " + data.getTurnNumber() + " | SIZE: " + gameGeschiedenis.getSize());
        gameGeschiedenis.push(data);
        System.out.println("NEW SIZE: " + gameGeschiedenis.getSize());
    }

    // ----- make the tiles for the current player selectable -----

    private void setSelectAble() {
        Stapel<Space> selectAbles = gameData.gameBoard.getPlayerSpaces(gameData.currentPlayer.getPlayerNumber());

        int size = selectAbles.getSize();
        for (int i = 0; i < size; i++) {
            try {
                makeSelectAble(selectAbles.pop());
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeSelectAble(Space space) {
        bordRows[space.y].getChildren().get(space.x).getStyleClass().add(ButtonType.SELECT.getType());
        bordRows[space.y].getChildren().get(space.x).setOnMouseClicked(showPossible());
    }

    private void removeSelectAble(int playerNumber) {
        for (int y = 0; y < gameData.gameBoard.yGroote; y++) {
            for (int x = 0; x < gameData.gameBoard.xGroote; x++) {
                if (gameData.gameBoard.getBord()[x][y].getPlayerNumber() == playerNumber)
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
                deselect(new Space(space.x, space.y));
                selected = null;
            } else {
                if (selected != null) deselect(selected);

                space.getStyleClass().add("spaceSelected");
                selectedSpaceBox = space;

                selected = new Space(space.x, space.y);

                AttackVectors attackVectors = gameData.gameBoard.getPossibleAttackSquare(new Vector2D(space.x, space.y), 3, 2);

                showShortRangeAttack(attackVectors.possibleOneRangeAttackVectors().getSize(), attackVectors.possibleOneRangeAttackVectors());
                showLongRangeAttack(attackVectors.possibleTwoRangeAttackVectors().getSize(), attackVectors.possibleTwoRangeAttackVectors(), space);
            }
        };
    }

    private void showShortRangeAttack(int size, Stapel<Space> attackVectors) {
        for (int i = 0; i < size; i++) {
            try {
                showPossibleBlock(attackVectors.pop(), ButtonType.SHORTRANGE.getType(), shortRangeAttack());
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void showLongRangeAttack(int size, Stapel<Space> attackVectors, SpaceBox origin) {
        for (int i = 0; i < size; i++) {
            try {
                showPossibleBlock(attackVectors.pop(), ButtonType.LONGRANGE.getType(), longRangeAttack(origin));
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void showPossibleBlock(Space space, String classStyle, EventHandler<? super MouseEvent> mouseEvent) {
        bordRows[space.y].getChildren().get(space.x).setOnMouseClicked(mouseEvent);
        bordRows[space.y].getChildren().get(space.x).getStyleClass().clear();
        bordRows[space.y].getChildren().get(space.x).getStyleClass().add(classStyle);
    }

    private void deselect(Space selectedSpace) {
        selectedSpaceBox.getStyleClass().clear();
        selectedSpaceBox.getStyleClass().add("spaceSelect");

        Stapel<Space> deselectAbles = gameData.gameBoard.getDeselect(selectedSpace);

        deselectSelectAble(deselectAbles);
    }

    private void deselectSelectAble(Stapel<Space> deselectAbles) {
        int size = deselectAbles.getSize();

        for (int i = 0; i < size; i++) {
            try {
                deselectBlock(deselectAbles.pop());
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void deselectBlock(Space space) {
        bordRows[space.y].getChildren().get(space.x).getStyleClass().clear();
        bordRows[space.y].getChildren().get(space.x).setOnMouseClicked(null);
    }
    // ----- movement methods -----

    private EventHandler<? super MouseEvent> shortRangeAttack() {
        return (EventHandler<MouseEvent>) event -> {
            SpaceBox space = (SpaceBox) event.getSource();

            space.getStyleClass().clear();

            gameData.gameBoard.getBord()[space.x][space.y].take(gameData.currentPlayer.getPlayerNumber());

            setColour(space, gameData.currentPlayer.getPlayerColour());

            deselect(selected);

            addPoint();

            endPlayerMove();
        };
    }

    private EventHandler<? super MouseEvent> longRangeAttack(SpaceBox oldSpace) {
        return (EventHandler<MouseEvent>) event -> {
            SpaceBox space = (SpaceBox) event.getSource();

            space.getStyleClass().clear();

            gameData.gameBoard.getBord()[space.x][space.y].take(gameData.currentPlayer.getPlayerNumber());
            gameData.gameBoard.getBord()[oldSpace.x][oldSpace.y].deselect();

            setColour(space, gameData.currentPlayer.getPlayerColour());

            removeColour(oldSpace);

            deselect(selected);

            endPlayerMove();
        };
    }

    private void addPoint() {
        if (gameData.currentPlayer.getPlayerNumber() == 1) {
            gameData.increasePlayerOneScore(1);
        } else {
            gameData.increasePlayerTwoScore(1);
        }

        updatePointLabel();
    }

    private void updatePointLabel() {
        player1Score.setText(String.valueOf(gameData.getPlayerOneScore()));
        player2Score.setText(String.valueOf(gameData.getPlayerTwoScore()));
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

    private void configBoard(int blockSize) {
        gameData.gameBoard.setInitialOccupied(2);
        drawBoard(gameData, blockSize);
    }

    private void drawBoard(GameData data, int blockSize) {
        bordRows = new HBox[data.gameBoard.yGroote];

        for (int y = 0; y < data.gameBoard.yGroote; y++) {
            HBox row = new HBox();
            for (int x = 0; x < data.gameBoard.xGroote; x++) {
                SpaceBox spaceBox = new SpaceBox(x, y, blockSize);

                setBoxBorder(x, y, spaceBox);

                if (data.gameBoard.getBord()[x][y].isTaken()) {
                    if (data.gameBoard.getBord()[x][y].getPlayerNumber() == 1) {
                        setColour(spaceBox, data.getPlayer1().getPlayerColour());
                    } else {
                        setColour(spaceBox, data.getPlayer2().getPlayerColour());
                    }
                }

                row.getChildren().add(spaceBox);
            }
            bordRows[y] = row;
        }

        if (!bordColumnBox.getChildren().isEmpty()) bordColumnBox.getChildren().clear();

        bordColumnBox.getChildren().addAll(bordRows);
    }

    private void setBoxBorder(int x, int y, Node space) {
        if (gameData.gameBoard.xGroote - 1 != x && y != 0) {
            space.setStyle(
                    "-fx-border-width: 2 2 0 0;\n" +
                            "-fx-border-color: #242526;");
        } else if (gameData.gameBoard.xGroote - 1 != x) {
            space.setStyle(
                    "-fx-border-width: 0 2 0 0;\n" +
                            "-fx-border-color: #242526;");
        } else if (gameData.gameBoard.xGroote - 1 == x && y != 0) {
            space.setStyle(
                    "-fx-border-width: 2 0 0 0;\n" +
                            "-fx-border-color: #242526;");
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
            gameData.currentPlayer = gameData.getPlayer1();
            setResetButtonActive(true);
            setNewPlayerMove();
        } else {
            gameData.currentPlayer = gameData.getPlayer2();
            setResetButtonActive(false);
            setNewPlayerMove();
        }

        setTurnInfo();
    }

    private void setTurnInfo() {
        turnInfo.setText("Turn " + gameData.getTurnNumber() + " | ");
    }

    private void setCurrentPlayer() {
        currentPlayerLabel.setText(gameData.currentPlayer.getPlayerName());

        if (gameData.currentPlayer.getPlayerNumber() == 1) {
            addTurnGlow(player1Label, player1AvatarCircle);
            setResetButtonActive(true);
        } else {
            addTurnGlow(player2Label, player2AvatarCircle);
            setResetButtonActive(false);
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

    // ----- event that runs when the game turn goes back -----
    // pops the new turn data and peeks for the previous turn data
    @FXML
    private void resetTurn() throws UnderflowException {
        if (!gameGeschiedenis.isEmpty()) {
            System.out.println("RESET OLD SIZE: " + gameGeschiedenis.getSize());

            deselectSelectAble(gameData.gameBoard.getPlayerSpaces(gameData.currentPlayer.getPlayerNumber()));

            resetTwistedGame(gameGeschiedenis.pop());
        }
    }

    private void resetTwistedGame(GameData data) {
        System.out.println(data.toString());

        gameData = data;
        drawBoard(data, 75);

        setOldGameData();

        setTurnInfo();
        updatePointLabel();

        setNewPlayerMove();

        System.out.println("RESET NEW SIZE: " + gameGeschiedenis.getSize());
    }

    private void setResetButtonActive(boolean active) {
        if (!gameGeschiedenis.isEmpty()) {
            resetTurnButton.setVisible(active);
            resetTurnButton.setDisable(!active);
        } else {
            resetTurnButton.setVisible(false);
            resetTurnButton.setDisable(false);
        }
    }

    // ----- event that runs when the game has ended -----

    @FXML
    private void endGame() {
        FXMLPart gameOver = FXMLPart.GAMEOVER;

        if (gameData.getPlayerOneScore() > gameData.getPlayerTwoScore()) {
            GameOverController gameOverController = new GameOverController(gameData.getPlayer1(), gameData.getPlayer2(), MatchOverType.P1, gameData.getPlayerOneScore(), gameData.getPlayerTwoScore());
            JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, gameOver).getRoot());
        } else if (gameData.getPlayerTwoScore() > gameData.getPlayerOneScore()) {
            GameOverController gameOverController = new GameOverController(gameData.getPlayer1(), gameData.getPlayer2(), MatchOverType.P2, gameData.getPlayerOneScore(), gameData.getPlayerTwoScore());
            JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, gameOver).getRoot());
        } else {
            GameOverController gameOverController = new GameOverController(gameData.getPlayer1(), gameData.getPlayer2(), MatchOverType.DRAW, gameData.getPlayerOneScore(), gameData.getPlayerTwoScore());
            JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, gameOver).getRoot());
        }
    }

}

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


