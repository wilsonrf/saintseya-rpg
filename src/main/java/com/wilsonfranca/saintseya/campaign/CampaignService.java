package com.wilsonfranca.saintseya.campaign;

import com.wilsonfranca.saintseya.QuestPart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by wilson on 15/04/18.
 */
public class CampaignService {

    public QuestPart loadQuestPart(String id) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("data/%s_part.data",
                id.toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            return stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(line -> line.split(";"))
                    .map(strings -> new QuestPart(strings))
                    .filter(questPart -> {
                        return questPart.getId().equals(id);
                    })
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(id));
        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest banner file");
        }
    }
}
