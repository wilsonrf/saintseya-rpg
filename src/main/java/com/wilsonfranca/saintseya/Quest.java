package com.wilsonfranca.saintseya;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
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

    Set<QuestPart> questParts;

    public Quest(final Player player, String questId) {
        this.id = questId;
        this.player = player;
        this.createdDate = Instant.now();
//        Arrays.asList(questParts).stream().forEach(questPart -> this.questParts.add(questPart));
    }

    public String getId() {
        return id;
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
            loadQuestBanner(this.id);
            loadQuestParts(this.id);
            Iterator<QuestPart> partIterator = questParts.iterator();
            while (partIterator.hasNext()) {
                QuestPart questPart = partIterator.next();
                questPart.start();
            }
        }
    }

    private void loadQuestParts(String id) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("data/%s_part.data",
                id.toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            questParts = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(line -> line.split(";"))
                    .map(strings -> new QuestPart(strings))
                    .collect(Collectors.toSet());
                    //TODO create collector to linkedlist of parts

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest banner file");
        }

    }

    private void loadQuestBanner(String id) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("quest/%s_banner.txt",
                id.toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            String banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));
            System.out.println(banner);

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest banner file");
        }

    }




}
