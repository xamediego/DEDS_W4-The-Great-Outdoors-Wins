package mai.service;

import mai.data.AI;
import mai.data.Player;
import mai.enums.DIFFICULTY;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIService {

    public static List<AI> aiList = new ArrayList<>();

    private static AI getByDifficulty(DIFFICULTY type) {
        List<AI> aiPlayers = aiList.stream().filter(ai -> ai.getAiTypes().contains(type)).toList();

        Random random = new Random();

        return aiPlayers.get(random.nextInt(aiPlayers.size()));
    }

    public static Player getAiPlayer(DIFFICULTY type) {
        AI ai = getByDifficulty(type);

        return new Player(2, ai.getPlayerName(), ai.getPlayerColour(), ai.getProfilePictureUrl());
    }

}
