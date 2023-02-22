package mai.scenes.game.logic;

import mai.datastructs.Stapel;

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
            //throw error?
            System.out.println("Starting size is too large");
        } else {
            cornerOne(startingSize);
            cornerTwo(startingSize);
        }
    }

    private void cornerOne(int size) {
        for (int x = 0; x < size; x++) {
            for (int y = yGroote - size; y < yGroote; y++) {
                System.out.println("X: " + x + " | Y: " + y + " | P: " + 1);
                bord[x][y].take(1);
            }
        }
    }

    private void cornerTwo(int size) {
        for (int x = xGroote - size; x < xGroote; x++) {
            for (int y = 0; y < size; y++) {
                System.out.println("X: " + x + " | Y: " + y + " | P: " + 2);
                bord[x][y].take(2);
            }
        }
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
                    AttackVectors attackVectors = getPossibleAttackSquare(select, 3, 2, playerNumber);
                    if (!attackVectors.possibleOneRangeAttackVectors().isEmpty() && !attackVectors.possibleTwoRangeAttackVectors().isEmpty()){
                        selectAble.push(select);
                    }
                }
            }
        }

        return selectAble;
    }

    public boolean checkBoard(int nextplayer) {
        System.out.println();
        System.out.println("NEXT PLAYER: " + nextplayer);
        System.out.println(checkPossibleAttacks(nextplayer));
        System.out.println(checkPlayerSquares());
        System.out.println(isFull());
        System.out.println();
        return checkPossibleAttacks(nextplayer) || checkPlayerSquares() || isFull();
    }

    public boolean checkPossibleAttacks(int nextPlayer){
        System.out.println("SCORE: " + (getPlayerSpaceCount(nextPlayer) < (xGroote * yGroote / 2)));
        System.out.println("SCORE: " + (getPlayerSpaceCount(nextPlayer) < (xGroote * yGroote / 2)));
        return getPlayerMoves(nextPlayer).isEmpty() && getPlayerSpaceCount(nextPlayer) < (xGroote * yGroote / 2);
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

    public AttackVectors getPossibleAttackDiagonal(Space space, int range, int attackDropOff, int playerNumber) {
        Stapel<Space> possibleOneRangeAttackVectors = new Stapel<>();
        Stapel<Space> possibleTwoRangeAttackVectors = new Stapel<>();

        for (int x = space.x - (range - 1); x < space.x + range; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = space.y - (range - 1); y < space.y + range; y++) {
                    if (y >= 0 && y < yGroote) {
                        if (!bord[x][y].isTaken()) {
//                        if (bord[x][y].getPlayerNumber() != playerNumber) {
                            int xDif, yDif;

                            if (space.x > x) {
                                xDif = space.x - x;
                            } else {
                                xDif = x - space.x;
                            }

                            if (space.y > y) {
                                yDif = space.y - y;
                            } else {
                                yDif = y - space.y;
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

    public Stapel<Space> getDiagonal(Space space, int range) {
        Stapel<Space> r = new Stapel<>();

        for (int x = space.x - range - 1; x < space.x + range; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = space.y - range - 1; y < space.y + range; y++) {
                    if (y >= 0 && y < yGroote) {
                        int xDif, yDif;

                        if (space.x > x) {
                            xDif = space.x - x;
                        } else {
                            xDif = x - space.x;
                        }

                        if (space.y > y) {
                            yDif = space.y - y;
                        } else {
                            yDif = y - space.y;
                        }

                        if (xDif + yDif < range) {
                            r.push(bord[x][y]);
                        }

                    }
                }
            }
        }
        return r;
    }

    public AttackVectors getPossibleAttackSquare(Space space, int range, int attackDropOff, int playerNumber) {
        Stapel<Space> possibleOneRangeAttackVectors = new Stapel<>();
        Stapel<Space> possibleTwoRangeAttackVectors = new Stapel<>();

        for (int x = space.x - (range - 1); x < space.x + range; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = space.y - (range - 1); y < space.y + range; y++) {
                    if (y >= 0 && y < yGroote) {
                        if (!bord[x][y].isTaken()) {
//                        if (bord[x][y].getPlayerNumber() != playerNumber) {
                            int xDif, yDif;

                            if (space.x > x) {
                                xDif = space.x - x;
                            } else {
                                xDif = x - space.x;
                            }

                            if (space.y > y) {
                                yDif = space.y - y;
                            } else {
                                yDif = y - space.y;
                            }

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

    public Stapel<Space> getSquare(Space space, int range) {
        Stapel<Space> r = new Stapel<>();

        for (int x = space.x - 1; x < space.x + 2; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = space.y - 1; y < space.y + 2; y++) {
                    if (y >= 0 && y < yGroote) {
                        r.push(bord[x][y]);
                    }
                }
            }
        }
        return r;
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


