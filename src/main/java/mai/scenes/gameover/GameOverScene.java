package mai.scenes.gameover;

import mai.data.Player;
import mai.enums.MatchOverType;

import java.io.IOException;

public class GameOverScene {

    private GameOverController gameOverController;
    private GameOver gameOver;

    public GameOverScene(Player player1, Player player2, MatchOverType matchOverType, int playerOneScore, int playerTwoScore) {
        try {
            this.gameOverController = new GameOverController(player1, player2, matchOverType, playerOneScore, playerTwoScore);
            this.gameOver = new GameOver(this.gameOverController);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public GameOverController getController() {
        return gameOverController;
    }

    public GameOver getRoot(){
        return this.gameOver;
    }


}
