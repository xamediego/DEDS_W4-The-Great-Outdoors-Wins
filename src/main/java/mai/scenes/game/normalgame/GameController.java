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
import mai.enums.GameOverType;
import mai.exceptions.UnderflowException;
import mai.scenes.game.logic.*;
import mai.scenes.gameover.GameOverController;
import mai.scenes.gameover.GameOverScene;
import mai.scenes.test.AbstractController;
import mai.audio.AudioPlayer;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController extends AbstractController implements Initializable {

    // ----- Game Data -----

    private final int spaceMinSize, spaceMaxSize;
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

    private Button controllerResetTurnButton;
    @FXML
    private HBox resetButtonBox;

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

    public GameController(GameData gameData, int spaceMinSize, int spaceMaxSize) {
        this.gameData = gameData;
        this.spaceMaxSize = spaceMaxSize;
        this.gameGeschiedenis = new Stapel<>();
        this.spaceMinSize = spaceMinSize;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controllerResetTurnButton = resetTurnButton;

        AudioPlayer.playAudioFile(MenuAudio.SUMMON);
        configButtons();

        configPlayer1(gameData.getPlayer1());
        configPlayer2(gameData.getPlayer2());

        configBoard();
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

        setTurnInfo();
        setOldGameData();
    }

    private void setOldGameData() {
        Space[][] bord = gameData.gameBoard.copyBord();
        GameBoard oldGameBoard = new GameBoard(gameData.gameBoard.xGroote, gameData.gameBoard.yGroote, bord);

        oldGameData = new GameData(gameData.getPlayerOneScore(), gameData.getPlayerTwoScore(), gameData.getTurnNumber(), gameData.getPlayer1(), gameData.getPlayer2(), oldGameBoard);
    }

    public void endPlayerMove() {
        removeSelectAble(gameData.currentPlayer.getPlayerNumber());

        int oldP, newP;

        if (gameData.currentPlayer.getPlayerNumber() == 1) {
            gameData.player1Finished = true;
            gameData.currentPlayer = gameData.getPlayer2();

            newP = 2;
            oldP = 1;
        } else {
            oldP = 2;
            newP = 1;

            gameData.player2Finished = true;
            gameData.currentPlayer = gameData.getPlayer1();
        }

        if (checkGameConditions(newP)) {
            endGame(oldP, newP);
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

    protected boolean checkGameConditions(int newP) {
        return gameData.gameBoard.checkBoard(newP);
    }

    protected void addGameHistory(GameData data) {
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

    private EventHandler<? super MouseEvent> showPossible() {
        return (EventHandler<MouseEvent>) event -> {
            SpaceBox space = (SpaceBox) event.getSource();
            if (selected != null && (selected.y == space.y && selected.x == space.x)) {
                AudioPlayer.playAudioFile(MenuAudio.CANCEL_AUDIO);

                space.getStyleClass().clear();
                deselect(gameData.gameBoard.getBord()[space.x][space.y]);

                selected = null;
            } else {
                if (selected != null) deselect(selected);
                AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

                space.getStyleClass().add("spaceSelected");
                selected = gameData.gameBoard.getBord()[space.x][space.y];

                AttackVectors attackVectors = gameData.gameBoard.getPossibleAttackSquare(new Space(space.x, space.y), gameData.currentPlayer.getRange(), gameData.currentPlayer.getAttackDropOff());

                showShortRangeAttack(attackVectors.possibleOneRangeAttackVectors(), space);
                showLongRangeAttack(attackVectors.possibleTwoRangeAttackVectors(), space);
            }
        };
    }

    private void showShortRangeAttack(Stapel<Space> attackVectors, SpaceBox origin) {
        while (!attackVectors.isEmpty()) {
            try {
                showPossibleBlock(attackVectors.pop(), ButtonType.SHORTRANGE.getType(), moveEvent(gameData.gameBoard.getBord()[origin.x][origin.y]));
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void showLongRangeAttack(Stapel<Space> attackVectors, SpaceBox origin) {
        while (!attackVectors.isEmpty()) {
            try {
                showPossibleBlock(attackVectors.pop(), ButtonType.LONGRANGE.getType(), moveEvent(gameData.gameBoard.getBord()[origin.x][origin.y]));
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
        bordRows[selectedSpace.y].getChildren().get(selectedSpace.x).getStyleClass().clear();
        bordRows[selectedSpace.y].getChildren().get(selectedSpace.x).getStyleClass().add("spaceSelect");

        Stapel<Space> deselectAbles = gameData.gameBoard.getDeselect(selectedSpace, 3);

        deselectSelectAble(deselectAbles);
    }

    private void deselectSelectAble(Stapel<Space> deselectAbles) {
        while (!deselectAbles.isEmpty()) {
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
    public EventHandler<? super MouseEvent> moveEvent(Space origin) {
        return (EventHandler<MouseEvent>) event -> {
            AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

            selected = null;
            SpaceBox selected = (SpaceBox) event.getSource();

            bordRows[selected.y].getChildren().get(selected.x).setOnMouseEntered(null);

            move(origin, gameData.gameBoard.getBord()[selected.x][selected.y], gameData.currentPlayer, gameData.currentPlayer.getAttackDropOff(), gameData.currentPlayer.getRange());
        };
    }

    public void move(Space origin, Space selected, User currentUser, int attackDropOff, int range) {
        int xDif = gameData.gameBoard.getDis(origin.x, selected.x);
        int yDif = gameData.gameBoard.getDis(origin.y, selected.y);

        if (xDif < attackDropOff && yDif < attackDropOff) {
            deselect(gameData.gameBoard.getBord()[origin.x][origin.y]);
            addPoint(1);
            shortRangeAttack(selected, currentUser);
        } else if (xDif < range && yDif < range) {
            longRangeAttack(origin, selected, currentUser);
            deselect(gameData.gameBoard.getBord()[origin.x][origin.y]);
        }

    }

    public void shortRangeAttack(Space select, User currentUser) {
        SpaceBox newSpace = (SpaceBox) bordRows[select.y].getChildren().get(select.x);

        newSpace.getStyleClass().clear();

        setInfected(select, currentUser.getPlayerNumber());

        gameData.gameBoard.getBord()[select.x][select.y].take(currentUser.getPlayerNumber());

        setColour(newSpace, currentUser.getPlayerColour());
        setSpaceLabel(currentUser.getPlayerNumber(), newSpace);

        endPlayerMove();
    }

    public void longRangeAttack(Space origin, Space select, User currentUser) {
        SpaceBox oldSpace = (SpaceBox) bordRows[origin.y].getChildren().get(origin.x);
        SpaceBox newSpace = (SpaceBox) bordRows[select.y].getChildren().get(select.x);

        removeColour((SpaceBox) bordRows[origin.y].getChildren().get(origin.x));
        gameData.gameBoard.getBord()[origin.x][origin.y].deselect();

        setInfected(select, currentUser.getPlayerNumber());

        gameData.gameBoard.getBord()[select.x][select.y].take(currentUser.getPlayerNumber());
        gameData.gameBoard.getBord()[oldSpace.x][oldSpace.y].deselect();

        setColour(newSpace, currentUser.getPlayerColour());
        setSpaceLabel(currentUser.getPlayerNumber(), newSpace);

        removeColour(oldSpace);
        removeSpaceLabel(oldSpace);

        endPlayerMove();
    }

    private void setInfected(Space select, int playerNumber) {
        Stapel<Space> a = gameData.gameBoard.getInfected(select, playerNumber);

        while (!a.isEmpty()) {
            try {
                Space t = a.pop();
                gameData.gameBoard.setInfected(t, playerNumber);

                addPoint(1);
                removePoint(1);

                setColour((SpaceBox) bordRows[t.y].getChildren().get(t.x), gameData.currentPlayer.getPlayerColour());
                setSpaceLabel(gameData.currentPlayer.getPlayerNumber(), (SpaceBox) bordRows[t.y].getChildren().get(t.x));
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void addPoint(int points) {
        if (gameData.currentPlayer.getPlayerNumber() == 1) {
            gameData.increasePlayerOneScore(points);
        } else {
            gameData.increasePlayerTwoScore(points);
        }

        updatePointLabel();
    }

    private void removePoint(int points) {
        if (gameData.currentPlayer.getPlayerNumber() == 1) {
            gameData.decreasePlayerTwoScore(points);
        } else {
            gameData.decreasePlayerOneScore(points);
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

    private void configBoard() {
        gameData.gameBoard.setInitialOccupied(2);
        drawBoard(gameData);
    }

    private void drawBoard(GameData data) {
        bordRows = new HBox[data.gameBoard.yGroote];

        for (int y = 0; y < data.gameBoard.yGroote; y++) {
            HBox row = new HBox();
            for (int x = 0; x < data.gameBoard.xGroote; x++) {
                SpaceBox spaceBox = new SpaceBox(x, y, spaceMinSize, spaceMaxSize);

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
        } else {
            space.setStyle(
                    "-fx-border-width: 0 0 0 0;\n" +
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

            resetButtonBox.getChildren().clear();

            deselectSelectAble(gameData.gameBoard.getPlayerMoves(gameData.currentPlayer.getPlayerNumber()));

            resetTwistedGame(gameGeschiedenis.pop());
        }
    }

    private void resetTwistedGame(GameData data) {
        gameData = data;
        drawBoard(data);

        setOldGameData();

        setTurnInfo();
        updatePointLabel();

        gameData.currentPlayer = gameData.getPlayer1();
        setNewPlayerMove();
    }

    protected void setResetButtonActive(boolean active) {
        if (!gameGeschiedenis.isEmpty()) {
            if(active && resetButtonBox.getChildren().isEmpty()){
                resetButtonBox.getChildren().add(controllerResetTurnButton);
            } else {
                resetButtonBox.getChildren().clear();
            }
        } else {
            resetButtonBox.getChildren().clear();
        }
    }

    // ----- event that runs when the game has ended -----

    @FXML
    private void endGameEvent() {
        AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

        if(gameData.currentPlayer.getPlayerNumber() == 1){
            endGame(1,2);
        } else {
            endGame(2,1);
        }
    }

    protected void endGame(int oldP, int newP) {
        if (gameData.getPlayerOneScore() > gameData.getPlayerTwoScore()) {
            spelerEenWin();
        } else if (gameData.getPlayerTwoScore() > gameData.getPlayerOneScore()) {
            spelerTweeWin();
        } else {
            if (gameData.gameBoard.checkBoard(newP)) {
                if(oldP == 1){
                    spelerEenWin();
                } else if(oldP == 2) {
                    spelerTweeWin();
                }
            } else {
                gelijkSpel();
            }
        }
    }

    private void spelerEenWin(){
        GameOverController gameOverController = new GameOverController(gameData.getPlayer1(), gameData.getPlayer2(), GameOverType.P1, gameData.getPlayerOneScore(), gameData.getPlayerTwoScore());
        JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, FXMLPart.GAMEOVER).getRoot());
    }

    private void spelerTweeWin(){
        GameOverController gameOverController = new GameOverController(gameData.getPlayer1(), gameData.getPlayer2(), GameOverType.P2, gameData.getPlayerOneScore(), gameData.getPlayerTwoScore());
        JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, FXMLPart.GAMEOVER).getRoot());
    }

    private void gelijkSpel(){
        GameOverController gameOverController = new GameOverController(gameData.getPlayer1(), gameData.getPlayer2(), GameOverType.DRAW, gameData.getPlayerOneScore(), gameData.getPlayerTwoScore());
        JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, FXMLPart.GAMEOVER).getRoot());
    }


    // ----- for adding sounds? -----

    private EventHandler<? super MouseEvent> select() {
        return (EventHandler<MouseEvent>) event -> AudioPlayer.playAudioFile(MenuAudio.SELECT_AUDIO);
    }

    private EventHandler<? super MouseEvent> move() {
        return (EventHandler<MouseEvent>) event -> AudioPlayer.playAudioFile(MenuAudio.MOVE_AUDIO);
    }
}


