package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.battle.Battle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wilson on 07/04/18.
 */
public class Quest {

    private final String id;

    private Instant createdDate;

    private Instant lastSaveDate;

    private boolean started;

    private boolean completed;

    private QuestPart questPart;

    private Battle battle;

    public Quest(String name) {
        this.id = name;
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

    public QuestPart getQuestPart() {
        if(questPart == null) {
            questPart = new QuestPart(String.format("%s_part_%s", this.id.toLowerCase(), "1"));
        }
        return questPart;
    }

    public void setQuestPart(QuestPart questPart) {
        this.questPart = questPart;
    }

    public String getNext(String nextId) {
        return this.getQuestPart().getId()+"_"+nextId;
    }

    private QuestPart loadPart(String id) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("data/%s_part.data",
                this.id.toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            return stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(line -> line.split(";"))
                    .map(strings -> new QuestPart(this, strings))
                    .filter(questPart -> {
                        return questPart.getId().equals(id);
                    })
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(id));
        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest banner file");
        }
    }

    private void loadQuestBanner() {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("quest/%s_banner.txt",
                this.id.toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            String banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));
            System.out.println(banner);

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest banner file");
        }
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id:").append(id).append(";");
        sb.append("createdDate:").append(createdDate.toEpochMilli()).append(";");
        sb.append("lastSaveDate:").append(lastSaveDate.toEpochMilli()).append(";");
        sb.append("started:").append(started).append(";");
        sb.append("completed:").append(completed).append(";");
//        sb.append("currentPartId:").append(currentPartId).append(";");
        return sb.toString();
    }

}
