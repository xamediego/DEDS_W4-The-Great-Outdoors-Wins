package mai.scenes.game.aigame;

import mai.data.AI;
import mai.datastructs.Stapel;
import mai.enums.Difficulty;
import mai.exceptions.UnderflowException;
import mai.scenes.game.logic.AttackVectors;
import mai.scenes.game.logic.Space;
import mai.scenes.game.normalgame.GameController;

import java.util.Random;

public class AILogic {

    public void makeMove(GameController gameController, AI aiPlayer) throws UnderflowException {
        Stapel<Space> selectAble = gameController.gameData.gameBoard.getPlayerMoves(2);

        if (aiPlayer.getAiTypes().contains(Difficulty.NORMAL)) {
            makeRandomMove(selectAble, gameController);
        } else if(aiPlayer.getAiTypes().contains(Difficulty.EASY)){
            makeRandomMove(selectAble, gameController);
        } else {
            makeRandomMove(selectAble, gameController);
        }

    }

    private void makeRandomMove(Stapel<Space> selectAble, GameController gameController) throws UnderflowException {
        Random random = new Random();

        int size = selectAble.getSize();

        Space select = getAttackSpace(selectAble, size);

        AttackVectors attackVectors = gameController.gameData.gameBoard.getPossibleAttackSquare(select, 3, 2, 2);

        System.out.println("SIZE L ST: " + attackVectors.possibleTwoRangeAttackVectors().getSize());
        System.out.println("SIZE S ST: " + attackVectors.possibleOneRangeAttackVectors().getSize());

        if (attackVectors.possibleTwoRangeAttackVectors().getSize() < 1) {
            attackRandomShort(gameController, attackVectors.possibleOneRangeAttackVectors());
        } else if (attackVectors.possibleOneRangeAttackVectors().getSize() < 1) {
            attackRandomLong(gameController, attackVectors.possibleTwoRangeAttackVectors(), select);
        }

        if (random.nextInt(2) == 0) {
            attackRandomShort(gameController, attackVectors.possibleOneRangeAttackVectors());
        } else {
            attackRandomLong(gameController, attackVectors.possibleTwoRangeAttackVectors(), select);
        }
    }

    private Space getAttackSpace(Stapel<Space> selectAble, int rate) throws UnderflowException {
        Space select = selectAble.peek();

        for (int i = 0; i < rate; i++) {
            select = selectAble.pop();
        }

        return select;
    }

    private void attackRandomLong(GameController gameController, Stapel<Space> attackVectors, Space origin) throws UnderflowException {
        int size = attackVectors.getSize();
        System.out.println("SIZE L: " + size);

        Space select = attackVectors.peek();

        for (int i = 0; i < size; i++) {
            select = attackVectors.pop();
        }

        gameController.longRangeAttack(origin, select);
    }

    private void attackRandomShort(GameController gameController, Stapel<Space> attackVectors) throws UnderflowException {
        int size = attackVectors.getSize();
        System.out.println("SIZE S: " + size);

        Space select = attackVectors.peek();

        for (int i = 0; i < size; i++) {
            select = attackVectors.pop();
        }

        gameController.shortRangeAttack(select);
    }

}
