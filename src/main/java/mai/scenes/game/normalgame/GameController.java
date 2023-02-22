package mai.scenes.game.normalgame;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import mai.audio.MenuAudio;
import mai.data.User;
import mai.datastructs.Stapel;
import mai.enums.ButtonType;
import mai.enums.FXMLPart;
import mai.enums.MatchOverType;
import mai.enums.Sound;
import mai.exceptions.UnderflowException;
import mai.scenes.game.logic.*;
import mai.scenes.gameover.GameOverController;
import mai.scenes.gameover.GameOverScene;
import mai.scenes.test.AbstractController;
import mai.service.AudioPlayer;

import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController extends AbstractController implements Initializable {

    // ----- Game Data -----

    private final int spaceSize;
    private final Stapel<GameData> gameGeschiedenis;
    public HBox[] bordRows;

    //move gameData to stack?
    public GameData gameData;
    public GameData oldGameData;

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
        AudioPlayer.playAudioFile(new File(Sound.SUMMON.getAudio()));
        configButtons();

        configPlayer1(gameData.getPlayer1());
        configPlayer2(gameData.getPlayer2());

        configBoard(spaceSize);
        setInitialTurn();

        setOldGameData();
    }

    private void configButtons() {
        gameOverButton.setOnMouseEntered(select());
        resetTurnButton.setOnMouseEntered(select());
    }

    // ----- methods when player ends his move -----

    protected void endTurn() {
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

    public void endPlayerMove() {
        removeSelectAble(gameData.currentPlayer.getPlayerNumber());

        int oldP;

        if (gameData.currentPlayer.getPlayerNumber() == 1) {
            gameData.player1Finished = true;
            gameData.currentPlayer = gameData.getPlayer2();

            oldP = 1;
        } else {
            oldP = 2;

            gameData.player2Finished = true;
            gameData.currentPlayer = gameData.getPlayer1();
        }

        if (checkGameConditions(oldP)) {
            endGame(oldP);
        } else {
            if (gameData.player1Finished && gameData.player2Finished) endTurn();
            setNewPlayerMove();
        }
    }

    protected void setNewPlayerMove() {
        setTurnGlow(gameData.currentPlayer.getPlayerNumber());

        setCurrentPlayer();
        setSelectAble();
    }

    protected boolean checkGameConditions(int nextPlayer) {
        return gameData.gameBoard.checkBoard(nextPlayer);
    }

    private void addGameHistory(GameData data) {
        data.currentPlayer = gameData.currentPlayer;
        gameGeschiedenis.push(data);
    }

    // ----- make the tiles for the current player selectable -----

    private void setSelectAble() {
        Stapel<Space> selectAbles = gameData.gameBoard.getPlayerMoves(gameData.currentPlayer.getPlayerNumber());

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

    protected void removeSelectAble(int playerNumber) {
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
                AudioPlayer.playAudioFile(MenuAudio.CANCEL_AUDIO);

                space.getStyleClass().clear();
                deselect(new Space(space.x, space.y));
                selected = null;
            } else {
                if (selected != null) deselect(selected);
                AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

                space.getStyleClass().add("spaceSelected");
                selectedSpaceBox = space;

                selected = new Space(space.x, space.y);

                AttackVectors attackVectors = gameData.gameBoard.getPossibleAttackSquare(new Space(space.x, space.y), 3, 2, gameData.currentPlayer.getPlayerNumber());

                showShortRangeAttack(attackVectors.possibleOneRangeAttackVectors().getSize(), attackVectors.possibleOneRangeAttackVectors());
                showLongRangeAttack(attackVectors.possibleTwoRangeAttackVectors().getSize(), attackVectors.possibleTwoRangeAttackVectors(), space);
            }
        };
    }

    private void showShortRangeAttack(int size, Stapel<Space> attackVectors) {
        for (int i = 0; i < size; i++) {
            try {
                showPossibleBlock(attackVectors.pop(), ButtonType.SHORTRANGE.getType(), shortRangeEventAttack());
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void showLongRangeAttack(int size, Stapel<Space> attackVectors, SpaceBox origin) {
        for (int i = 0; i < size; i++) {
            try {
                showPossibleBlock(attackVectors.pop(), ButtonType.LONGRANGE.getType(), longRangeAttackEvent(origin));
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void showPossibleBlock(Space space, String classStyle, EventHandler<? super MouseEvent> mouseEvent) {
        bordRows[space.y].getChildren().get(space.x).setOnMouseClicked(mouseEvent);
        bordRows[space.y].getChildren().get(space.x).setOnMouseEntered(move());

        bordRows[space.y].getChildren().get(space.x).getStyleClass().clear();
        bordRows[space.y].getChildren().get(space.x).getStyleClass().add(classStyle);
    }

    private void deselect(Space selectedSpace) {
        System.out.println("X: " + selectedSpace.x + " | Y: " + selectedSpace.y + " | P: " + gameData.currentPlayer.getPlayerNumber() + " | DESELECT");
        selectedSpaceBox.getStyleClass().clear();
        selectedSpaceBox.getStyleClass().add("spaceSelect");

        Stapel<Space> deselectAbles = gameData.gameBoard.getDeselect(selectedSpace, 3);

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
        bordRows[space.y].getChildren().get(space.x).setOnMouseEntered(null);
    }

    // ----- movement methods -----

    public EventHandler<? super MouseEvent> shortRangeEventAttack() {
        return (EventHandler<MouseEvent>) event -> {
            AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

            SpaceBox space = (SpaceBox) event.getSource();

            deselect(selected);

            shortRangeAttack(gameData.gameBoard.getBord()[space.x][space.y]);
        };
    }

    public void shortRangeAttack(Space select){
        SpaceBox newSpace = (SpaceBox) bordRows[select.y].getChildren().get(select.x);

        newSpace.getStyleClass().clear();

        Stapel<Space> a = gameData.gameBoard.getSquare(select, 1);

        int size = a.getSize();

        for (int i = 0; i < size; i++) {
            try {
                Space t = a.pop();
                if (t.getPlayerNumber() != 0 && t.getPlayerNumber() != gameData.currentPlayer.getPlayerNumber()) {
                    gameData.gameBoard.getBord()[t.x][t.y].take(gameData.currentPlayer.getPlayerNumber());
                    setColour((SpaceBox) bordRows[t.y].getChildren().get(t.x), gameData.currentPlayer.getPlayerColour());
                    setSpaceLabel(gameData.currentPlayer.getPlayerNumber() , (SpaceBox) bordRows[t.y].getChildren().get(t.x));
                }
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        gameData.gameBoard.getBord()[select.x][select.y].take(gameData.currentPlayer.getPlayerNumber());

        setColour(newSpace, gameData.currentPlayer.getPlayerColour());
        setSpaceLabel(gameData.currentPlayer.getPlayerNumber(), newSpace);

        addPoint();

        endPlayerMove();
    }

    public EventHandler<? super MouseEvent> longRangeAttackEvent(SpaceBox oldSpace) {
        return (EventHandler<MouseEvent>) event -> {
            AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

            SpaceBox space = (SpaceBox) event.getSource();

            space.getStyleClass().clear();

            deselect(gameData.gameBoard.getBord()[oldSpace.x][oldSpace.y]);

            longRangeAttack(gameData.gameBoard.getBord()[oldSpace.x][oldSpace.y], gameData.gameBoard.getBord()[space.x][space.y]);
        };
    }

    public void longRangeAttack(Space origin, Space select){
        SpaceBox oldSpace = (SpaceBox) bordRows[origin.y].getChildren().get(origin.x);
        SpaceBox newSpace = (SpaceBox) bordRows[select.y].getChildren().get(select.x);

        Stapel<Space> a = gameData.gameBoard.getSquare(select, 1);

        int size = a.getSize();

        for (int i = 0; i < size; i++) {
            try {
                Space t = a.pop();
                if (t.getPlayerNumber() != 0 && t.getPlayerNumber() != gameData.currentPlayer.getPlayerNumber()) {
                    gameData.gameBoard.getBord()[t.x][t.y].take(gameData.currentPlayer.getPlayerNumber());
                    setColour((SpaceBox) bordRows[t.y].getChildren().get(t.x), gameData.currentPlayer.getPlayerColour());
                    setSpaceLabel(gameData.currentPlayer.getPlayerNumber() , (SpaceBox) bordRows[t.y].getChildren().get(t.x));
                }
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        gameData.gameBoard.getBord()[select.x][select.y].take(gameData.currentPlayer.getPlayerNumber());
        gameData.gameBoard.getBord()[oldSpace.x][oldSpace.y].deselect();

        setColour(newSpace, gameData.currentPlayer.getPlayerColour());
        setSpaceLabel(gameData.currentPlayer.getPlayerNumber(), newSpace);

        removeColour(oldSpace);
        removeSpaceLabel(oldSpace);

        endPlayerMove();
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

    private void configPlayer1(User user) {
        configAvatar(user, player1Avatar, player1AvatarCircle);
        configLabel(user, player1Label);
    }

    private void configPlayer2(User user) {
        configAvatar(user, player2Avatar, player2AvatarCircle);
        configLabel(user, player2Label);
    }

    // ----- configuring initial avatars -----

    private void configAvatar(User user, ImageView avatarView, Circle playerAvatarCircle) {
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

    private void configLabel(User user, Label label) {
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
                        setSpaceLabel(1, spaceBox);
                    } else {
                        setColour(spaceBox, data.getPlayer2().getPlayerColour());
                        setSpaceLabel(2, spaceBox);
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

    private void setSpaceLabel(int playerNumber, SpaceBox spaceBox) {
        if (playerNumber == 1) {
            spaceBox.setText("H");
        } else {
            spaceBox.setText("B");
        }
    }

    private void removeSpaceLabel(SpaceBox spaceBox) {
        spaceBox.setText("");
    }

    private void setColour(SpaceBox spaceBox, String color) {
        System.out.println("X: " + spaceBox.x + " | Y: " + spaceBox.y + " |  | COLOR");
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

    public void setInitialTurn() {
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

    protected void setTurnInfo() {
        turnInfo.setText("Turn " + gameData.getTurnNumber() + " | ");
    }

    protected void setCurrentPlayer() {
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

    protected void setTurnGlow(int playerNumber) {
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
            AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

            deselectSelectAble(gameData.gameBoard.getPlayerMoves(gameData.currentPlayer.getPlayerNumber()));

            resetTwistedGame(gameGeschiedenis.pop());
        }
    }

    private void resetTwistedGame(GameData data) {
        gameData = data;
        drawBoard(data, 75);

        setOldGameData();

        setTurnInfo();
        updatePointLabel();

        setNewPlayerMove();
    }

    protected void setResetButtonActive(boolean active) {
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
    private void endGameEvent(){
        AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);
        if(gameData.getPlayerOneScore() > gameData.getPlayerTwoScore()){
            endGame(1);
        } else if(gameData.getPlayerTwoScore() > gameData.getPlayerOneScore()){
            endGame(2);
        } else {
            endGame(0);
        }
    }

    protected void endGame(int winner) {
        FXMLPart gameOver = FXMLPart.GAMEOVER;

        if (winner == 1) {
            GameOverController gameOverController = new GameOverController(gameData.getPlayer1(), gameData.getPlayer2(), MatchOverType.P1, gameData.getPlayerOneScore(), gameData.getPlayerTwoScore());
            JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, gameOver).getRoot());
        } else if (winner == 2) {
            GameOverController gameOverController = new GameOverController(gameData.getPlayer1(), gameData.getPlayer2(), MatchOverType.P2, gameData.getPlayerOneScore(), gameData.getPlayerTwoScore());
            JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, gameOver).getRoot());
        } else {
            GameOverController gameOverController = new GameOverController(gameData.getPlayer1(), gameData.getPlayer2(), MatchOverType.DRAW, gameData.getPlayerOneScore(), gameData.getPlayerTwoScore());
            JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, gameOver).getRoot());
        }
    }

    // ----- for adding sounds? -----

    private EventHandler<? super MouseEvent> select() {
        return (EventHandler<MouseEvent>) event -> AudioPlayer.playAudioFile(MenuAudio.SELECT_AUDIO);
    }

    private EventHandler<? super MouseEvent> move() {
        return (EventHandler<MouseEvent>) event -> AudioPlayer.playAudioFile(MenuAudio.MOVE_AUDIO);
    }
}


