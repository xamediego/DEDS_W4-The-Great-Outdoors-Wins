package mai.bootstrap;


import mai.data.AI;
import mai.data.User;
import mai.enums.Difficulty;
import mai.service.AIService;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;


public class GenerateData {

    public static void createUserWithImage() {
        File dir = new File("src/main/resources/usercrops");

        Random random = new Random();

        String colour = "#d14957";

        for (int i = 0; i < Objects.requireNonNull(dir.listFiles()).length; i += 1) {
            AI ai = new AI(
                    FilenameUtils.removeExtension(Objects.requireNonNull(dir.listFiles())[i].getName()),
                    colour,
                    Objects.requireNonNull(dir.listFiles())[i].getAbsolutePath(),
                    2,
                    2,
                    3
            );

            if (i == 0) {
                ai.setAiTypes(new HashSet<>(Set.of(Difficulty.JOURNALIST)));
            } else if (i == 1) {
                ai.setAiTypes(new HashSet<>(Set.of(Difficulty.EASY)));
            } else if (i == 2) {
                ai.setAiTypes(new HashSet<>(Set.of(Difficulty.NORMAL)));
            } else {
                ai.setAiTypes(new HashSet<>(Set.of(Difficulty.values()[random.nextInt(3)])));
            }

            AIService.aiList.add(ai);
        }

    }

}