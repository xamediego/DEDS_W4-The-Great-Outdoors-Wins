package mai.scenes.game.logic;

import mai.datastructs.Stapel;


public record AttackVectors(Stapel<Space> possibleOneRangeAttackVectors, Stapel<Space> possibleTwoRangeAttackVectors) {
}
