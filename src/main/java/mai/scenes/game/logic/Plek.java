package mai.scenes.game.logic;

public class Plek {
    public int x, y;

    private boolean isBezet;
    private int spelerNummer;

    public Plek(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Plek(int x, int y, boolean isBezet, int spelerNummer) {
        this(x, y);
        this.isBezet = isBezet;
        this.spelerNummer = spelerNummer;
    }

    public boolean isBezet() {
        return isBezet;
    }

    public int getSpelerNummer() {
        return spelerNummer;
    }

    public void take(int playerNumber) {
        isBezet = true;
        this.spelerNummer = playerNumber;
    }

    public void deselect() {
        isBezet = false;
        this.spelerNummer = 0;
    }

    @Override
    public String toString() {
        return "Space{" +
                "x=" + x +
                ", y=" + y +
                ", isTaken=" + isBezet +
                ", playerNumber=" + spelerNummer +
                '}';
    }
}