package mai.scenes.game.data;

import mai.datastructs.Stack;


public class AttackVectors {
    public Stack<Space> possibleOneRangeAttackVectors;
    public Stack<Space> possibleTwoRangeAttackVectors;

    public AttackVectors(Stack<Space> possibleOneRangeAttackVectors, Stack<Space> possibleTwoRangeAttackVectors) {
        this.possibleOneRangeAttackVectors = possibleOneRangeAttackVectors;
        this.possibleTwoRangeAttackVectors = possibleTwoRangeAttackVectors;
    }
}

