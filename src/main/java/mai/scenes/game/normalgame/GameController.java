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
import mai.data.Speler;
import mai.datastructs.Stapel;
import mai.enums.ButtonType;
import mai.enums.FXMLPart;
import mai.enums.GameOverType;
import mai.exceptions.UnderflowException;
import mai.scenes.game.logic.*;
import mai.scenes.gameover.GameOverController;
import mai.scenes.gameover.GameOverScene;
import mai.scenes.sceneconstructor.AbstractController;
import mai.audio.AudioPlayer;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController extends AbstractController implements Initializable {

    // ----- Game Data -----

    private final int plekGroote;
    private final Stapel<GameData> gameGeschiedenis;
    public HBox[] bordRijen;

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

    public GameController(GameData gameData, int plekGroote) {
        this.gameData = gameData;
        this.gameGeschiedenis = new Stapel<>();
        this.plekGroote = plekGroote;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AudioPlayer.playAudioFile(MenuAudio.SUMMON);
        configButtons();

        configSpelerEen(gameData.getSpelerEen());
        configSpelerTwee(gameData.getSpelerTwee());

        configBord(plekGroote);
        setBeginBeurt();

        setOudeGameData();
    }

    private void configButtons() {
        gameOverButton.setOnMouseEntered(selecteerAudio());
        resetTurnButton.setOnMouseEntered(selecteerAudio());
    }

    // ----- methods when player ends his move -----

    protected void eindBeurt() {
        gameData.increaseTurnNumber();
        gameData.spelerEenKlaar = false;
        gameData.spelerTweeKlaar = false;

        setBeurtInfo();
        setOudeGameData();
    }

    private void setOudeGameData() {
        Plek[][] bord = gameData.gameBord.copyBord();
        GameBord oldGameBord = new GameBord(gameData.gameBord.xGroote, gameData.gameBord.yGroote, bord);

        oldGameData = new GameData(gameData.getSpelerEenScore(), gameData.getSpelerTweeScore(), gameData.getTurnNummer(), gameData.getSpelerEen(), gameData.getSpelerTwee(), oldGameBord);
    }

    public void endSpelerBeurt() {
        verwijderSelecteerBaar(gameData.huidigeSpeler.getSpelerNummer());

        int oldP, newP;

        if (gameData.huidigeSpeler.getSpelerNummer() == 1) {
            gameData.spelerEenKlaar = true;
            gameData.huidigeSpeler = gameData.getSpelerTwee();

            newP = 2;
            oldP = 1;
        } else {
            oldP = 2;
            newP = 1;

            gameData.spelerTweeKlaar = true;
            gameData.huidigeSpeler = gameData.getSpelerEen();
        }

        if (checkGameConditions(newP, oldP)) {
            stopGame(oldP, newP);
        } else {
            if (gameData.spelerEenKlaar && gameData.spelerTweeKlaar) eindBeurt();
            setNieuweSpelerBeurt();
        }
    }

    protected void setNieuweSpelerBeurt() {
        setTurnGlow(gameData.huidigeSpeler.getSpelerNummer());

        setHuigeBeurtSpeler();
        setSelecteerBaar();
    }

    protected boolean checkGameConditions(int newP, int oldP) {
        return gameData.gameBord.checkBoard(newP);
    }

    protected void voegGameGeschiedenisToe(GameData data) {
        data.huidigeSpeler = gameData.huidigeSpeler;
        gameGeschiedenis.push(data);
    }

    // ----- make the tiles for the current player selectable -----

    private void setSelecteerBaar() {
        Stapel<Plek> selectAbles = gameData.gameBord.getPlayerMoves(gameData.huidigeSpeler.getSpelerNummer());

        int size = selectAbles.getGroote();
        for (int i = 0; i < size; i++) {
            try {
                maakSelecteerBaar(selectAbles.pop());
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void maakSelecteerBaar(Plek plek) {
        bordRijen[plek.y].getChildren().get(plek.x).getStyleClass().add(ButtonType.SELECT.getType());
        bordRijen[plek.y].getChildren().get(plek.x).setOnMouseClicked(laatMogelijkHedenZien());
    }

    protected void verwijderSelecteerBaar(int spelerNummer) {
        for (int y = 0; y < gameData.gameBord.yGroote; y++) {
            for (int x = 0; x < gameData.gameBord.xGroote; x++) {
                if (gameData.gameBord.getBord()[x][y].getSpelerNummer() == spelerNummer)
                    bordRijen[y].getChildren().get(x).getStyleClass().clear();
                bordRijen[y].getChildren().get(x).setOnMouseClicked(null);
            }
        }
    }

    // ----- setting selectable tiles -----

    private Plek selected;

    private EventHandler<? super MouseEvent> laatMogelijkHedenZien() {
        return (EventHandler<MouseEvent>) event -> {
            PlekHBox space = (PlekHBox) event.getSource();
            if (selected != null && (selected.y == space.y && selected.x == space.x)) {
                AudioPlayer.playAudioFile(MenuAudio.CANCEL_AUDIO);

                space.getStyleClass().clear();
                deSelecteer(gameData.gameBord.getBord()[space.x][space.y]);

                selected = null;
            } else {
                if (selected != null) deSelecteer(selected);
                AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

                space.getStyleClass().add("spaceSelected");
                selected = gameData.gameBord.getBord()[space.x][space.y];

                AanvalsHoeken aanvalsHoeken = gameData.gameBord.getPossibleAttackSquare(new Plek(space.x, space.y), gameData.huidigeSpeler.getBereik(), gameData.huidigeSpeler.getMinBereik());

                laatKortBereikZien(aanvalsHoeken.mogelijkeKleinBereikAanval(), space);
                laatLangBereikZien(aanvalsHoeken.mogelijkeVerBereikAanval(), space);
            }
        };
    }

    private void laatKortBereikZien(Stapel<Plek> aanvalsHoeken, PlekHBox oorsprong) {
        while (!aanvalsHoeken.isLeeg()) {
            try {
                laatHuidigeBlokkenZien(aanvalsHoeken.pop(), ButtonType.SHORTRANGE.getType(), moveEvent(gameData.gameBord.getBord()[oorsprong.x][oorsprong.y]));
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void laatLangBereikZien(Stapel<Plek> aanvalsHoeken, PlekHBox oorsprong) {
        while (!aanvalsHoeken.isLeeg()) {
            try {
                laatHuidigeBlokkenZien(aanvalsHoeken.pop(), ButtonType.LONGRANGE.getType(), moveEvent(gameData.gameBord.getBord()[oorsprong.x][oorsprong.y]));
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void laatHuidigeBlokkenZien(Plek plek, String classStyle, EventHandler<? super MouseEvent> mouseEvent) {
        bordRijen[plek.y].getChildren().get(plek.x).setOnMouseClicked(mouseEvent);
        bordRijen[plek.y].getChildren().get(plek.x).setOnMouseEntered(beweeg());

        bordRijen[plek.y].getChildren().get(plek.x).getStyleClass().clear();
        bordRijen[plek.y].getChildren().get(plek.x).getStyleClass().add(classStyle);
    }

    private void deSelecteer(Plek selectedPlek) {
        bordRijen[selectedPlek.y].getChildren().get(selectedPlek.x).getStyleClass().clear();
        bordRijen[selectedPlek.y].getChildren().get(selectedPlek.x).getStyleClass().add("spaceSelect");

        Stapel<Plek> deselectAbles = gameData.gameBord.getDeselect(selectedPlek, 3);

        deselecteerHuidigeSelectie(deselectAbles);
    }

    private void deselecteerHuidigeSelectie(Stapel<Plek> deselectAbles) {
        while (!deselectAbles.isLeeg()) {
            try {
                deselecteerPlek(deselectAbles.pop());
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void deselecteerPlek(Plek plek) {
        bordRijen[plek.y].getChildren().get(plek.x).getStyleClass().clear();
        bordRijen[plek.y].getChildren().get(plek.x).setOnMouseClicked(null);
        bordRijen[plek.y].getChildren().get(plek.x).setOnMouseEntered(null);
    }

    // ----- movement methods -----
    public EventHandler<? super MouseEvent> moveEvent(Plek origin) {
        return (EventHandler<MouseEvent>) event -> {
            AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

            selected = null;
            PlekHBox selected = (PlekHBox) event.getSource();

            bordRijen[selected.y].getChildren().get(selected.x).setOnMouseEntered(null);

            beweeg(origin, gameData.gameBord.getBord()[selected.x][selected.y], gameData.huidigeSpeler, gameData.huidigeSpeler.getMinBereik(), gameData.huidigeSpeler.getBereik());
        };
    }

    public void beweeg(Plek origin, Plek selected, Speler currentSpeler, int attackDropOff, int range) {
        int xDif = gameData.gameBord.getDis(origin.x, selected.x);
        int yDif = gameData.gameBord.getDis(origin.y, selected.y);

        if (xDif < attackDropOff && yDif < attackDropOff) {
            deSelecteer(gameData.gameBord.getBord()[origin.x][origin.y]);
            voegPuntenToe(1);
            kleineAfstandsAanval(selected, currentSpeler);
        } else if (xDif < range && yDif < range) {
            langeAfstandsAanval(origin, selected, currentSpeler);
            deSelecteer(gameData.gameBord.getBord()[origin.x][origin.y]);
        }

    }

    public void kleineAfstandsAanval(Plek select, Speler currentSpeler) {
        PlekHBox newSpace = (PlekHBox) bordRijen[select.y].getChildren().get(select.x);

        newSpace.getStyleClass().clear();

        setInfected(select, currentSpeler.getSpelerNummer());

        gameData.gameBord.getBord()[select.x][select.y].take(currentSpeler.getSpelerNummer());

        setKleur(newSpace, currentSpeler.getSpelerKleur());
        setPlekLabel(currentSpeler.getSpelerNummer(), newSpace);

        endSpelerBeurt();
    }

    public void langeAfstandsAanval(Plek oorsprongPlek, Plek select, Speler currentSpeler) {
        PlekHBox oorsprong = (PlekHBox) bordRijen[oorsprongPlek.y].getChildren().get(oorsprongPlek.x);
        PlekHBox selectie = (PlekHBox) bordRijen[select.y].getChildren().get(select.x);

        verwijderKleur((PlekHBox) bordRijen[oorsprongPlek.y].getChildren().get(oorsprongPlek.x));
        gameData.gameBord.getBord()[oorsprongPlek.x][oorsprongPlek.y].deselect();

        setInfected(select, currentSpeler.getSpelerNummer());

        gameData.gameBord.getBord()[select.x][select.y].take(currentSpeler.getSpelerNummer());
        gameData.gameBord.getBord()[oorsprong.x][oorsprong.y].deselect();

        setKleur(selectie, currentSpeler.getSpelerKleur());
        setPlekLabel(currentSpeler.getSpelerNummer(), selectie);

        verwijderKleur(oorsprong);
        verwijderPlekLabel(oorsprong);

        endSpelerBeurt();
    }

    private void setInfected(Plek select, int spelerNummer) {
        Stapel<Plek> a = gameData.gameBord.getInfected(select, spelerNummer);

        while (!a.isLeeg()) {
            try {
                Plek t = a.pop();
                gameData.gameBord.setInfected(t, spelerNummer);

                voegPuntenToe(1);
                verwijderPunt(1);

                setKleur((PlekHBox) bordRijen[t.y].getChildren().get(t.x), gameData.huidigeSpeler.getSpelerKleur());
                setPlekLabel(gameData.huidigeSpeler.getSpelerNummer(), (PlekHBox) bordRijen[t.y].getChildren().get(t.x));
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    private void voegPuntenToe(int punten) {
        if (gameData.huidigeSpeler.getSpelerNummer() == 1) {
            gameData.verhoogSpelerEenPunten(punten);
        } else {
            gameData.verhoogSpelerTweePunten(punten);
        }

        updatePuntenLabel();
    }

    private void verwijderPunt(int punten) {
        if (gameData.huidigeSpeler.getSpelerNummer() == 1) {
            gameData.decreasePlayerTwoScore(punten);
        } else {
            gameData.decreasePlayerOneScore(punten);
        }

        updatePuntenLabel();
    }

    private void updatePuntenLabel() {
        player1Score.setText(String.valueOf(gameData.getSpelerEenScore()));
        player2Score.setText(String.valueOf(gameData.getSpelerTweeScore()));
    }

    // ----- configuring initial user details -----

    private void configSpelerEen(Speler speler) {
        configAvatar(speler, player1Avatar, player1AvatarCircle);
        configLabel(speler, player1Label);
    }

    private void configSpelerTwee(Speler speler) {
        configAvatar(speler, player2Avatar, player2AvatarCircle);
        configLabel(speler, player2Label);
    }

    // ----- configuring initial avatars -----

    private void configAvatar(Speler speler, ImageView avatarView, Circle playerAvatarCircle) {
        if (speler.getProfilePictureUrl() != null) {
            setAvatar(avatarView, new Image(speler.getProfilePictureUrl()), playerAvatarCircle, speler.getSpelerKleur());
        } else {
            setAvatar(avatarView, new Image("/images/app/defaultProfImage.png"), playerAvatarCircle, speler.getSpelerKleur());
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

    private void configLabel(Speler speler, Label label) {
        label.setText(speler.getNaam());
        label.setStyle("-fx-text-fill: " + speler.getSpelerKleur());
    }

    // ----- configuring initial board -----

    private void configBord(int blockSize) {
        gameData.gameBord.setInitialOccupied(2);
        maakBord(gameData, blockSize);
    }

    private void maakBord(GameData data, int blokGroote) {
        bordRijen = new HBox[data.gameBord.yGroote];

        for (int y = 0; y < data.gameBord.yGroote; y++) {
            HBox row = new HBox();
            for (int x = 0; x < data.gameBord.xGroote; x++) {
                PlekHBox plekHBox = new PlekHBox(x, y, blokGroote);

                setPlekHBoxBorder(x, y, plekHBox);

                if (data.gameBord.getBord()[x][y].isBezet()) {
                    if (data.gameBord.getBord()[x][y].getSpelerNummer() == 1) {
                        setKleur(plekHBox, data.getSpelerEen().getSpelerKleur());
                        setPlekLabel(1, plekHBox);
                    } else {
                        setKleur(plekHBox, data.getSpelerTwee().getSpelerKleur());
                        setPlekLabel(2, plekHBox);
                    }
                }

                row.getChildren().add(plekHBox);
            }
            bordRijen[y] = row;
        }

        if (!bordColumnBox.getChildren().isEmpty()) bordColumnBox.getChildren().clear();

        bordColumnBox.getChildren().addAll(bordRijen);
    }

    private void setPlekHBoxBorder(int x, int y, Node space) {
        if (gameData.gameBord.xGroote - 1 != x && y != 0) {
            space.setStyle(
                    "-fx-border-width: 2 2 0 0;\n" +
                            "-fx-border-color: #242526;");
        } else if (gameData.gameBord.xGroote - 1 != x) {
            space.setStyle(
                    "-fx-border-width: 0 2 0 0;\n" +
                            "-fx-border-color: #242526;");
        } else if (gameData.gameBord.xGroote - 1 == x && y != 0) {
            space.setStyle(
                    "-fx-border-width: 2 0 0 0;\n" +
                            "-fx-border-color: #242526;");
        } else {
            space.setStyle(
                    "-fx-border-width: 0 0 0 0;\n" +
                            "-fx-border-color: #242526;");
        }
    }

    private void setPlekLabel(int spelerNummer, PlekHBox plekHBox) {
        if (spelerNummer == 1) {
            plekHBox.setText("H");
        } else {
            plekHBox.setText("B");
        }
    }

    private void verwijderPlekLabel(PlekHBox plekHBox) {
        plekHBox.setText("");
    }

    private void setKleur(PlekHBox plekHBox, String color) {
        String style = plekHBox.getStyle();
        String nieuweStijl = "";

        if (!style.isEmpty()) {
            String[] t = style.split("((?=;))");
            nieuweStijl = t[0] + t[1] + ";";
        }

        plekHBox.setStyle(nieuweStijl + "\n-fx-background-color: #" + "B0" + color.substring(3) + ";");
    }

    private void verwijderKleur(PlekHBox plekHBox) {
        String style = plekHBox.getStyle();
        String nieuweStijl = "";

        if (!style.isEmpty()) {
            String[] t = style.split("((?=;))");
            nieuweStijl = t[0] + t[1] + ";";
        }

        plekHBox.setStyle(nieuweStijl);
    }


    // ----- configuring initial turn -----

    public void setBeginBeurt() {
        Random random = new Random();

        if (random.nextInt(2) == 0) {
            gameData.huidigeSpeler = gameData.getSpelerEen();
            setHerstelButtonActief(true);
            setNieuweSpelerBeurt();
        } else {
            gameData.huidigeSpeler = gameData.getSpelerTwee();
            setHerstelButtonActief(false);
            setNieuweSpelerBeurt();
        }

        setBeurtInfo();
    }

    protected void setBeurtInfo() {
        turnInfo.setText("Turn " + gameData.getTurnNummer() + " | ");
    }

    protected void setHuigeBeurtSpeler() {
        currentPlayerLabel.setText(gameData.huidigeSpeler.getNaam());

        if (gameData.huidigeSpeler.getSpelerNummer() == 1) {
            addBeurtGlow(player1Label, player1AvatarCircle);
            setHerstelButtonActief(true);
        } else {
            addBeurtGlow(player2Label, player2AvatarCircle);
            setHerstelButtonActief(false);
        }
    }

    // ----- set or removes a glow when it's one player's turn or not -----

    protected void setTurnGlow(int playerNumber) {
        if (playerNumber == 1) {
            addBeurtGlow(player1Label, player1AvatarCircle);
            verwijderBeurtGlow(player2Label, player2AvatarCircle);
        } else {
            addBeurtGlow(player2Label, player2AvatarCircle);
            verwijderBeurtGlow(player1Label, player1AvatarCircle);
        }
    }

    private void addBeurtGlow(Label playerLabel, Circle avatarCircle) {
        Bloom bloom = new Bloom();
        bloom.setThreshold(0.01);

        playerLabel.setEffect(bloom);
        avatarCircle.setEffect(bloom);
    }

    private void verwijderBeurtGlow(Label playerLabel, Circle avatarCircle) {
        playerLabel.setEffect(null);
        avatarCircle.setEffect(null);
    }

    // ----- event that runs when the game turn goes back -----
    // pops the new turn data and peeks for the previous turn data
    @FXML
    private void resetTurn() throws UnderflowException {
        if (!gameGeschiedenis.isLeeg()) {
            AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

            deselecteerHuidigeSelectie(gameData.gameBord.getPlayerMoves(gameData.huidigeSpeler.getSpelerNummer()));

            resetBeurtGame(gameGeschiedenis.pop());
        }
    }

    private void resetBeurtGame(GameData data) {
        gameData = data;
        maakBord(data, 75);

        setOudeGameData();

        setBeurtInfo();
        updatePuntenLabel();

        gameData.huidigeSpeler = gameData.getSpelerEen();
        setNieuweSpelerBeurt();
    }

    protected void setHerstelButtonActief(boolean active) {
        if (!gameGeschiedenis.isLeeg()) {
            resetTurnButton.setVisible(active);
            resetTurnButton.setDisable(!active);
        } else {
            resetTurnButton.setVisible(false);
            resetTurnButton.setDisable(false);
        }
    }

    // ----- event that runs when the game has ended -----

    @FXML
    private void endGameEvent() {
        AudioPlayer.playAudioFile(MenuAudio.OK_AUDIO);

        if(gameData.huidigeSpeler.getSpelerNummer() == 1){
            stopGame(1,2);
        } else {
            stopGame(2,1);
        }
    }

    protected void stopGame(int oldP, int newP) {
        if (gameData.getSpelerEenScore() > gameData.getSpelerTweeScore()) {
            spelerEenWin();
        } else if (gameData.getSpelerTweeScore() > gameData.getSpelerEenScore()) {
            spelerTweeWin();
        } else {
            if (gameData.gameBord.checkBoard(newP)) {
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
        GameOverController gameOverController = new GameOverController(gameData.getSpelerEen(), gameData.getSpelerTwee(), GameOverType.P1, gameData.getSpelerEenScore(), gameData.getSpelerTweeScore());
        JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, FXMLPart.GAMEOVER).getRoot());
    }

    private void spelerTweeWin(){
        GameOverController gameOverController = new GameOverController(gameData.getSpelerEen(), gameData.getSpelerTwee(), GameOverType.P2, gameData.getSpelerEenScore(), gameData.getSpelerTweeScore());
        JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, FXMLPart.GAMEOVER).getRoot());
    }

    private void gelijkSpel(){
        GameOverController gameOverController = new GameOverController(gameData.getSpelerEen(), gameData.getSpelerTwee(), GameOverType.GELEIKSPEL, gameData.getSpelerEenScore(), gameData.getSpelerTweeScore());
        JFXApplication.gameMenuController.setContent(new GameOverScene(gameOverController, FXMLPart.GAMEOVER).getRoot());
    }

    // ----- for adding sounds? -----

    private EventHandler<? super MouseEvent> selecteerAudio() {
        return (EventHandler<MouseEvent>) event -> AudioPlayer.playAudioFile(MenuAudio.SELECT_AUDIO);
    }

    private EventHandler<? super MouseEvent> beweeg() {
        return (EventHandler<MouseEvent>) event -> AudioPlayer.playAudioFile(MenuAudio.MOVE_AUDIO);
    }
}


