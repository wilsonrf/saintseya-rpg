package com.wilsonfranca.saintseya;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by wilson on 07/04/18.
 */
public class Quest {

    private final String id;

    private final Player player;

    private Instant createdDate;

    private Instant lastSaveDate;

    private boolean started;

    private boolean completed;

    List<QuestPart> questParts;

    public Quest(final Player player, String questId, QuestPart... questParts) {
        this.id = questId;
        this.player = player;
        this.createdDate = Instant.now();
        Arrays.asList(questParts).stream().forEach(questPart -> this.questParts.add(questPart));
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void start() {
        this.started  = true;
        while (!player.isDead() && !isCompleted()) {

        }
    }

    protected void load() {

        ClassLoader classLoader = getClass().getClassLoader();

        String dataPath = classLoader.getResource(String.format("data/%s.data", id.toLowerCase())).getPath();
        String partsPath = classLoader.getResource(String.format("data/%s_part.data", id.toLowerCase())).getPath();
        String optionsPath = classLoader.getResource(String.format("data/%s_part_options.data", id.toLowerCase())).getPath();

        try (Stream<String> dataStream = Files.lines(Paths.get(dataPath));
             Stream<String> partsStream = Files.lines(Paths.get(partsPath));
             Stream<String> optionsStream = Files.lines(Paths.get(optionsPath))) {

            String id = dataStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(s -> s.split(";"))
                    .flatMap(Arrays::stream)
                    .filter(s -> s.contains("id"))
                    .map(s -> s.substring(s.indexOf(":") + 1, s.length()))
                    .filter(s -> this.id.equalsIgnoreCase(s))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            partsStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(s -> s.split(";"))
                    .map(Arrays::toString)
                    .forEach(System.out::println);

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest data file");
        }

    }
}
