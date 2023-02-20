package mai.scenes.game;

import mai.datastructs.Stapel;

public class GameData {

    private Space[][] bord;

    private final int xGroote, yGroote;

    public GameData(int xGroote, int yGroote) {
        this.bord = new Space[xGroote][yGroote];
        this.xGroote = xGroote;
        this.yGroote = yGroote;
    }

    public AttackVectors getPossibleDiagonal(Vector2D vector2D, int range, int attackDropOff) {
        Stapel<Space> possibleOneRangeAttackVectors = new Stapel<>();
        Stapel<Space> possibleTwoRangeAttackVectors = new Stapel<>();

        for (int y = vector2D.y - (attackDropOff - 1); y < vector2D.y + attackDropOff; y++) {
            if (y >= 0 && y < yGroote) {
                for (int x = vector2D.x - (attackDropOff - 1); x < vector2D.x + attackDropOff; x++) {
                    if (x >= 0 && x < xGroote) {
                        if (!bord[x][y].isTaken()) {
                            int xDif, yDif;

                            if (vector2D.x > x) {
                                xDif = vector2D.x - x;
                            } else {
                                xDif = x - vector2D.x;
                            }

                            if (vector2D.y > y) {
                                yDif = vector2D.y - y;
                            } else {
                                yDif = y - vector2D.y;
                            }

                            if (xDif + yDif < range) {
                                if (xDif + yDif < attackDropOff) {
                                    possibleOneRangeAttackVectors.push(new Space(x, y));
                                } else if (xDif + yDif < range) {
                                    possibleTwoRangeAttackVectors.push(new Space(x, y));
                                }
                            }

                        }
                    }
                }
            }
        }

        return new AttackVectors(possibleOneRangeAttackVectors, possibleTwoRangeAttackVectors);
    }
}

class AttackVectors {

    public final Stapel<Space> possibleOneRangeAttackVectors;
    public final Stapel<Space> possibleTwoRangeAttackVectors;

    AttackVectors(Stapel<Space> possibleOneRangeAttackVectors, Stapel<Space> possibleTwoRangeAttackVectors) {
        this.possibleOneRangeAttackVectors = possibleOneRangeAttackVectors;
        this.possibleTwoRangeAttackVectors = possibleTwoRangeAttackVectors;
    }

}

