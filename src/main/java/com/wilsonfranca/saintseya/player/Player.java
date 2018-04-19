package com.wilsonfranca.saintseya.player;

import com.wilsonfranca.saintseya.quest.Enemy;
import com.wilsonfranca.saintseya.util.FilesHelper;
import com.wilsonfranca.saintseya.util.Persistent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by wilson.franca on 06/04/18.
 */
public class Player implements Persistent<Player> {

    public static final int XP_POINTS_TO_LEVEL_UP = 5;

    private String name;

    private Constellation constellation;

    private int level;

    private int experience;

    private int healthPoints;

    private int hitPoints;

    private int recoveryXpPoints;

    private int recoveryHpPoints;

    private boolean damaged;

    private boolean leveledUp;

    FilesHelper filesHelper;

    public Player(String name, Constellation constellation) {
        this.name = name;
        this.constellation = constellation;
        this.filesHelper = new FilesHelper();
        loadConstellationData();
    }

    protected Player(String... properties) {
        Arrays.asList(properties)
                .stream()
                .map(s -> s.split(";"))
                .flatMap(Arrays::stream)
                .forEach(property -> {

                    if(property.contains("name")) {
                        this.name = property.substring(property.indexOf(":") + 1, property.length());
                    }

                    if(property.contains("constellation")) {
                        String constellation = property.substring(property.indexOf(":") + 1, property.length());
                        this.constellation = Constellation.getByName(constellation);
                    }

                    if(property.contains("health_points")) {
                        this.healthPoints = Integer.valueOf(property.substring(property.indexOf(":") + 1, property.length()));
                    }

                    if(property.contains("hit_points")) {
                        this.hitPoints = Integer.valueOf(property.substring(property.indexOf(":") + 1, property.length()));
                    }

                    if(property.contains("experience_points")) {
                        this.experience = Integer.valueOf(property.substring(property.indexOf(":") + 1, property.length()));
                    }

                    if(property.contains("recovery_xp")) {
                        this.recoveryXpPoints = Integer.valueOf(property.substring(property.indexOf(":") + 1, property.length()));
                    }

                    if(property.contains("recovery_hp")) {
                        this.recoveryHpPoints = Integer.valueOf(property.substring(property.indexOf(":") + 1, property.length()));
                    }

                });
    }

    protected Player(byte[] data) {
        this(new String(data));
    }

    public String getName() {
        return name;
    }

    public Constellation getConstellation() {
        return constellation;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getExperience() {
        return experience;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLeveledUp() {
        return leveledUp;
    }

    public void setFilesHelper(FilesHelper filesHelper) {
        this.filesHelper = filesHelper;
    }

    public boolean isDead() {
        return healthPoints < 1;
    }

    protected void loadConstellationData() {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("character/%s.data",
                constellation.getDescription().toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(line -> line.split(";"))
                    .flatMap(Arrays::stream)
                    .forEach(property -> {
                        if(property.contains("health_points")) {
                            String stringValue = property.substring(property.indexOf(":") + 1, property.length());
                            this.healthPoints = Integer.valueOf(stringValue);
                        } else if(property.contains("hit_points")) {
                            String stringValue = property.substring(property.indexOf(":") + 1, property.length());
                            this.hitPoints = Integer.valueOf(stringValue);
                        } else if(property.contains("recovery_xp")) {
                            String stringValue = property.substring(property.indexOf(":") + 1, property.length());
                            this.recoveryXpPoints = Integer.valueOf(stringValue);
                        } else if(property.contains("recovery_hp")) {
                            String stringValue = property.substring(property.indexOf(":") + 1, property.length());
                            this.recoveryHpPoints = Integer.valueOf(stringValue);
                        }
                    });

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the constellation file");
        }
    }

    public void addXp(int xp) {
        if(xp > 0 && experience < XP_POINTS_TO_LEVEL_UP) {
            this.experience += xp;
            this.leveledUp = false;
        } else {
            this.experience = 0;
            this.leveledUp = true;
            levelUp();
        }
    }

    public void addHp(int hp) {
        if(hp > 0) {
            this.healthPoints += hp;
        }
    }

    private void levelUp() {
        this.level++;
    }

    public boolean exists() {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s_%s.data", savesPath, this.getName().toLowerCase(),
                this.getConstellation().getDescription().toLowerCase());

        Path path = Paths.get(stringPath);

        return Files.exists(path);
    }

    public void attack(Enemy enemy) {
        Random random = new Random();
        int playerDice = random.ints(1, 6).findFirst().getAsInt();
        int enemyDice = random.ints(1, 6).findFirst().getAsInt();
        if(playerDice > enemyDice) {
            enemy.hit(this.hitPoints);
            enemy.damage();

        } else {
            enemy.notDamage();
        }
    }

    public boolean runAway() {
        boolean runWay = false;
        Random random = new Random();
        int playerDice = random.ints(1, 6).findFirst().getAsInt();
        int enemyDice = random.ints(1, 6).findFirst().getAsInt();
        if(playerDice > enemyDice) {
            runWay = true;
        }
        return runWay;

    }

    public void won() {
        this.addXp(recoveryXpPoints);
        this.addHp(recoveryHpPoints);
    }

    public void hit(int hitPoints) {
        this.healthPoints -= hitPoints;
    }

    public void damage() {
        this.damaged = true;
    }

    public void notDamage() {
        this.damaged = false;
    }

    public String getPersistentPath() {
        return this.getName().toLowerCase() + "_" + this.getConstellation().getDescription().toLowerCase();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("name:").append(name).append(";");
        sb.append("constellation:").append(constellation.getDescription().toLowerCase()).append(";");
        sb.append("level:").append(level).append(";");
        sb.append("experience_points:").append(experience).append(";");
        sb.append("health_points:").append(healthPoints).append(";");
        sb.append("hit_points:").append(hitPoints).append(";");
        sb.append("recovery_xp:").append(recoveryXpPoints).append(";");
        sb.append("recovery_hp:").append(recoveryHpPoints).append(";");
        return sb.toString();
    }

    @Override
    public byte[] getPersistentData() {
        return this.toString().getBytes();
    }

}
