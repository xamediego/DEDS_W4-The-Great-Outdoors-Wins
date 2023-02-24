package mai.datastructs;


import mai.exceptions.UnderflowException;

public class Stapel<T> {

    private static class Punt<T> {
        T value;
        Punt<T> prev;
    }

    private Punt<T> top;
    private int groote;

    public T push(T waarde) {
        Punt<T> newVal = new Punt<>();

        newVal.value = waarde;
        newVal.prev = top;

        top = newVal;
        groote++;

        return newVal.value;
    }

    public T pop() throws UnderflowException {
        if (isLeeg()) {
            throw new UnderflowException("Stapel is leeg");
        }

        T value = top.value;
        top = top.prev;
        groote--;

        return value;
    }

    public T peek() throws UnderflowException {
        if (isLeeg()) {
            throw new UnderflowException("Stapel is leeg");
        }

        return top.value;
    }

    public boolean contains(T waarde){
        Punt<T> comparePunt = top;

        for(int i = 0; i < groote; i++){
            if(waarde == comparePunt.value) return true;
            comparePunt = comparePunt.prev;
        }

        return false;
    }


    public void pushAll(Stapel<T> waardes){
        while (!waardes.isLeeg()){
            try {
                push(waardes.pop());
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    public int getGroote(){
        return groote;
    }

    public boolean isLeeg() {
        return top == null;
    }

    public int indexOf(T waarde){
        Punt<T> comparePunt = top;

        for(int i = (groote - 1); i >= 0; i--){
            if(waarde == comparePunt.value) return i;
            comparePunt = comparePunt.prev;
        }

        return -1;
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();

        Punt<T> comparePunt = top;

        for(int i = 0; i < groote; i++){
            if (i <= groote - 1) {
                returnString.append(comparePunt.value.toString()).append("/n");
                comparePunt = comparePunt.prev;
            } else {
                returnString.append(comparePunt.value.toString());
                comparePunt = comparePunt.prev;
            }
        }

        return returnString.toString();
    }
}
