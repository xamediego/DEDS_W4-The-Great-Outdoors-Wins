package mai.scenes.game.aigame;

import mai.data.AI;
import mai.datastructs.Stapel;
import mai.enums.Difficulty;
import mai.exceptions.UnderflowException;
import mai.scenes.game.logic.AttackVectors;
import mai.scenes.game.logic.GameBoard;
import mai.scenes.game.logic.Space;

import java.util.Random;

public class AILogic {

    public AIMove makeMove(GameBoard gameBoard, AI aiPlayer, int aiNumber, int opponentNumber) throws UnderflowException {
        Stapel<Space> selectAble = gameBoard.getPlayerMoves(aiNumber);

        if (aiPlayer.getAiTypes().contains(Difficulty.NORMAL)) {
            return EasyAI.makeEasyMove(selectAble, gameBoard, aiNumber, opponentNumber, aiPlayer);
        } else if (aiPlayer.getAiTypes().contains(Difficulty.EASY)) {
            return EasyAI.makeEasyMove(selectAble, gameBoard, aiNumber, opponentNumber, aiPlayer);
        } else {
            return RandomAI.makeRandomMove(selectAble, gameBoard, aiPlayer);
        }
    }
}

class EasyAI {
    public static AIMove makeEasyMove(Stapel<Space> selectAble, GameBoard gameBoard, int aiNumber, int opponentNumber, AI ai) {
        return selectMostPointMove(selectAble, gameBoard, aiNumber, opponentNumber, ai);
    }

    public static AIMove selectMostPointMove(Stapel<Space> selectAble, GameBoard gameBoard, int aiNumber, int opponentNumber, AI ai) {
        Stapel<Space> possibleLoses = new Stapel<>();

        Stapel<Space> possiblePlayerOneMove = gameBoard.getPlayerMoves(opponentNumber);

        int s = selectAble.getSize();

        for (int i = 0; i < s; i++) {
            try {
                if (!possiblePlayerOneMove.isEmpty()) {
                    Stapel<Space> playerMoveSquare = gameBoard.getInfected(possiblePlayerOneMove.pop(), opponentNumber);

                    while (!playerMoveSquare.isEmpty()) {
                        Space possibleAttack = playerMoveSquare.pop();
                        if (possibleAttack.getPlayerNumber() == 2 && !possibleLoses.contains(possibleAttack)) {
                            possibleLoses.push(possibleAttack);
                        }

                    }
                }
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        if (possibleLoses.getSize() - gameBoard.getPlayerSpaceCount(2) < 1) {
            return getMostValueAttack(selectAble, gameBoard, aiNumber, ai);
        } else {
            return getMostValueAttack(selectAble, gameBoard, aiNumber, ai);
        }
    }

    private static int hVal;

    private static AIMove getMostValueAttack(Stapel<Space> possiblePlayerMoves, GameBoard gameBoard, int aiNumber, AI ai) {
        Stapel<AIMove> possibleAttacks = new Stapel<>();
        hVal = 0;

        while (!possiblePlayerMoves.isEmpty()) {
            try {
                Space origin = possiblePlayerMoves.pop();

                AttackVectors attackVectors = gameBoard.getPossibleAttackSquare(origin, ai.getRange(), ai.getAttackDropOff());

                possibleAttacks = getHighestAttackValue(attackVectors.possibleOneRangeAttackVectors(), gameBoard, origin, possibleAttacks, aiNumber);
                possibleAttacks = getHighestAttackValue(attackVectors.possibleTwoRangeAttackVectors(), gameBoard, origin, possibleAttacks, aiNumber);

            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        return getRandomMove(possibleAttacks);
    }


    private static Stapel<AIMove> getHighestAttackValue(Stapel<Space> attackVectors, GameBoard gameBoard, Space origin, Stapel<AIMove> possibleAttacks, int aiNumber) {
        Space possibleAttackSelect;
        System.out.println("AI: " + aiNumber);
        while (!attackVectors.isEmpty()) {
            try {

                possibleAttackSelect = attackVectors.pop();

                Stapel<Space> possibleAttackCompare = gameBoard.getInfected(possibleAttackSelect, aiNumber);


                if (possibleAttackCompare.getSize() > hVal) {
                    hVal = possibleAttackCompare.getSize();

                    possibleAttacks = new Stapel<>();
                    possibleAttacks.push(new AIMove(origin, possibleAttackSelect));
                } else if (possibleAttackCompare.getSize() == hVal) {
                    possibleAttacks.push(new AIMove(origin, possibleAttackSelect));
                }

            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        return possibleAttacks;
    }

    private static AIMove getRandomMove(Stapel<AIMove> moves) {
        Random random = new Random();

        AIMove aiMove = null;
        System.out.println("M: " + moves.getSize());

        try {
            aiMove = moves.peek();
        } catch (UnderflowException e) {
            e.printStackTrace();
        }

        if (moves.getSize() > 1) {
            int r = random.nextInt(1, moves.getSize());

            for (int i = 0; i < r; i++) {
                try {
                    aiMove = moves.pop();
                } catch (UnderflowException e) {
                    e.printStackTrace();
                }
            }

        }

        return aiMove;
    }
}

class RandomAI {
    public static AIMove makeRandomMove(Stapel<Space> selectAble, GameBoard gameBoard, AI ai) throws UnderflowException {
        Random random = new Random();

        int size = selectAble.getSize();

        Space select = getAttackSpace(selectAble, size);

        AttackVectors attackVectors = gameBoard.getPossibleAttackSquare(select, ai.getRange(), ai.getAttackDropOff());

        if (attackVectors.possibleTwoRangeAttackVectors().getSize() < 1) {
            return getRandomMove(attackVectors.possibleOneRangeAttackVectors(), select, random.nextInt(attackVectors.possibleOneRangeAttackVectors().getSize()));
        } else if (attackVectors.possibleOneRangeAttackVectors().getSize() < 1) {
            return getRandomMove(attackVectors.possibleTwoRangeAttackVectors(), select, random.nextInt(attackVectors.possibleTwoRangeAttackVectors().getSize()));
        }

        if (random.nextInt(2) == 0) {
            return getRandomMove(attackVectors.possibleOneRangeAttackVectors(), select, random.nextInt(attackVectors.possibleOneRangeAttackVectors().getSize()));
        } else {
            return getRandomMove(attackVectors.possibleTwoRangeAttackVectors(), select, random.nextInt(attackVectors.possibleTwoRangeAttackVectors().getSize()));
        }
    }

    private static Space getAttackSpace(Stapel<Space> selectAble, int rate) throws UnderflowException {
        Space select = selectAble.peek();

        if (selectAble.getSize() > 1) {
            for (int i = 0; i < rate; i++) {
                select = selectAble.pop();
            }
        }

        return select;
    }

    private static AIMove getRandomMove(Stapel<Space> attackVectors, Space origin, int random) throws UnderflowException {
        Space select = attackVectors.peek();

        if (attackVectors.getSize() > 1) {
            for (int i = 0; i < random; i++) {
                select = attackVectors.pop();
            }
        }

        return new AIMove(origin, select);
    }

}
