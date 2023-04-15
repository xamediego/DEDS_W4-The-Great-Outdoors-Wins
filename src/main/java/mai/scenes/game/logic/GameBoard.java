package mai.scenes.game.logic;

import mai.datastructs.Stapel;
import mai.exceptions.UnderflowException;

public class GameBoard {

    private final Space[][] bord;

    public int xGroote, yGroote;

    public GameBoard(int xGroote, int yGroote, Space[][] bord) {
        this.bord = bord;
        this.xGroote = xGroote;
        this.yGroote = yGroote;
    }

    public Space[][] getBord() {
        return bord;
    }

    public void configBoard() {
        for (int x = 0; x < xGroote; x++) {
            for (int y = 0; y < yGroote; y++) {
                bord[x][y] = new Space(x, y);
            }
        }
    }

    public void setInitialOccupied(int startingSize) {
        if (startingSize * 2 > yGroote || startingSize * 2 > xGroote) {
            //throw error because starting size is larger than board size?
        } else {
            cornerOne(startingSize);
            cornerTwo(startingSize);
        }
    }

    private void cornerOne(int size) {
        for (int x = 0; x < size; x++) {
            for (int y = yGroote - size; y < yGroote; y++) {
                bord[x][y].take(1);
            }
        }
    }

    private void cornerTwo(int size) {
        for (int x = xGroote - size; x < xGroote; x++) {
            for (int y = 0; y < size; y++) {
                bord[x][y].take(2);
            }
        }
    }

    public boolean checkBoard(int nextplayer ) {
        return checkPossibleAttacks(nextplayer) || checkPlayerSquares() || isFull();
    }

    public boolean checkPossibleAttacks(int nextPlayer) {
        return getPlayerMoves(nextPlayer).isEmpty();
    }

    public boolean checkScore(int nextPlayer) {
        return getPlayerSpaceCount(nextPlayer) > ((xGroote * yGroote) / 2);
    }

    public boolean checkPlayerSquares() {
        int player1Count = 0;
        int player2Count = 0;

        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {
                if (bord[x][y].getPlayerNumber() == 1) {
                    player1Count++;
                } else if (bord[x][y].getPlayerNumber() == 2) {
                    player2Count++;
                }
            }
        }

