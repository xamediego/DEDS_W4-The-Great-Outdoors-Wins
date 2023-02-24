package mai.scenes.game.logic;

import mai.datastructs.Stapel;
import mai.exceptions.UnderflowException;

public class GameBord {

    private final Plek[][] bord;

    public int xGroote, yGroote;

    public GameBord(int xGroote, int yGroote, Plek[][] bord) {
        this.bord = bord;
        this.xGroote = xGroote;
        this.yGroote = yGroote;
    }

    public Plek[][] getBord() {
        return bord;
    }

    public void configBoard() {
        for (int x = 0; x < xGroote; x++) {
            for (int y = 0; y < yGroote; y++) {
                bord[x][y] = new Plek(x, y);
            }
        }
    }

    public void setInitialOccupied(int startingSize) {
        if (startingSize * 2 > yGroote || startingSize * 2 > xGroote) {
            //throw error?
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

    public boolean checkBoard(int nextplayer) {
        return checkPossibleAttacks(nextplayer) || checkPlayerSquares() || isFull();
    }

    public boolean checkPossibleAttacks(int nextPlayer) {
        return getPlayerMoves(nextPlayer).isLeeg();
    }

    public boolean checkScore(int nextPlayer) {
        return getPlayerSpaceCount(nextPlayer) > ((xGroote * yGroote) / 2);
    }

    public boolean checkPlayerSquares() {
        int player1Count = 0;
        int player2Count = 0;

        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {
                if (bord[x][y].getSpelerNummer() == 1) {
                    player1Count++;
                } else if (bord[x][y].getSpelerNummer() == 2) {
                    player2Count++;
                }
            }
        }

        return player1Count == 0 || player2Count == 0;
    }

    public boolean isFull() {
        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {
                if (!bord[x][y].isBezet()) return false;
            }
        }
        return true;
    }

