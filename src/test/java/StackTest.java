import mai.datastructs.Stack;
import mai.exceptions.UnderflowException;
import mai.scenes.game.data.GameBoard;
import mai.scenes.game.data.GameData;
import mai.scenes.game.data.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StackTest {

    @Test
    void pushTestOnce() throws Exception {
        int value = 1;

        Stack<Integer> stack = new Stack<>();

        stack.push(value);

        Assertions.assertEquals((int) stack.peek(), value);
    }

    @Test
    void pushTestMultiple() throws Exception {
        int count = 10;

        Stack<Integer> stack = new Stack<>();

        for (int i = 1; i <= count; i++) {
            stack.push(i);
        }

        Assertions.assertEquals((int) stack.peek(), count);
    }

    @Test
    void pushPopTestMultiple() throws Exception {
        int count = 10;

        Stack<Integer> stack = new Stack<>();

        for (int i = 1; i <= count; i++) {
            stack.push(i);
        }

        stack.pop();
        stack.pop();

        Assertions.assertEquals((int) stack.peek(), count - 2);
    }


    @Test
    void popTest() throws Exception {
        int count = 10;

        Stack<Integer> stack = new Stack<>();

        for (int i = 1; i <= count; i++) {
            stack.push(i);
        }

        Assertions.assertEquals((int) stack.pop(), count);

        Assertions.assertEquals((int) stack.peek(), count - 1);
    }

    @Test
    void isEmptyTestOne() {
        Stack<Integer> stack = new Stack<>();

        Assertions.assertTrue(stack.isEmpty());
    }

    @Test
    void isNotEmptyTest() {
        Stack<Integer> stack = new Stack<>();

        int value = 1;
        stack.push(value);

        Assertions.assertFalse(stack.isEmpty());
    }

    @Test
    void popUnderflowTestOne() {
        Stack<Integer> stack = new Stack<>();

        Assertions.assertThrows(UnderflowException.class, stack::pop);
    }

    @Test
    void popUnderflowTestTwo() throws Exception {
        int count = 10;

        Stack<Integer> stack = new Stack<>();

        for (int i = 1; i <= count; i++) {
            stack.push(i);
        }

        for (int i = 1; i <= count; i++) {
            stack.pop();
        }

        Assertions.assertThrows(UnderflowException.class, stack::pop);
    }


    @Test
    void stackContainTest(){
        int count = 10;

        Stack<Integer> stack = new Stack<>();

        for (int i = 1; i <= count; i++) {
            stack.push(i);
        }

        Assertions.assertTrue(stack.contains(1));
    }

    @Test
    void stackDoesNotContainTest(){
        int count = 10;

        Stack<Integer> stack = new Stack<>();

        for (int i = 1; i <= count; i++) {
            stack.push(i);
        }

        Assertions.assertFalse(stack.contains(0));
    }



    @Test
    void peekUnderflowTest() {
        Stack<Integer> stack = new Stack<>();

        Assertions.assertThrows(UnderflowException.class, stack::peek);
    }

    @Test
    void gameDataStackTest() throws Exception {
        int count = 10;

        Stack<GameData> stack = new Stack<>();
        GameData gameData = new GameData(0, 0, 1, null, null, null);

        for (int i = 0; i < count; i++) {
            stack.push(new GameData(0, 0, gameData.getTurnNumber(), null, null, null));
            gameData.increaseTurnNumber();
        }

        Assertions.assertEquals(count, stack.peek().getTurnNumber());

        for (int i = gameData.getTurnNumber() - 1; i >= 1; i--) {
            Assertions.assertEquals(i , stack.pop().getTurnNumber());
        }

    }

    @Test
    void gameDataStackTest2() throws Exception {
        int count = 10;

        Stack<GameData> stack = new Stack<>();
        GameData gameData = new GameData(0, 0, 1, null, null, null);

        for (int i = 0; i < count; i++) {
            stack.push(new GameData(0, 0, gameData.getTurnNumber(), null, null, null));
            gameData.increaseTurnNumber();
        }

        Assertions.assertEquals(count, stack.peek().getTurnNumber());
    }

    @Test
    void gameEndRulesTest(){
        GameBoard gameBoard = new GameBoard(7,7, new Space[7][7]);

        for (int y = 0; y < 7; y++) {

            for(int x = 0;x < 3; x++){
                gameBoard.getBord()[x][y] = new Space(x, y, true, 1);
            }

            for(int x = 3;x < 7; x++){
                gameBoard.getBord()[x][y] = new Space(x, y, true, 2);
            }

        }

        Assertions.assertTrue(gameBoard.isFull());

        Assertions.assertTrue(gameBoard.checkPossibleAttacks(1));
        Assertions.assertTrue(gameBoard.checkPossibleAttacks(2));

        Assertions.assertFalse(gameBoard.checkScore(1));
        Assertions.assertTrue(gameBoard.checkScore(2));

        Assertions.assertTrue(gameBoard.checkBoard(1));
        Assertions.assertTrue(gameBoard.checkBoard(2));
    }
}
