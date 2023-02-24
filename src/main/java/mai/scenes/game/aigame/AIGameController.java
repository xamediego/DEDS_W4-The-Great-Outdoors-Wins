package mai.scenes.game.aigame;

import mai.exceptions.UnderflowException;
import mai.scenes.game.logic.GameData;
import mai.scenes.game.normalgame.GameController;

import java.util.Random;

public class AIGameController extends GameController {

    private final AILogic aiLogic = new AILogic();

    public AIGameController(GameData gameData, int spaceSize) {
        super(gameData, spaceSize);
    }

    @Override
    public void endSpelerBeurt() {
        verwijderSelecteerBaar(gameData.huidigeSpeler.getSpelerNummer());

        int oldP, newP;

        if (gameData.huidigeSpeler.getSpelerNummer() == 1) {
            gameData.spelerEenKlaar = true;
            gameData.huidigeSpeler = gameData.getSpelerTwee();

            voegGameGeschiedenisToe(oldGameData);

            oldP = 1;
            newP = 2;
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

            if (newP == 2) {
                setNewAIMove();
            } else {
                setNieuweSpelerBeurt();
            }
        }
    }

    @Override
    public void setBeginBeurt() {
        Random random = new Random();

        if (random.nextInt(2) == 0) {
            gameData.huidigeSpeler = gameData.getSpelerEen();

            setHerstelButtonActief(true);
            setNieuweSpelerBeurt();
        } else {
            gameData.huidigeSpeler = gameData.getSpelerTwee();

            setHerstelButtonActief(false);
            setNewAIMove();
        }

        setBeurtInfo();
    }

    private void setNewAIMove() {
        try {
            setTurnGlow(gameData.huidigeSpeler.getSpelerNummer());

            setHuigeBeurtSpeler();

            AIMove aiMove = aiLogic.maakMove(this.gameData.gameBord, gameData.getSpelerTwee(), 2, 1);

            beweeg(aiMove.getOorsprong(), aiMove.getSelectie(), gameData.getSpelerTwee(), gameData.getSpelerTwee().getBereik(), gameData.getSpelerTwee().getMinBereik());
        } catch (UnderflowException e) {
            e.printStackTrace();
        }
    }
}
