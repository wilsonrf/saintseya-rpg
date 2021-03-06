package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.player.Player;
import com.wilsonfranca.saintseya.util.FilesHelper;

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
                    .map(QuestPart::new)
                    .filter(questPart -> questPart.getId().equals(partId))
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

        if(!path.toFile().exists()) {
            return false;
        } else {
            try (Stream<String> stringStream = Files.lines(path)) {

                return stringStream
                        .filter(s -> !"".equals(s) && s != null)
                        .map(line -> line.split(";"))
                        .flatMap(Arrays::stream)
                        .anyMatch(s -> s.equalsIgnoreCase("completed:true"));


            } catch (IOException e) {
                throw new IllegalStateException("There is a problem loading the quest part file");
            }
        }
    }

    public void save(Player player, Quest quest) {
        FilesHelper filesHelper = new FilesHelper();
        filesHelper.save(player.getPersistentPath() + "_" + quest.getId(), quest.getPersistentData());
    }

    public void save(Player player, QuestPart part) {
        FilesHelper filesHelper = new FilesHelper();
        filesHelper.save(player.getPersistentPath() + "_" + part.getId(), part.getPersistentData());
    }

    public Quest load(Player player, Quest quest) {
        FilesHelper filesHelper = new FilesHelper();
        final byte[] load = filesHelper.load(player.getPersistentPath() + "_" + quest.getId());
        if(load.length > 1) {
            return new Quest(load);
        }

        return quest;
    }

    public QuestPart load(Player player, QuestPart part) {
        FilesHelper filesHelper = new FilesHelper();
        final byte[] load = filesHelper.load(player.getPersistentPath() + "_" + part.getId());
        if(load.length > 1) {
            return new QuestPart(load);
        }

        return part;
    }
}
