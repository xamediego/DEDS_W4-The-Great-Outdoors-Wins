import mai.datastructs.Stapel;
import mai.exceptions.UnderflowException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StapelTest {

    @Test
    void pushTestOnce() throws Exception{
        int value = 1;

        Stapel<Integer> stapel = new Stapel<>();

        stapel.push(value);

        Assertions.assertEquals((int) stapel.peek(), value);
    }

    @Test
    void pushTestMultiple() throws Exception{
        int count = 10;

        Stapel<Integer> stapel = new Stapel<>();

        for(int i = 1 ;i <= count; i++){
            stapel.push(i);
        }

        Assertions.assertEquals((int) stapel.peek(), count);
    }


    @Test
    void popTest() throws Exception{
        int count = 10;

        Stapel<Integer> stapel = new Stapel<>();

        for(int i = 1 ;i <= count; i++){
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
    void popUnderflowTestTwo() throws Exception{
        int count = 10;

        Stapel<Integer> stapel = new Stapel<>();

        for(int i = 1 ;i <= count; i++){
            stapel.push(i);
        }

        for(int i = 1 ;i <= count; i++){
            stapel.pop();
        }

        Assertions.assertThrows(UnderflowException.class, stapel::pop);
    }

    @Test
    void peekUnderflowTest() {
        Stapel<Integer> stapel = new Stapel<>();

        Assertions.assertThrows(UnderflowException.class, stapel::peek);
    }


}
