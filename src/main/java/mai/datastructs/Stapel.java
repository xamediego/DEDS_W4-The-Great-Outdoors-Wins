package mai.datastructs;


import mai.exceptions.UnderflowException;

public class Stapel<T> {

    private static class Node<T> {
        T value;
        Node<T> prev;
    }

    private Node<T> top;
    private int size;

    public boolean isEmpty() {
        return top == null;
    }

    public T push(T value) {
        Node<T> newVal = new Node<>();

        newVal.value = value;
        newVal.prev = top;

        top = newVal;
        size++;

        return newVal.value;
    }

    public T pop() throws UnderflowException {
        if (isEmpty()) {
            throw new UnderflowException("Stapel is leeg");
        }

        T value = top.value;
        top = top.prev;
        size--;

        return value;
    }

    public T peek() throws UnderflowException {
        if (isEmpty()) {
            throw new UnderflowException("Stapel is leeg");
        }

        return top.value;
    }

    public T get(int index) throws IndexOutOfBoundsException {
        if (index >= getSize()) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> returnNode = top;

        while (index > 0){
            returnNode = returnNode.prev;

            index--;
        }

        return returnNode.value;
    }


    public boolean contains(T value){
        Node<T> compareNode = top;

        for(int i = 0; i < size; i++){
            if(value == compareNode.value) return true;
            compareNode = compareNode.prev;
        }

        return false;
    }


    public void pushAll(Stapel<T> values){
        while (!values.isEmpty()){
            try {
                push(values.pop());
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSize(){
        return size;
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();

        Node<T> compareNode = top;

        for(int i = 0; i < size; i++){
            if (i <= size - 1) {
                returnString.append(compareNode.value.toString()).append("/n");
                compareNode = compareNode.prev;
            } else {
                returnString.append(compareNode.value.toString());
                compareNode = compareNode.prev;
            }
        }

        return returnString.toString();
    }
}
