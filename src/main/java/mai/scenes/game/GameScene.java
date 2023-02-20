package mai.scenes.game;

import mai.data.Player;

import java.io.IOException;

public class GameScene {

    private GameController gameController;
    private Game game;

    public GameScene(Player player1, Player player2, int xSize, int ySize, int spaceSize) {
        try {
            this.gameController = new GameController(player1, player2, xSize, ySize, spaceSize);
            this.game = new Game(this.gameController);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public GameController getController() {
        return gameController;
    }

    public Game getRoot(){
        return this.game;
    }

}
