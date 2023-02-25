package mai.bootstrap;


import mai.data.AI;
import mai.enums.Difficulty;
import mai.service.AIService;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;




public class GenerateData {

    public static void createUserWithImage() throws URISyntaxException, IOException {
        ClassLoader classLoader = GenerateData.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("usercrops");

        Random random = new Random();

        String colour = "#d14957";

        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            int i = 0;

            while ((line = reader.readLine()) != null) {
                AI ai = new AI(
                        FilenameUtils.removeExtension(line),
                        colour,
                        "/usercrops/"+ line,
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
                i++;
            }
            reader.close();
        }

    }

}