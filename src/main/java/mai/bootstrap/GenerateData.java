package mai.bootstrap;


import mai.data.AI;
import mai.enums.DIFFICULTY;
import mai.service.AIService;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;


public class GenerateData {

    public static void createUserWithImage() {

        //find way to switch this to the normal resource folder
        File dir = new File("src/test/resources/usercrops");

        Random random = new Random();

        String colour = "#d14957";

        for (int i = 0; i < Objects.requireNonNull(dir.listFiles()).length; i += 1) {
            AI ai = new AI(FilenameUtils.removeExtension(Objects.requireNonNull(dir.listFiles())[i].getName()), colour, Objects.requireNonNull(dir.listFiles())[i].getAbsolutePath());

            if (i == 0) {
                ai.setAiTypes(new HashSet<>(Set.of(DIFFICULTY.JOURNALIST)));
            } else if (i == 1) {
                ai.setAiTypes(new HashSet<>(Set.of(DIFFICULTY.EASY)));
            } else if (i == 2) {
                ai.setAiTypes(new HashSet<>(Set.of(DIFFICULTY.NORMAL)));
            } else {
                ai.setAiTypes(new HashSet<>(Set.of(DIFFICULTY.values()[random.nextInt(3)])));
            }

            AIService.aiList.add(ai);
        }

    }

}
