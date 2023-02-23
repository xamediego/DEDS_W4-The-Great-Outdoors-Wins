package mai.scenes.game.logic;

import mai.data.AI;
import mai.data.User;

public class GameData {

    private int playerOneScore, playerTwoScore;
    private int turnNumber;

    private final User player1;
    private final AI player2;

    public boolean player1Finished = false, player2Finished = false;

    public User currentPlayer;
    public GameBoard gameBoard;

    /**
     * Current player should be set manually
     *
     * @param playerOneScore default score for player 1
     * @param playerTwoScore default score for player 2
     * @param turnNumber     initial turn number
     * @param player1        data container for player 1
     * @param player2        data container for player 2
     * @param gameBoard      game board used by this game data
     */
    public GameData(int playerOneScore, int playerTwoScore, int turnNumber, User player1, AI player2, GameBoard gameBoard) {
        this.playerOneScore = playerOneScore;
        this.playerTwoScore = playerTwoScore;
        this.turnNumber = turnNumber;
        this.player1 = player1;
        this.player2 = player2;
        this.gameBoard = gameBoard;
    }

    public int getPlayerOneScore() {
        return playerOneScore;
    }

    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void increaseTurnNumber() {this.turnNumber++;}

    public User getPlayer1() {
        return player1;
    }

    public AI getPlayer2() {
        return player2;
    }

    public void increasePlayerOneScore(int score) {
        playerOneScore += score;
    }

    public void increasePlayerTwoScore(int score) {
        playerTwoScore += score;
    }

    public void decreasePlayerOneScore(int score) {
        playerOneScore -= score;
    }

    public void decreasePlayerTwoScore(int score) {
        playerTwoScore -= score;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "playerOneScore=" + playerOneScore +
                ", playerTwoScore=" + playerTwoScore +
                ", turnNumber=" + turnNumber +
                ", player1=" + player1.getPlayerName() +
                ", player2=" + player2.getPlayerName() +
                ", player1Finished=" + player1Finished +
                ", player2Finished=" + player2Finished +
                ", currentPlayer=" + currentPlayer.getPlayerName() +
                '}';
    }
}
