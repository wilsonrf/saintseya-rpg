package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.util.FilesHelper;
import com.wilsonfranca.saintseya.util.Persistent;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Created by wilson.franca on 06/04/18.
 */
public class Player implements Persistent<Player> {

    private String name;

    private Constellation constellation;

    private int level;

    private int experience;

    private int healthPoints;

    private int hitPoints;

    private int recoveryXpPoints;

    private int recoveryHpPoints;

    FilesHelper filesHelper;

    public Player(String name, Constellation constellation) {
        this.name = name;
        this.constellation = constellation;
        this.filesHelper = new FilesHelper();
        loadConstellationData();
    }

    public Player(String... properties) {
        Arrays.asList(properties)
                .stream()
                .map(s -> s.split(" "))
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

    public Player(byte[] data) {
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
                    .map(line -> line.split(" "))
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
        if(xp > 0 && experience < 5) {
            this.experience += xp;
        } else {
            this.experience = 0;
            levelUp();
        }
        try {
            save();
        } catch (RuntimeException e) {
            System.err.println("Error on save the player!");
        }
    }

    public void addHp(int hp) {
        if(hp > 0) {
            this.healthPoints += hp;
            try {
                save();
            } catch (RuntimeException e) {
                System.err.println("Error on save the player!");
            }
        }
    }

    private void levelUp() {
        this.level++;
        System.out.println("Level UP!");
        System.out.printf("You're now on level %d.", this.level);
        try {
            save();
        } catch (RuntimeException e) {
            System.err.println("Error on save the player!");
        }
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
            System.out.println(String.format("You hit %s!", enemy.getName()));
            enemy.hit(this.hitPoints);
            System.out.println(String.format("%s Health Points: %d", this.getName(), this.getHealthPoints()));
            System.out.println(String.format("%s Health Points: %d", enemy.getName(), enemy.getHealthPoints()));

        } else {
            System.out.println("You didn't hit the enemy!");
        }
    }

    public boolean runAway(Enemy enemy) {

        boolean runWay = false;

        Random random = new Random();
        int playerDice = random.ints(1, 6).findFirst().getAsInt();
        int enemyDice = random.ints(1, 6).findFirst().getAsInt();
        if(playerDice > enemyDice) {
            runWay = true;
            System.out.println("You ran way!");
        } else {
            System.out.println("You didn't ran way!");
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

    @Override
    public String toString() {
        return String.format("name:%s constellation:%s health_points:%d hit_points:%d experience_points:%d recovery_xp:%d recovery_hp:%d",
                name, constellation.getDescription().toLowerCase(), healthPoints, hitPoints, experience, recoveryXpPoints, recoveryHpPoints);
    }

    @Override
    public String getPersistentPath() {
        return this.getName();
    }

    @Override
    public byte[] getPersistentData() {
        return this.toString().getBytes();
    }

    @Override
    public void save() {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s_%s.data", savesPath, this.getName().toLowerCase(),
                this.getConstellation().getDescription().toLowerCase());

        Path path = Paths.get(stringPath);

        try(OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(path, CREATE, TRUNCATE_EXISTING))) {
            // Player data
            String s = this.toString();

            byte data[] = s.getBytes();

            out.write(data, 0, data.length);

        } catch (IOException e) {
            throw new IllegalStateException("Error creating file.");
        }

    }

    @Override
    public Player load() {
        byte[] data = filesHelper.load(this.getName().toLowerCase()+"_"+this.getConstellation().getDescription().toLowerCase());
        Player player = new Player(data);
        return player;
    }

    public int getExperience() {
        return experience;
    }
}
