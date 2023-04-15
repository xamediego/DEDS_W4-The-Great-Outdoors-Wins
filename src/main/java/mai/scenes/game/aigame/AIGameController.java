package mai.scenes.game.aigame;

import mai.exceptions.UnderflowException;
import mai.scenes.game.logic.GameData;
import mai.scenes.game.normalgame.GameController;
import mai.scenes.gamemenu.GameMenuController;

import java.util.Random;

public class AIGameController extends GameController {

    private final AILogic aiLogic = new AILogic();

    public AIGameController(GameData gameData, int spaceMinSize, int spaceMaxSize, GameMenuController gameMenuController) {
        super(gameData, spaceMinSize, spaceMaxSize, gameMenuController);
    }

    @Override
    public void endPlayerMove() {
        int oldP, newP;

        if (gameData.currentPlayer.getPlayerNumber() == 1) {
            gameData.player1Finished = true;
            gameData.currentPlayer = gameData.getPlayer2();

            addGameHistory(oldGameData);

            oldP = 1;
            newP = 2;
        } else {
            oldP = 2;
            newP = 1;

            gameData.player2Finished = true;
            gameData.currentPlayer = gameData.getPlayer1();
        }

        if (checkGameConditions(newP)) {
            endGame(newP,oldP);
        } else {
            if (gameData.player1Finished && gameData.player2Finished) endTurn();

            if (newP == 2) {
                setNewAIMove();
            } else {
                setNewPlayerMove();
            }
        }
    }

    @Override
    public void setInitialTurn() {
        Random random = new Random();

        if (random.nextInt(2) == 0) {
            gameData.currentPlayer = gameData.getPlayer1();

            setResetButtonActive(true);
            setNewPlayerMove();
        } else {
            gameData.currentPlayer = gameData.getPlayer2();

            setResetButtonActive(false);
            setNewAIMove();
        }

        setTurnInfo();
    }

    private void setNewAIMove() {
        try {
            setTurnGlow(gameData.currentPlayer.getPlayerNumber());

            setCurrentPlayer();

            AIMove aiMove = aiLogic.makeMove(this.gameData.gameBoard, gameData.getPlayer2(), 2);

            move(aiMove.getOrigin(), aiMove.getSelected(), gameData.getPlayer2(), gameData.getPlayer2().getRange(), gameData.getPlayer2().getAttackDropOff());
        } catch (UnderflowException e) {
            e.printStackTrace();
        }
    }
}
