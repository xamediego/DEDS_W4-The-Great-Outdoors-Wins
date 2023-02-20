package mai.scenes.game;

import mai.datastructs.Stapel;

public class GameBoard {

    private final Space[][] bord;

    public final int xGroote, yGroote;

    public GameBoard(int xGroote, int yGroote) {
        this.bord = new Space[xGroote][yGroote];
        this.xGroote = xGroote;
        this.yGroote = yGroote;

        configBoard();
    }

    public Space[][] getBord() {
        return bord;
    }

    private void configBoard() {
        for (int x = 0; x < xGroote; x++) {
            for (int y = 0; y < yGroote; y++) {
                bord[x][y] = new Space(x, y);
            }
        }
    }

    public void setInitialOccupied(int startingSize){
        if(startingSize * 2 > yGroote || startingSize * 2 > xGroote){
            //throw error?
            System.out.println("Starting size is too large");
        } else {
            cornerOne(startingSize);
            cornerTwo(startingSize);
        }
    }

    private void cornerOne(int size){
        for(int x = 0; x < size; x++){
            for(int y = yGroote - size; y < yGroote; y++){
                bord[x][y].take(1);
            }
        }
    }

    private void cornerTwo(int size){
        for(int x = xGroote - size; x < xGroote; x++){
            for(int y = 0; y < size; y++){
                bord[x][y].take(2);
            }
        }
    }

    public Stapel<Space> getPlayerSpaces(int playerNumber){
        Stapel<Space> selectAble = new Stapel<>();
        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {
                if (bord[x][y].getPlayerNumber() == playerNumber) {
                    selectAble.push(new Space(x, y));
                }
            }
        }

        return selectAble;
    }

    public AttackVectors getPossibleAttackDiagonal(Vector2D vector2D, int range, int attackDropOff) {
        Stapel<Space> possibleOneRangeAttackVectors = new Stapel<>();
        Stapel<Space> possibleTwoRangeAttackVectors = new Stapel<>();

        for (int x = vector2D.x - (range - 1); x < vector2D.x + range; x++) {

            if (x >= 0 && x < yGroote) {
                for (int y = vector2D.y - (range - 1); y < vector2D.y + range; y++) {

                    if (y >= 0 && y < xGroote) {
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

    public Stapel<Space> getDeselect(Space selectedSpace) {
        Stapel<Space> deselect = new Stapel<>();

        for (int y = selectedSpace.y - 2; y < selectedSpace.y + 3; y++) {
            if (y >= 0 && y < yGroote) {
                for (int x = selectedSpace.x - 2; x < selectedSpace.x + 3; x++) {
                    if (x >= 0 && x < xGroote) {
                        if (!bord[x][y].isTaken()) {
                            deselect.push(bord[x][y]);
                        }
                    }
                }
            }
        }

        return deselect;
    }
}

