package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.util.Persistent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by wilson on 07/04/18.
 */
public class QuestPart implements Persistent<QuestPart> {

    private String id;

    private String next;

    private boolean completed;

    private Reward reward;

    private Enemy enemy;

    private Battle battle;

    protected QuestPart(String... properties){
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

    protected QuestPart(String currentPart) {
        this.id = currentPart;
    }

    public QuestPart(byte[] data) {
        this(new String[] { new String(data) });
    }

    public String getId() {
        return id;
    }

    public boolean isCompleted() {
        return completed;
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

    public boolean hasReward() {
        return this.reward != null;
    }

    public boolean hasEnemy() {
        return this.enemy != null;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id:").append(id).append(";");
        sb.append("completed:").append(completed).append(";");
        if (next != null && !"".equals(next)) {
            sb.append("next:").append(next).append(";");
        }
        sb.append("completed:").append(completed).append(";");
        if(reward != null) {
            sb.append("rewardId:").append(reward.getId()).append(";");
        }
        if(enemy != null) {
            sb.append("enemyId:").append(enemy.getId()).append(";");
        }
        return sb.toString();
    }

    @Override
    public byte[] getPersistentData() {
        return this.toString().getBytes();
    }

}
