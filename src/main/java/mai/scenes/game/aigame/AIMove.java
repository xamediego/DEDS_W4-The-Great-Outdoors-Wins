package mai.scenes.game.aigame;

import mai.scenes.game.logic.Plek;

public class AIMove {

    private final Plek oorsprong, selectie;

    public AIMove(Plek oorsprong, Plek selectie) {
        this.oorsprong = oorsprong;
        this.selectie = selectie;
    }

    public Plek getOorsprong() {
        return oorsprong;
    }

    public Plek getSelectie() {
        return selectie;
    }
}
