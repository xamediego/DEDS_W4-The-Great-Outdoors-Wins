import mai.datastructs.Stapel;
import mai.exceptions.UnderflowException;
import mai.scenes.game.aigame.AILogic;
import mai.scenes.game.logic.GameBoard;
import mai.scenes.game.logic.GameData;
import mai.scenes.game.logic.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StapelTest {

    @Test
    void pushTestOnce() throws Exception {
        int value = 1;

        Stapel<Integer> stapel = new Stapel<>();

        stapel.push(value);

        Assertions.assertEquals((int) stapel.peek(), value);
    }

    @Test
    void pushTestMultiple() throws Exception {
        int count = 10;

        Stapel<Integer> stapel = new Stapel<>();

        for (int i = 1; i <= count; i++) {
            stapel.push(i);
        }

        Assertions.assertEquals((int) stapel.peek(), count);
    }

    @Test
    void pushPopTestMultiple() throws Exception {
        int count = 10;

        Stapel<Integer> stapel = new Stapel<>();

        for (int i = 1; i <= count; i++) {
            stapel.push(i);
        }

        stapel.pop();
        stapel.pop();

        Assertions.assertEquals((int) stapel.peek(), count - 2);
    }


    @Test
    void popTest() throws Exception {
        int count = 10;

        Stapel<Integer> stapel = new Stapel<>();

        for (int i = 1; i <= count; i++) {
            stapel.push(i);
        }

        Assertions.assertEquals((int) stapel.pop(), count);

        Assertions.assertEquals((int) stapel.peek(), count - 1);
    }

    @Test
    void isEmptyTestOne() {
        Stapel<Integer> stapel = new Stapel<>();

        Assertions.assertTrue(stapel.isEmpty());
    }

    @Test
    void isNotEmptyTest() {
        Stapel<Integer> stapel = new Stapel<>();

        int value = 1;
        stapel.push(value);

        Assertions.assertFalse(stapel.isEmpty());
    }

    @Test
    void popUnderflowTestOne() {
        Stapel<Integer> stapel = new Stapel<>();

        Assertions.assertThrows(UnderflowException.class, stapel::pop);
    }

    @Test
    void popUnderflowTestTwo() throws Exception {
        int count = 10;

        Stapel<Integer> stapel = new Stapel<>();

        for (int i = 1; i <= count; i++) {
            stapel.push(i);
        }

        for (int i = 1; i <= count; i++) {
            stapel.pop();
        }

        Assertions.assertThrows(UnderflowException.class, stapel::pop);
    }


    @Test
    void stackContainTest(){
        int count = 10;

        Stapel<Integer> stapel = new Stapel<>();

        for (int i = 1; i <= count; i++) {
            stapel.push(i);
        }

        Assertions.assertTrue(stapel.contains(1));
    }

    @Test
    void stackDoesNotContainTest(){
        int count = 10;

        Stapel<Integer> stapel = new Stapel<>();

        for (int i = 1; i <= count; i++) {
            stapel.push(i);
        }

        Assertions.assertFalse(stapel.contains(0));
    }



    @Test
    void peekUnderflowTest() {
        Stapel<Integer> stapel = new Stapel<>();

        Assertions.assertThrows(UnderflowException.class, stapel::peek);
    }

    @Test
    void gameDataStackTest() throws Exception {
        int count = 10;

        Stapel<GameData> stapel = new Stapel<>();
        GameData gameData = new GameData(0, 0, 1, null, null, null);

        for (int i = 0; i < count; i++) {
            stapel.push(new GameData(0, 0, gameData.getTurnNumber(), null, null, null));
            gameData.increaseTurnNumber();
        }

        Assertions.assertEquals(count, stapel.peek().getTurnNumber());

        for (int i = gameData.getTurnNumber() - 1; i >= 1; i--) {
            Assertions.assertEquals(i , stapel.pop().getTurnNumber());
        }

    }

    @Test
    void gameDataStackTest2() throws Exception {
        int count = 10;

        Stapel<GameData> stapel = new Stapel<>();
        GameData gameData = new GameData(0, 0, 1, null, null, null);

        for (int i = 0; i < count; i++) {
            stapel.push(new GameData(0, 0, gameData.getTurnNumber(), null, null, null));
            gameData.increaseTurnNumber();
        }

        Assertions.assertEquals(count, stapel.peek().getTurnNumber());
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
