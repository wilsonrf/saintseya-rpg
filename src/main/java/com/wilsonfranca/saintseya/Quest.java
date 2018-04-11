package com.wilsonfranca.saintseya;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

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

    String currentPart;

    TreeMap<String, TreeMap<String, QuestPart>> questParts = new TreeMap<>();

    Set<QuestPart> parts;

    public Quest(final Player player, String questId) {
        this.id = questId;
        this.player = player;
        this.createdDate = Instant.now();
        this.lastSaveDate = Instant.now();
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
        loadQuestBanner();
        while (!player.isDead() && !isCompleted()) {
            if(currentPart == null) {
                currentPart = String.format("%s_part_%s", this.id.toLowerCase(), "1");
            }
            QuestPart questPart = loadPart(currentPart);
            currentPart = questPart.start(player);
            try {
                save();
            } catch (RuntimeException e) {
                System.err.println("Error on save the quest!");
            }
        }
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

    public Player getPlayer() {
        return player;
    }

    public void save() {

        this.lastSaveDate = Instant.now();

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s_%s_%s.data", savesPath, this.getPlayer().getName(),
                this.getPlayer().getConstellation().getDescription().toLowerCase(),
                this.id.toLowerCase());

        Path path = Paths.get(stringPath);

        try (OutputStream out = new BufferedOutputStream(
                    Files.newOutputStream(path, CREATE, TRUNCATE_EXISTING))) {

            // Quest data
            String s = this.toString();

            byte data[] = s.getBytes();

            out.write(data, 0, data.length);

        } catch (IOException e) {
            throw new IllegalStateException("Error creating file.");
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
        sb.append("currentPart:").append(currentPart).append(";");
        return sb.toString();
    }
}
