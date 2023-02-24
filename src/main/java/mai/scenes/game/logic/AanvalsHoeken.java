package mai.scenes.game.logic;

import mai.datastructs.Stapel;


public record AanvalsHoeken(Stapel<Plek> mogelijkeKleinBereikAanval, Stapel<Plek> mogelijkeVerBereikAanval) {
}
