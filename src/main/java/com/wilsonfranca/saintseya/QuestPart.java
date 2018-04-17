package com.wilsonfranca.saintseya;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Created by wilson on 07/04/18.
 */
public class QuestPart implements Comparable <QuestPart> {

    private Quest quest;

    private String id;

    private String next;

    private boolean completed;

    private Reward reward;

    private Enemy enemy;

    private Battle battle;

    public QuestPart(Quest quest, String... properties){
        this.quest = quest;
        Arrays.asList(properties)
                .stream()
                .forEach(property -> {
                    if(property.contains("id")) {
                        this.id = property.substring(property.indexOf(":") + 1, property.length());
                    }

                    if(property.contains("next")) {
                        this.next = property.substring(property.indexOf(":") + 1, property.length());
                    }

                    if(property.contains("reward")) {
                        String rewardId = property.substring(property.indexOf(":") + 1, property.length());
                        this.reward = loadReward(rewardId);
                    }

                    if(property.contains("enemy")) {
                        String enemyId = property.substring(property.indexOf(":") + 1, property.length());
                        this.enemy = loadEnemy(enemyId);
                    }
                });
    }

    public QuestPart(String... properties){
        Arrays.asList(properties)
                .stream()
                .forEach(property -> {
                    if(property.contains("id")) {
                        this.id = property.substring(property.indexOf(":") + 1, property.length());
                    }

                    if(property.contains("next")) {
                        this.next = property.substring(property.indexOf(":") + 1, property.length());
                    }

                    if(property.contains("reward")) {
                        String rewardId = property.substring(property.indexOf(":") + 1, property.length());
                        this.reward = loadReward(rewardId);
                    }

                    if(property.contains("enemy")) {
                        String enemyId = property.substring(property.indexOf(":") + 1, property.length());
                        this.enemy = loadEnemy(enemyId);
                    }
                });
    }

    public QuestPart(String currentPart) {
        this.id = currentPart;
    }

    public String getId() {
        return id;
    }

    public boolean hasReward() {
        return this.reward != null;
    }

    public boolean hasEnemy() {
        return this.enemy != null;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestPart questPart = (QuestPart) o;
        return Objects.equals(id, questPart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(QuestPart o) {
        return this.getId().compareTo(o.getId());
    }

    public String start(Player player) {
        String next;
        loadPartBanner();
        if(this.next == null) {
            Scanner scanner = new Scanner(System.in);
            next = this.getId()+"_"+scanner.nextLine();
        } else {
            next = this.next;
        }

        if(hasReward()) {
            // if this part was already loaded and completed (rewarded)
            if(!loadedAndCompleted()) {
                player.addXp(this.reward.getXp());
                player.addHp(this.reward.getHp());
            }
            completed = true;
        }

        if(hasEnemy()) {
            if(!loadedAndCompleted()) {
                Battle battle = new Battle(player, enemy);
                battle.start();
            } else {
                System.out.println("You already fighted this enemy!");
            }
            completed = true;
        }
        save();
        return next;
    }


    private void loadPartBanner() {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("quest/%s.txt",
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

    private boolean loadedAndCompleted() {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s_%s_%s.data", savesPath,
                this.quest.getPlayer().getName().toLowerCase(),
                this.quest.getPlayer().getConstellation().getDescription().toLowerCase(),
                this.getId());


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

    private Reward loadReward(String rewardId) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("data/reward.data",
                rewardId.toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            return stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(line -> line.split(";"))
                    .map(Reward::new)
                    .filter(r -> r.getId().equals(rewardId))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);


        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the rewards file");
        }
    }

    private Enemy loadEnemy(String enemyId) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("data/enemy.data",
                enemyId.toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            return stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(line -> line.split(";"))
                    .map(Enemy::new)
                    .filter(e -> e.getId().equals(enemyId))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);


        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the enemies file");
        }
    }

    public void save() {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s_%s_%s.data", savesPath,
                this.quest.getPlayer().getName().toLowerCase(),
                this.quest.getPlayer().getConstellation().getDescription().toLowerCase(),
                this.getId());

        Path path = Paths.get(stringPath);

        try(OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(path, CREATE, TRUNCATE_EXISTING))) {

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
        sb.append("completed:").append(completed).append(";");
        return sb.toString();
    }

    public String getNext() {
        return next;
    }

    public Reward getReward() {
        return reward;
    }

    public void complete() {
        this.completed = true;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }
}
