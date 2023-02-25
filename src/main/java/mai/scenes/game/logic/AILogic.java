package mai.scenes.game.logic;

import mai.data.AI;
import mai.datastructs.Stack;
import mai.enums.Difficulty;
import mai.exceptions.UnderflowException;
import mai.scenes.game.data.AttackVectors;
import mai.scenes.game.data.GameBoard;
import mai.scenes.game.data.Space;

import java.util.Random;

import static mai.scenes.game.logic.EasyAI.*;

public class AILogic {

    public AIMove makeMove(GameBoard gameBoard, AI aiPlayer, int aiNumber, int opponentNumber) throws UnderflowException {
        Stack<Space> selectAble = gameBoard.getPlayerMoves(aiNumber);

        if (aiPlayer.getAiTypes().contains(Difficulty.NORMAL)) {
            return EasyAI.makeEasyMove(selectAble, gameBoard, aiNumber, opponentNumber, aiPlayer);
        } else if (aiPlayer.getAiTypes().contains(Difficulty.EASY)) {
            return EasyAI.makeEasyMove(selectAble, gameBoard, aiNumber, opponentNumber, aiPlayer);
        } else {
            return RandomAI.makeRandomMove(selectAble, gameBoard, aiPlayer);
        }
    }
}

class NormalAI {
    public static AIMove makeEasyMove(Stack<Space> selectAble, GameBoard gameBoard, int aiNumber, int opponentNumber, AI ai) {
        return selectMostPointMove(selectAble, gameBoard, aiNumber, opponentNumber, ai);
    }

    public static AIMove selectMostPointMove(Stack<Space> selectAble, GameBoard gameBoard, int aiNumber, int opponentNumber, AI ai) {
        Stack<Space> possibleLoses = new Stack<>();

        Stack<Space> possiblePlayerOneMove = gameBoard.getPlayerMoves(opponentNumber);

        int s = selectAble.getSize();

        for (int i = 0; i < s; i++) {
            try {
                if (!possiblePlayerOneMove.isEmpty()) {
                    Stack<Space> playerMoveSquare = gameBoard.getInfected(possiblePlayerOneMove.pop(), opponentNumber);

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



}

class EasyAI {
    public static AIMove makeEasyMove(Stack<Space> selectAble, GameBoard gameBoard, int aiNumber, int opponentNumber, AI ai) {
        return selectMostPointMove(selectAble, gameBoard, aiNumber, opponentNumber, ai);
    }

    public static AIMove selectMostPointMove(Stack<Space> selectAble, GameBoard gameBoard, int aiNumber, int opponentNumber, AI ai) {
        return getMostValueAttack(selectAble, gameBoard, aiNumber, ai);
    }

    private static int hVal;

    protected static AIMove getMostValueAttack(Stack<Space> possiblePlayerMoves, GameBoard gameBoard, int aiNumber, AI ai) {
        Stack<AIMove> possibleAttacks = new Stack<>();
        hVal = 0;

        while (!possiblePlayerMoves.isEmpty()) {
            try {
                Space origin = possiblePlayerMoves.pop();

                AttackVectors attackVectors = gameBoard.getPossibleAttackSquare(origin, ai.getRange(), ai.getAttackDropOff());



                possibleAttacks = getHighestAttackValue(attackVectors.possibleOneRangeAttackVectors, gameBoard, origin, possibleAttacks, aiNumber);
                possibleAttacks = getHighestAttackValue(attackVectors.possibleTwoRangeAttackVectors, gameBoard, origin, possibleAttacks, aiNumber);

            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        return getRandomMove(possibleAttacks);
    }


    protected static Stack<AIMove> getHighestAttackValue(Stack<Space> attackVectors, GameBoard gameBoard, Space origin, Stack<AIMove> possibleAttacks, int aiNumber) {
        Space possibleAttackSelect;

        while (!attackVectors.isEmpty()) {
            try {

                possibleAttackSelect = attackVectors.pop();

                Stack<Space> possibleAttackCompare = gameBoard.getInfected(possibleAttackSelect, aiNumber);


                if (possibleAttackCompare.getSize() > hVal) {
                    hVal = possibleAttackCompare.getSize();

                    possibleAttacks = new Stack<>();
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

    protected static AIMove getRandomMove(Stack<AIMove> moves) {
        Random random = new Random();

        AIMove aiMove = null;
        System.out.println("M: " + moves.getSize());

        try {
            aiMove = moves.peek();
        } catch (UnderflowException e) {
            e.printStackTrace();
        }

        if (moves.getSize() > 1) {

            int r = random.nextInt(moves.getSize());

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
    public static AIMove makeRandomMove(Stack<Space> selectAble, GameBoard gameBoard, AI ai) throws UnderflowException {
        Random random = new Random();

        int size = selectAble.getSize();

        Space select = getAttackSpace(selectAble, size);

        AttackVectors attackVectors = gameBoard.getPossibleAttackSquare(select, ai.getRange(), ai.getAttackDropOff());

        if (attackVectors.possibleTwoRangeAttackVectors.getSize() < 1) {
            return getRandomMove(attackVectors.possibleOneRangeAttackVectors, select, random.nextInt(attackVectors.possibleOneRangeAttackVectors.getSize()));
        } else if (attackVectors.possibleOneRangeAttackVectors.getSize() < 1) {
            return getRandomMove(attackVectors.possibleTwoRangeAttackVectors, select, random.nextInt(attackVectors.possibleTwoRangeAttackVectors.getSize()));
        }

        if (random.nextInt(2) == 0) {
            return getRandomMove(attackVectors.possibleOneRangeAttackVectors, select, random.nextInt(attackVectors.possibleOneRangeAttackVectors.getSize()));
        } else {
            return getRandomMove(attackVectors.possibleTwoRangeAttackVectors, select, random.nextInt(attackVectors.possibleTwoRangeAttackVectors.getSize()));
        }
    }

    private static Space getAttackSpace(Stack<Space> selectAble, int rate) throws UnderflowException {
        Space select = selectAble.peek();

        if (selectAble.getSize() > 1) {
            for (int i = 0; i < rate; i++) {
                select = selectAble.pop();
            }
        }

        return select;
    }

    private static AIMove getRandomMove(Stack<Space> attackVectors, Space origin, int random) throws UnderflowException {
        Space select = attackVectors.peek();

        if (attackVectors.getSize() > 1) {
            for (int i = 0; i < random; i++) {
                select = attackVectors.pop();
            }
        }

        return new AIMove(origin, select);
    }

}
