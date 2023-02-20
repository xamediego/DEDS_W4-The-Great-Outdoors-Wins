package mai.datastructs;


import mai.exceptions.UnderflowException;

//reversed linked list?
public class Stapel<T> {

    private static class Node<T> {
        T value;
        Node<T> prev;
    }

    private Node<T> top;

    public boolean isEmpty() {
        return top == null;
    }

    public T push(T value) {
        Node<T> newVal = new Node<>();

        newVal.value = value;
        newVal.prev = top;

        top = newVal;

        return newVal.value;
    }

    public T pop() throws UnderflowException {
        if (isEmpty()) {
            throw new UnderflowException("Stapel is leeg");
        }

        T value = top.value;
        top = top.prev;

        return value;
    }

    public T peek() throws UnderflowException {
        if (isEmpty()) {
            throw new UnderflowException("Stapel is leeg");
        }

        return top.value;
    }

}