    public int getPlayerSpaceCount(int playerNumber) {
        int count = 0;
        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {
                if (bord[x][y].getSpelerNummer() == playerNumber) {
                    count++;
                }
            }
        }
        return count;
    }

    public Stapel<Plek> getPlayerMoves(int playerNumber) {
        Stapel<Plek> selectAble = new Stapel<>();
        for (int y = 0; y < yGroote; y++) {
            for (int x = 0; x < xGroote; x++) {

                if (bord[x][y].getSpelerNummer() == playerNumber) {
                    Plek select = new Plek(x, y, true, playerNumber);
                    AanvalsHoeken aanvalsHoeken = getPossibleAttackSquare(select, 3, 2);
                    if (!aanvalsHoeken.mogelijkeKleinBereikAanval().isLeeg() || !aanvalsHoeken.mogelijkeVerBereikAanval().isLeeg()) {
                        selectAble.push(select);
                    }

                }
            }
        }
        return selectAble;
    }

    public Stapel<Plek> getDeselect(Plek selectedPlek, int range) {
        Stapel<Plek> deselect = new Stapel<>();

        for (int y = selectedPlek.y - range - 1; y < selectedPlek.y + range; y++) {
            if (y >= 0 && y < yGroote) {
                for (int x = selectedPlek.x - range - 1; x < selectedPlek.x + range; x++) {
                    if (x >= 0 && x < xGroote) {
                        if (!bord[x][y].isBezet()) {
                            deselect.push(bord[x][y]);
                        }
                    }
                }
            }
        }
        return deselect;
    }


    public AanvalsHoeken getPossibleAttackDiagonal(Plek plek, int range, int attackDropOff) {
        Stapel<Plek> possibleOneRangeAttackVectors = new Stapel<>();
        Stapel<Plek> possibleTwoRangeAttackVectors = new Stapel<>();

        for (int x = plek.x - (range - 1); x < plek.x + range; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = plek.y - (range - 1); y < plek.y + range; y++) {
                    if (y >= 0 && y < yGroote) {
                        if (!bord[x][y].isBezet()) {
                            int xDif = getDis(plek.x, x);
                            int yDif = getDis(plek.y, y);

                            if (xDif + yDif < range) {
                                if (xDif + yDif < attackDropOff) {
                                    possibleOneRangeAttackVectors.push(new Plek(x, y));
                                } else if (xDif + yDif < range) {
                                    possibleTwoRangeAttackVectors.push(new Plek(x, y));
                                }
                            }
                        }
                    }

                }
            }
        }

        return new AanvalsHoeken(possibleOneRangeAttackVectors, possibleTwoRangeAttackVectors);
    }

    public Stapel<Plek> getDiagonal(Plek plek, int range) {
        Stapel<Plek> r = new Stapel<>();

        for (int x = plek.x - range - 1; x < plek.x + range; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = plek.y - range - 1; y < plek.y + range; y++) {
                    if (y >= 0 && y < yGroote) {
                        int xDif = getDis(plek.x, x);
                        int yDif = getDis(plek.y, y);

                        if (xDif + yDif < range) {
                            r.push(bord[x][y]);
                        }

                    }
                }
            }
        }
        return r;
    }

    public AanvalsHoeken getPossibleAttackSquare(Plek plek, int range, int attackDropOff) {
        Stapel<Plek> possibleOneRangeAttackVectors = new Stapel<>();
        Stapel<Plek> possibleTwoRangeAttackVectors = new Stapel<>();

        for (int x = plek.x - (range - 1); x < plek.x + range; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = plek.y - (range - 1); y < plek.y + range; y++) {
                    if (y >= 0 && y < yGroote) {
                        if (!bord[x][y].isBezet()) {
                            int xDif = getDis(plek.x, x);
                            int yDif = getDis(plek.y, y);

                            if (xDif < attackDropOff && yDif < attackDropOff) {
                                possibleOneRangeAttackVectors.push(new Plek(x, y));
                            } else {
                                possibleTwoRangeAttackVectors.push(new Plek(x, y));
                            }

                        }
                    }
                }
            }
        }
        return new AanvalsHoeken(possibleOneRangeAttackVectors, possibleTwoRangeAttackVectors);
    }

    public Stapel<Plek> getSquare(Plek plek, int infectRange) {
        Stapel<Plek> r = new Stapel<>();

        for (int x = plek.x - (infectRange - 1); x < plek.x + infectRange; x++) {
            if (x >= 0 && x < xGroote) {
                for (int y = plek.y - (infectRange - 1); y < plek.y + infectRange; y++) {
                    if (y >= 0 && y < yGroote) {
                        r.push(bord[x][y]);
                    }
                }
            }
        }
        return r;
    }

    public void move(Plek origin, Plek select, int attackDropOff, int range, int playerNumber) {
        int distance = getDis(origin.x, select.x) + getDis(origin.y, select.y);

        if (distance < attackDropOff) {
            shortRangeAttack(select, playerNumber);
        } else if (distance < range) {
            longRangeAttack(origin, select, playerNumber);
        }
    }

    public void shortRangeAttack(Plek select, int playerNumber) {
        setInfected(getInfected(select, playerNumber), playerNumber);
    }

    public void longRangeAttack(Plek origin, Plek select, int playerNumber) {
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

    public Stapel<Plek> getInfected(Plek select, int playerNumber) {
        Stapel<Plek> spaceSelectSquare = getSquare(select, 2);
        Stapel<Plek> returnStack = new Stapel<>();

        int size = spaceSelectSquare.getGroote();

        for (int i = 0; i < size; i++) {
            try {
                Plek t = spaceSelectSquare.pop();
                if (t.getSpelerNummer() != 0 && t.getSpelerNummer() != playerNumber) {
                    returnStack.push(t);
                }
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        return returnStack;
    }

    public void setInfected(Stapel<Plek> spaces, int playerNumber) {
        int s = spaces.getGroote();

        for (int i = 0; i < s; i++) {
            try {
                Plek t = spaces.pop();
                bord[t.x][t.y].take(playerNumber);
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }
    }

    public void setInfected(Plek plek, int playerNumber) {
        bord[plek.x][plek.y].take(playerNumber);
    }


    public Plek[][] copyBord() {
        Plek[][] copy = new Plek[xGroote][yGroote];

        for (int x = 0; x < xGroote; x++) {
            for (int y = 0; y < yGroote; y++) {
                if (bord[x][y].isBezet()) {
                    if (bord[x][y].getSpelerNummer() == 1) {
                        copy[x][y] = new Plek(x, y);
                        copy[x][y].take(1);
                    } else {
                        copy[x][y] = new Plek(x, y);
                        copy[x][y].take(2);
                    }
                } else {
                    copy[x][y] = new Plek(x, y);
                }
            }
        }

        return copy;
    }

    public void print() {
        for (int x = 0; x < xGroote; x++) {
            for (int y = 0; y < yGroote; y++) {
                if (bord[x][y].isBezet()) {
                    if (bord[x][y].getSpelerNummer() == 1) {
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


