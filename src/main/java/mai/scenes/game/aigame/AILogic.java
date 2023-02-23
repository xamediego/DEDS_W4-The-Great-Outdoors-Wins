package mai.scenes.game.aigame;

import mai.data.AI;
import mai.datastructs.Stapel;
import mai.enums.Difficulty;
import mai.exceptions.UnderflowException;
import mai.scenes.game.logic.AttackVectors;
import mai.scenes.game.logic.GameBoard;
import mai.scenes.game.logic.Space;
import mai.scenes.game.normalgame.GameController;

import java.util.Random;

public class AILogic {

    public AIMove makeMove(GameBoard gameBoard, AI aiPlayer) throws UnderflowException {
        Stapel<Space> selectAble = gameBoard.getPlayerMoves(2);

        if (aiPlayer.getAiTypes().contains(Difficulty.NORMAL)) {
            return makeRandomMove(selectAble, gameBoard);
        } else if (aiPlayer.getAiTypes().contains(Difficulty.EASY)) {
            return makeRandomMove(selectAble, gameBoard);
        } else {
            return makeRandomMove(selectAble, gameBoard);
        }

    }

    private AIMove makeRandomMove(Stapel<Space> selectAble, GameBoard gameBoard) throws UnderflowException {
        Random random = new Random();

        int size = selectAble.getSize();

        Space select = getAttackSpace(selectAble, size);

        AttackVectors attackVectors = gameBoard.getPossibleAttackSquare(select, 3, 2, 2);

        System.out.println("SIZE L ST: " + attackVectors.possibleTwoRangeAttackVectors().getSize());
        System.out.println("SIZE S ST: " + attackVectors.possibleOneRangeAttackVectors().getSize());

        if (attackVectors.possibleTwoRangeAttackVectors().getSize() < 1) {
            return attackRandomShort(attackVectors.possibleOneRangeAttackVectors(), select);
        } else if (attackVectors.possibleOneRangeAttackVectors().getSize() < 1) {
            return attackRandomLong(attackVectors.possibleTwoRangeAttackVectors(), select);
        }

        if (random.nextInt(2) == 0) {
            return attackRandomShort(attackVectors.possibleOneRangeAttackVectors(), select);
        } else {
            return attackRandomLong(attackVectors.possibleTwoRangeAttackVectors(), select);
        }
    }

    private Space getAttackSpace(Stapel<Space> selectAble, int rate) throws UnderflowException {
        Space select = selectAble.peek();

        for (int i = 0; i < rate; i++) {
            select = selectAble.pop();
        }

        return select;
    }

    private AIMove attackRandomLong(Stapel<Space> attackVectors, Space origin) throws UnderflowException {
        int size = attackVectors.getSize();

        Space select = attackVectors.peek();

        for (int i = 0; i < size; i++) {
            select = attackVectors.pop();
        }

        return new AIMove(origin, select);
    }

    private AIMove attackRandomShort(Stapel<Space> attackVectors, Space origin) throws UnderflowException {
        int size = attackVectors.getSize();

        Space select = attackVectors.peek();

        for (int i = 0; i < size; i++) {
            select = attackVectors.pop();
        }

        return new AIMove(origin, select);
    }

}
