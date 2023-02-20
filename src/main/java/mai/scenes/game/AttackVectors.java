package mai.scenes.game;

import mai.datastructs.Stapel;

public record AttackVectors(Stapel<Space> possibleOneRangeAttackVectors, Stapel<Space> possibleTwoRangeAttackVectors) {
}
