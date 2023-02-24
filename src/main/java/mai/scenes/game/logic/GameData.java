package mai.scenes.game.logic;

import mai.data.AI;
import mai.data.Speler;

public class GameData {

    private int spelerEenScore, spelerTweeScore;
    private int turnNummer;

    private final Speler spelerEen;
    private final AI spelerTwee;

    public boolean spelerEenKlaar = false, spelerTweeKlaar = false;

    public Speler huidigeSpeler;
    public GameBord gameBord;

    /**
     * Current player should be set manually
     *
     * @param spelerEenScore default score for player 1
     * @param spelerTweeScore default score for player 2
     * @param turnNummer     initial turn number
     * @param spelerEen        data container for player 1
     * @param spelerTwee        data container for player 2
     * @param gameBord      game board used by this game data
     */
    public GameData(int spelerEenScore, int spelerTweeScore, int turnNummer, Speler spelerEen, AI spelerTwee, GameBord gameBord) {
        this.spelerEenScore = spelerEenScore;
        this.spelerTweeScore = spelerTweeScore;
        this.turnNummer = turnNummer;
        this.spelerEen = spelerEen;
        this.spelerTwee = spelerTwee;
        this.gameBord = gameBord;
    }

    public int getSpelerEenScore() {
        return spelerEenScore;
    }

    public int getSpelerTweeScore() {
        return spelerTweeScore;
    }

    public int getTurnNummer() {
        return turnNummer;
    }

    public void increaseTurnNumber() {this.turnNummer++;}

    public Speler getSpelerEen() {
        return spelerEen;
    }

    public AI getSpelerTwee() {
        return spelerTwee;
    }

    public void verhoogSpelerEenPunten(int score) {
        spelerEenScore += score;
    }

    public void verhoogSpelerTweePunten(int score) {
        spelerTweeScore += score;
    }

    public void decreasePlayerOneScore(int score) {
        spelerEenScore -= score;
    }

    public void decreasePlayerTwoScore(int score) {
        spelerTweeScore -= score;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "playerOneScore=" + spelerEenScore +
                ", playerTwoScore=" + spelerTweeScore +
                ", turnNumber=" + turnNummer +
                ", player1=" + spelerEen.getNaam() +
                ", player2=" + spelerTwee.getNaam() +
                ", player1Finished=" + spelerEenKlaar +
                ", player2Finished=" + spelerTweeKlaar +
                ", currentPlayer=" + huidigeSpeler.getNaam() +
                '}';
    }
}
