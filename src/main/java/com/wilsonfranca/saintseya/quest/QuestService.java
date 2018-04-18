package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.player.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by wilson on 16/04/18.
 */
public class QuestService {

    public QuestPart questPart(String questId, String partId) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("data/%s_part.data",
                questId.toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            return stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(line -> line.split(";"))
                    .map(strings -> new QuestPart(strings))
                    .filter(questPart -> {
                        return questPart.getId().equals(partId);
                    })
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(partId));
        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest banner file");
        }
    }

    public boolean isPartloadedAndCompleted(Player player, QuestPart questPart) {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s_%s_%s.data", savesPath,
                player.getName().toLowerCase(),
                player.getConstellation().getDescription().toLowerCase(),
                questPart.getId());


        Path path = Paths.get(stringPath);

        if(!Files.exists(path)) {
            return false;
        } else {
            try (Stream<String> stringStream = Files.lines(path)) {

                return stringStream
                        .filter(s -> !"".equals(s) && s != null)
                        .map(line -> line.split(";"))
                        .flatMap(Arrays::stream)
                        .filter(s -> s.equalsIgnoreCase("completed:true"))
                        .findAny().isPresent();


            } catch (IOException e) {
                throw new IllegalStateException("There is a problem loading the quest part file");
            }
        }
    }
}
