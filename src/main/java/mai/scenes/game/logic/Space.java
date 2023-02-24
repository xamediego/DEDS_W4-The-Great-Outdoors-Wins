package mai.scenes.game.logic;

public class Space {
    public int x, y;

    private boolean isTaken;
    private int playerNumber;

    public Space(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Space(int x, int y, boolean isTaken, int playerNumber) {
        this(x, y);
        this.isTaken = isTaken;
        this.playerNumber = playerNumber;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void take(int playerNumber) {
        isTaken = true;
        this.playerNumber = playerNumber;
    }

    public void deselect() {
        isTaken = false;
        this.playerNumber = 0;
    }

    @Override
    public String toString() {
        return "Space{" +
                "x=" + x +
                ", y=" + y +
                ", isTaken=" + isTaken +
                ", playerNumber=" + playerNumber +
                '}';
    }
}