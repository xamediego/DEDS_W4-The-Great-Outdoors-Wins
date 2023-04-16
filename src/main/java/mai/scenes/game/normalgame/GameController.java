package mai.scenes.game.normalgame;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import mai.audio.ButtonAudio;
import mai.audio.Sound;
import mai.data.User;
import mai.datastructs.Stapel;
import mai.enums.ButtonType;
import mai.enums.GameOverType;
import mai.exceptions.UnderflowException;
import mai.scenes.game.Parts.UserInfoBox;
import mai.scenes.game.logic.*;
import mai.scenes.gamemenu.GameMenuController;
import mai.scenes.gameover.GameOverData;
import mai.scenes.abstractscene.AbstractController;
import mai.audio.SoundPlayer;

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

    UserInfoBox horizontalPlayerOneInfo;
    UserInfoBox horizontalPlayerTwoInfo;

    @FXML
    private VBox HorizontalPlayerOneInfoContainer;
    @FXML
    private VBox HorizontalPlayerTwoInfoContainer;

    private final GameMenuController gameMenuController;

    public GameController(GameData gameData, int spaceMinSize, int spaceMaxSize, GameMenuController gameMenuController) {
        this.gameData = gameData;
        this.spaceMaxSize = spaceMaxSize;
        this.gameMenuController = gameMenuController;
        this.gameGeschiedenis = new Stapel<>();
        this.spaceMinSize = spaceMinSize;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controllerResetTurnButton = resetTurnButton;

        SoundPlayer.playAudioFile(Sound.SUMMON.getAudio());
        configButtons();

        horizontalPlayerOneInfo = new UserInfoBox(gameData.getPlayer1(), gameData.getPlayerOneScore(), "Hoodie",60);
        horizontalPlayerTwoInfo = new UserInfoBox(gameData.getPlayer2(), gameData.getPlayerTwoScore(), "Baggy Sweater",60);

        HorizontalPlayerOneInfoContainer.getChildren().add(horizontalPlayerOneInfo);
        HorizontalPlayerTwoInfoContainer.getChildren().add(horizontalPlayerTwoInfo);

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
        setSelectAble(gameData.gameBoard.getPlayerMoves(gameData.currentPlayer.getPlayerNumber()));
    }

    protected boolean checkGameConditions(int newP) {
        return gameData.gameBoard.checkBoard(newP);
    }

    protected void addGameHistory(GameData data) {
        data.currentPlayer = gameData.currentPlayer;
        gameGeschiedenis.push(data);
    }

    // ----- make the tiles for the current player selectable -----

    private void setSelectAble(Stapel<Space> selectAbles) {
        while (!selectAbles.isEmpty()){
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

    protected void removeSelectAble(Stapel<Space> removeAbles) {
        while (!removeAbles.isEmpty()){
            try {
                deselectBlock(removeAbles.pop());
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

    // ----- setting selectable tiles -----

    private Space selected;

    private EventHandler<? super MouseEvent> showPossible() {
        return (EventHandler<MouseEvent>) event -> {

            SpaceBox space = (SpaceBox) event.getSource();
            if (selected != null && (selected.y == space.y && selected.x == space.x)) {
                cancelAttack(space);
            } else {
                showPossibleAttacks(space);
            }
        };
    }

    private void cancelAttack(SpaceBox space){
        SoundPlayer.playAudioFile(ButtonAudio.CANCEL.getAudio());

        space.getStyleClass().clear();
        deselectActiveSelection(gameData.gameBoard.getBord()[space.x][space.y]);

        selected = null;
    }

    private void showPossibleAttacks(SpaceBox space) {
        if (selected != null) deselectActiveSelection(selected);

        SoundPlayer.playAudioFile(ButtonAudio.OK.getAudio());

        space.getStyleClass().add(ButtonType.SELECT.getType());
        selected = gameData.gameBoard.getBord()[space.x][space.y];

        AttackVectors attackVectors = gameData.gameBoard.getPossibleAttackSquare(new Space(space.x, space.y), gameData.currentPlayer.getRange(), gameData.currentPlayer.getAttackDropOff());

        showShortRangeAttack(attackVectors.possibleOneRangeAttackVectors(), space);
        showLongRangeAttack(attackVectors.possibleTwoRangeAttackVectors(), space);
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

    private void deselectActiveSelection(Space selectedSpace) {
        bordRows[selectedSpace.y].getChildren().get(selectedSpace.x).getStyleClass().clear();
        bordRows[selectedSpace.y].getChildren().get(selectedSpace.x).getStyleClass().add(ButtonType.SELECT.getType());

        Stapel<Space> deselectAbles = gameData.gameBoard.getDeselect(selectedSpace, 3);

        removeSelectAble(deselectAbles);
    }

    // ----- movement methods -----
    public EventHandler<? super MouseEvent> moveEvent(Space origin) {
        return (EventHandler<MouseEvent>) event -> {
            SoundPlayer.playAudioFile(ButtonAudio.OK.getAudio());

            selected = null;
            SpaceBox selected = (SpaceBox) event.getSource();

            bordRows[selected.y].getChildren().get(selected.x).setOnMouseEntered(null);

            move(origin, gameData.gameBoard.getBord()[selected.x][selected.y], gameData.currentPlayer, gameData.currentPlayer.getAttackDropOff(), gameData.currentPlayer.getRange());
        };
    }

    public void move(Space origin, Space selected, User currentUser, int attackDropOff, int range) {
        Stapel<Space> previousSelectables = gameData.gameBoard.getPlayerMoves(gameData.currentPlayer.getPlayerNumber());

        int xDif = gameData.gameBoard.getDis(origin.x, selected.x);
        int yDif = gameData.gameBoard.getDis(origin.y, selected.y);

        deselectActiveSelection(gameData.gameBoard.getBord()[origin.x][origin.y]);

        if (xDif < attackDropOff && yDif < attackDropOff) {
            addPoint(1);
            shortRangeAttack(selected, currentUser);
        } else if (xDif < range && yDif < range) {
            longRangeAttack(origin, selected, currentUser);
        }

        removeSelectAble(previousSelectables);

        endPlayerMove();
    }

    public void shortRangeAttack(Space select, User currentUser) {
        SpaceBox newSpace = (SpaceBox) bordRows[select.y].getChildren().get(select.x);

        newSpace.getStyleClass().clear();

        setInfected(select, currentUser.getPlayerNumber());

        gameData.gameBoard.getBord()[select.x][select.y].take(currentUser.getPlayerNumber());

        setColour(newSpace, currentUser.getPlayerColour());
        setSpaceLabel(currentUser.getPlayerNumber(), newSpace);
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

        removeSpaceLabel(oldSpace);
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
        horizontalPlayerOneInfo.getScoreLabel().setText(String.valueOf(gameData.getPlayerOneScore()));
        horizontalPlayerTwoInfo.getScoreLabel().setText(String.valueOf(gameData.getPlayerTwoScore()));
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
            setTurnGlow(1);
            setResetButtonActive(true);
        } else {
            setTurnGlow(2);
            setResetButtonActive(false);
        }
    }

    // ----- set or removes a glow when it's one player's turn or not -----

    protected void setTurnGlow(int playerNumber) {
        if (playerNumber == 1) {
            addTurnGlow(horizontalPlayerOneInfo.getPlayerLabel(), horizontalPlayerOneInfo.getAvatarBox().getAvatarCircle());
            removeTurnGlow(horizontalPlayerTwoInfo.getPlayerLabel(), horizontalPlayerTwoInfo.getAvatarBox().getAvatarCircle());
        } else {
            addTurnGlow(horizontalPlayerTwoInfo.getPlayerLabel(), horizontalPlayerTwoInfo.getAvatarBox().getAvatarCircle());
            removeTurnGlow(horizontalPlayerOneInfo.getPlayerLabel(), horizontalPlayerOneInfo.getAvatarBox().getAvatarCircle());
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
            SoundPlayer.playAudioFile(ButtonAudio.OK.getAudio());

            resetButtonBox.getChildren().clear();

            removeSelectAble(gameData.gameBoard.getPlayerMoves(gameData.currentPlayer.getPlayerNumber()));

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
        SoundPlayer.playAudioFile(ButtonAudio.OK.getAudio());

        if(gameData.currentPlayer.getPlayerNumber() == 1){
            endGame(1,2);
        } else {
            endGame(2,1);
        }
    }

    protected void endGame(int oldP, int newP) {
        if (gameData.getPlayerOneScore() > gameData.getPlayerTwoScore()) {
            user1Win();
        } else if (gameData.getPlayerTwoScore() > gameData.getPlayerOneScore()) {
            user2Win();
        } else {
            if (gameData.gameBoard.checkBoard(newP)) {
                if(oldP == 1){
                    user1Win();
                } else if(oldP == 2) {
                    user2Win();
                }
            } else {
                draw();
            }
        }
    }

    private void user1Win(){
        GameOverData gameOverData = new GameOverData(GameOverType.P1, gameData.getPlayer1(), gameData.getPlayer2(), gameData.getPlayerOneScore(), gameData.getPlayerTwoScore(), gameData.getTurnNumber());
        this.gameMenuController.setGameOverScreen(gameOverData);
    }

    private void user2Win(){
        GameOverData gameOverData = new GameOverData(GameOverType.P2, gameData.getPlayer1(), gameData.getPlayer2(), gameData.getPlayerOneScore(), gameData.getPlayerTwoScore(), gameData.getTurnNumber());
        this.gameMenuController.setGameOverScreen(gameOverData);
    }

    private void draw(){
        GameOverData gameOverData = new GameOverData(GameOverType.DRAW, gameData.getPlayer1(), gameData.getPlayer2(), gameData.getPlayerOneScore(), gameData.getPlayerTwoScore(), gameData.getTurnNumber());
        this.gameMenuController.setGameOverScreen(gameOverData);
    }


    // ----- for adding sounds? -----

    private EventHandler<? super MouseEvent> select() {
        return (EventHandler<MouseEvent>) event -> SoundPlayer.playAudioFile(ButtonAudio.SELECT.getAudio());
    }

    private EventHandler<? super MouseEvent> move() {
        return (EventHandler<MouseEvent>) event -> SoundPlayer.playAudioFile(ButtonAudio.MOVE.getAudio());
    }

}


