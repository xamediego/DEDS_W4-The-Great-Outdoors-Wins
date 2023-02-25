package mai.scenes.game.logic;

import mai.scenes.game.data.Space;

public class AIMove {
    private final Space origin, selected;

    public AIMove(Space origin, Space selected) {
        this.origin = origin;
        this.selected = selected;
    }

    public Space getOrigin() {
        return origin;
    }

    public Space getSelected() {
        return selected;
    }
}