        return player1Count == 0 || player2Count == 0;
    }

    public boolean isFull() {
        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {
                if (!bord[x][y].isTaken()) return false;

            }
        }
        return true;
    }

    public int getPlayerSpaceCount(int playerNumber) {
        int count = 0;
        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {
                if (bord[x][y].getPlayerNumber() == playerNumber) {
                    count++;
                }
            }
        }
        return count;
    }

    public Stapel<Space> getPlayerMoves(int playerNumber) {
        Stapel<Space> selectAble = new Stapel<>();
        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {

                if (bord[x][y].getPlayerNumber() == playerNumber) {
                    Space select = new Space(x, y, true, playerNumber);
                    AttackVectors attackVectors = getPossibleAttackSquare(select, 3, 2);
                    if (!attackVectors.possibleOneRangeAttackVectors().isEmpty() || !attackVectors.possibleTwoRangeAttackVectors().isEmpty()) {
                        selectAble.push(select);
                    }

                }
            }
        }
        return selectAble;
    }

    public Stapel<Space> getDeselect(Space selectedSpace, int range) {
        Stapel<Space> deselect = new Stapel<>();

        for (int y = selectedSpace.y - range - 1; y < selectedSpace.y + range; y++) {
            if (y >= 0 && y < yGroote) {
                for (int x = selectedSpace.x - range - 1; x < selectedSpace.x + range; x++) {
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


    public AttackVectors getPossibleAttackDiagonal(Space space, int range, int attackDropOff) {
        Stapel<Space> possibleOneRangeAttackVectors = new Stapel<>();
        Stapel<Space> possibleTwoRangeAttackVectors = new Stapel<>();

        for (int x = space.x - (range - 1); x < space.x + range; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = space.y - (range - 1); y < space.y + range; y++) {
                    if (y >= 0 && y < yGroote) {
                        if (!bord[x][y].isTaken()) {
                            int xDif = getDis(space.x, x);
                            int yDif = getDis(space.y, y);

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

    public Stapel<Space> getDiagonal(Space space, int range) {
        Stapel<Space> r = new Stapel<>();

        for (int x = space.x - range - 1; x < space.x + range; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = space.y - range - 1; y < space.y + range; y++) {
                    if (y >= 0 && y < yGroote) {
                        int xDif = getDis(space.x, x);
                        int yDif = getDis(space.y, y);

                        if (xDif + yDif < range) {
                            r.push(bord[x][y]);
                        }

                    }
                }
            }
        }
        return r;
    }

    public AttackVectors getPossibleAttackSquare(Space space, int range, int attackDropOff) {
        Stapel<Space> possibleOneRangeAttackVectors = new Stapel<>();
        Stapel<Space> possibleTwoRangeAttackVectors = new Stapel<>();

        for (int x = space.x - (range - 1); x < space.x + range; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = space.y - (range - 1); y < space.y + range; y++) {
                    if (y >= 0 && y < yGroote) {
                        if (!bord[x][y].isTaken()) {
                            int xDif = getDis(space.x, x);
                            int yDif = getDis(space.y, y);

                            if (xDif < attackDropOff && yDif < attackDropOff) {
                                possibleOneRangeAttackVectors.push(new Space(x, y));
                            } else {
                                possibleTwoRangeAttackVectors.push(new Space(x, y));
                            }

                        }
                    }
                }
            }
        }
        return new AttackVectors(possibleOneRangeAttackVectors, possibleTwoRangeAttackVectors);
    }

    public Stapel<Space> getSquare(Space space, int infectRange) {
        Stapel<Space> r = new Stapel<>();

        for (int x = space.x - (infectRange - 1); x < space.x + infectRange; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = space.y - (infectRange - 1); y < space.y + infectRange; y++) {
                    if (y >= 0 && y < yGroote) {
                        r.push(bord[x][y]);
                    }
                }
            }
        }
        return r;
    }

    public void move(Space origin, Space select, int attackDropOff, int range, int playerNumber) {
        int distance = getDis(origin.x, select.x) + getDis(origin.y, select.y);

        if (distance < attackDropOff) {
            shortRangeAttack(select, playerNumber);
        } else if (distance < range) {
            longRangeAttack(origin, select, playerNumber);
        }
    }

    public void shortRangeAttack(Space select, int playerNumber) {
        setInfected(getInfected(select, playerNumber), playerNumber);
    }

    public void longRangeAttack(Space origin, Space select, int playerNumber) {
        bord[origin.x][origin.y].deselect();
        setInfected(getInfected(select, playerNumber), playerNumber);
    }


    public int getDis(int origin, int destination) {
        if (origin > destination) {
            return origin - destination;
        } else {
            return destination - origin;
        }
    }

    public Stapel<Space> getInfected(Space select, int playerNumber) {
        Stapel<Space> spaceSelectSquare = getSquare(select, 2);
        Stapel<Space> returnStack = new Stapel<>();

        int size = spaceSelectSquare.getSize();

        for (int i = 0; i < size; i++) {
            try {
                Space t = spaceSelectSquare.pop();
                if (t.getPlayerNumber() != 0 && t.getPlayerNumber() != playerNumber) {
                    returnStack.push(t);
                }
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        return returnStack;
    }

    public void setInfected(Stapel<Space> spaces, int playerNumber) {
        int s = spaces.getSize();

        for (int i = 0; i < s; i++) {
            try {
                Space t = spaces.pop();
                bord[t.x][t.y].take(playerNumber);
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    public void setInfected(Space space, int playerNumber) {
        bord[space.x][space.y].take(playerNumber);
    }


    public Space[][] copyBord() {
        Space[][] copy = new Space[xGroote][yGroote];

        for (int x = 0; x < xGroote; x++) {
            for (int y = 0; y < yGroote; y++) {
                if (bord[x][y].isTaken()) {
                    if (bord[x][y].getPlayerNumber() == 1) {
                        copy[x][y] = new Space(x, y);
                        copy[x][y].take(1);
                    } else {
                        copy[x][y] = new Space(x, y);
                        copy[x][y].take(2);
                    }
                } else {
                    copy[x][y] = new Space(x, y);
                }
            }
        }

        return copy;
    }

    public void print() {
        for (int x = 0; x < xGroote; x++) {
            for (int y = 0; y < yGroote; y++) {
                if (bord[x][y].isTaken()) {
                    if (bord[x][y].getPlayerNumber() == 1) {
                        System.out.println("X " + x + " Y " + y + " TAKEN PLAYER " + 1);
                    } else {
                        System.out.println("X " + x + " Y " + y + " TAKEN PLAYER " + 2);
                    }
                } else {
                    System.out.println("X " + x + " Y " + y + " FREE ");
                }
            }
        }
    }
}


