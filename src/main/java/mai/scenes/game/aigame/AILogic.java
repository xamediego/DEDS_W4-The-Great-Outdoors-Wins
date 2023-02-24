package mai.scenes.game.aigame;

import mai.data.AI;
import mai.datastructs.Stapel;
import mai.enums.AIType;
import mai.exceptions.UnderflowException;
import mai.scenes.game.logic.AanvalsHoeken;
import mai.scenes.game.logic.GameBord;
import mai.scenes.game.logic.Plek;

import java.util.Random;

public class AILogic {

    public AIMove maakMove(GameBord gameBord, AI aiSpeler, int aiNummer, int tegenstanderNummer) throws UnderflowException {
        Stapel<Plek> selectAble = gameBord.getPlayerMoves(aiNummer);

        if (aiSpeler.getAiTypes().contains(AIType.NORMAL)) {
            return EasyAI.maakEasyMove(selectAble, gameBord, aiNummer, tegenstanderNummer, aiSpeler);
        } else if (aiSpeler.getAiTypes().contains(AIType.EASY)) {
            return EasyAI.maakEasyMove(selectAble, gameBord, aiNummer, tegenstanderNummer, aiSpeler);
        } else {
            return RandomAI.makeRandomMove(selectAble, gameBord, aiSpeler);
        }
    }
}

class EasyAI {
    public static AIMove maakEasyMove(Stapel<Plek> selecteerbaar, GameBord gameBord, int aiNummer, int tegenstandNummer, AI aiSpeler) {
        return selecteerMeestePuntenMove(selecteerbaar, gameBord, aiNummer, tegenstandNummer, aiSpeler);
    }

