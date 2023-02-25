package mai.service;

import mai.data.AI;
import mai.enums.Difficulty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AIService {

    public static List<AI> aiList = new ArrayList<>();

    private static AI getByDifficulty(Difficulty type) {
        List<AI> aiPlayers = aiList.stream().filter(ai -> ai.getAiTypes().contains(type)).collect(Collectors.toList());

        Random random = new Random();

        return aiPlayers.get(random.nextInt(aiPlayers.size()));
    }

    public static AI getAiPlayer(Difficulty type, int pNumber) {
        AI ai = getByDifficulty(type);
        ai.setPlayerNumber(pNumber);
        return ai;
    }
}
