package mai.scenes.gameover;

import mai.data.User;
import mai.enums.GameOverType;

public record GameOverData(GameOverType gameOverType, User user1, User user2,
                           int user1Score, int user2Score, int turnCount) {

    public int getUser1Score() {
        return user1Score;
    }

    public int getUser2Score() {
        return user2Score;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public GameOverType getGameOverType() {
        return gameOverType;
    }
}