    // TODO: 24/02/2023 Implement check voor wanneer ai in gevaar is IE mogelijkverloren > huidige plekken
    public static AIMove selecteerMeestePuntenMove(Stapel<Plek> selecteerBaar, GameBord gameBord, int aiNummer, int tegenstanderNummer, AI aiSpeler) {
        Stapel<Plek> mogelijkeVerlorenPlekken = new Stapel<>();
        Stapel<Plek> mogelijkeSpelerEenMoves = gameBord.getPlayerMoves(tegenstanderNummer);

        int s = selecteerBaar.getGroote();

        for (int i = 0; i < s; i++) {
            try {
                if (!mogelijkeSpelerEenMoves.isLeeg()) {
                    Stapel<Plek> playerMoveSquare = gameBord.getInfected(mogelijkeSpelerEenMoves.pop(), tegenstanderNummer);

                    while (!playerMoveSquare.isLeeg()) {
                        Plek possibleAttack = playerMoveSquare.pop();
                        if (possibleAttack.getSpelerNummer() == 2 && !mogelijkeVerlorenPlekken.contains(possibleAttack)) {
                            mogelijkeVerlorenPlekken.push(possibleAttack);
                        }

                    }
                }
            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        if (mogelijkeVerlorenPlekken.getGroote() - gameBord.getPlayerSpaceCount(2) < 1) {
            return getMeestePuntenMove(selecteerBaar, gameBord, aiNummer, aiSpeler);
        } else {
            return getMeestePuntenMove(selecteerBaar, gameBord, aiNummer, aiSpeler);
        }
    }

    private static int hoogsteWaarde;

    private static AIMove getMeestePuntenMove(Stapel<Plek> possiblePlayerMoves, GameBord gameBord, int aiNummer, AI aiSpeler) {
        Stapel<AIMove> mogelijkeAanval = new Stapel<>();
        hoogsteWaarde = 0;

        while (!possiblePlayerMoves.isLeeg()) {
            try {
                Plek origin = possiblePlayerMoves.pop();

                AanvalsHoeken aanvalsHoeken = gameBord.getPossibleAttackSquare(origin, aiSpeler.getBereik(), aiSpeler.getMinBereik());

                mogelijkeAanval = getHoogsteAanvalsPlekkenVoorSelectie(aanvalsHoeken.mogelijkeKleinBereikAanval(), gameBord, origin, mogelijkeAanval, aiNummer);
                mogelijkeAanval = getHoogsteAanvalsPlekkenVoorSelectie(aanvalsHoeken.mogelijkeVerBereikAanval(), gameBord, origin, mogelijkeAanval, aiNummer);

            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        return getRandomMove(mogelijkeAanval);
    }


    private static Stapel<AIMove> getHoogsteAanvalsPlekkenVoorSelectie(Stapel<Plek> attackVectors, GameBord gameBord, Plek oorsprong, Stapel<AIMove> mogelijkeAanval, int aiNumber) {
        Plek mogelijkeAanvalSelectie;
        while (!attackVectors.isLeeg()) {
            try {

                mogelijkeAanvalSelectie = attackVectors.pop();

                Stapel<Plek> mogelijkeAanvalVergelijk = gameBord.getInfected(mogelijkeAanvalSelectie, aiNumber);


                if (mogelijkeAanvalVergelijk.getGroote() > hoogsteWaarde) {
                    hoogsteWaarde = mogelijkeAanvalVergelijk.getGroote();

                    mogelijkeAanval = new Stapel<>();
                    mogelijkeAanval.push(new AIMove(oorsprong, mogelijkeAanvalSelectie));
                } else if (mogelijkeAanvalVergelijk.getGroote() == hoogsteWaarde) {
                    mogelijkeAanval.push(new AIMove(oorsprong, mogelijkeAanvalSelectie));
                }

            } catch (UnderflowException e) {
                e.printStackTrace();
            }
        }

        return mogelijkeAanval;
    }

    private static AIMove getRandomMove(Stapel<AIMove> moves) {
        Random random = new Random();

        AIMove aiMove = null;

        try {
            aiMove = moves.peek();
        } catch (UnderflowException e) {
            e.printStackTrace();
        }

        if (moves.getGroote() > 1) {
            int r = random.nextInt(1, moves.getGroote());

            for (int i = 0; i < r; i++) {
                try {
                    aiMove = moves.pop();
                } catch (UnderflowException e) {
                    e.printStackTrace();
                }
            }

        }

        return aiMove;
    }
}

class RandomAI {

    public static AIMove makeRandomMove(Stapel<Plek> selectAble, GameBord gameBord, AI ai) throws UnderflowException {
        Random random = new Random();

        int size = selectAble.getGroote();

        Plek select = getAttackSpace(selectAble, size);

        AanvalsHoeken aanvalsHoeken = gameBord.getPossibleAttackSquare(select, ai.getBereik(), ai.getMinBereik());

        if (aanvalsHoeken.mogelijkeVerBereikAanval().getGroote() < 1) {
            return getRandomMove(aanvalsHoeken.mogelijkeKleinBereikAanval(), select, random.nextInt(aanvalsHoeken.mogelijkeKleinBereikAanval().getGroote()));
        } else if (aanvalsHoeken.mogelijkeKleinBereikAanval().getGroote() < 1) {
            return getRandomMove(aanvalsHoeken.mogelijkeVerBereikAanval(), select, random.nextInt(aanvalsHoeken.mogelijkeVerBereikAanval().getGroote()));
        }

        if (random.nextInt(2) == 0) {
            return getRandomMove(aanvalsHoeken.mogelijkeKleinBereikAanval(), select, random.nextInt(aanvalsHoeken.mogelijkeKleinBereikAanval().getGroote()));
        } else {
            return getRandomMove(aanvalsHoeken.mogelijkeVerBereikAanval(), select, random.nextInt(aanvalsHoeken.mogelijkeVerBereikAanval().getGroote()));
        }
    }

    private static Plek getAttackSpace(Stapel<Plek> selectAble, int rate) throws UnderflowException {
        Plek select = selectAble.peek();

        if (selectAble.getGroote() > 1) {
            for (int i = 0; i < rate; i++) {
                select = selectAble.pop();
            }
        }

        return select;
    }

    private static AIMove getRandomMove(Stapel<Plek> attackVectors, Plek origin, int random) throws UnderflowException {
        Plek select = attackVectors.peek();

        if (attackVectors.getGroote() > 1) {
            for (int i = 0; i < random; i++) {
                select = attackVectors.pop();
            }
        }

        return new AIMove(origin, select);
    }
}
