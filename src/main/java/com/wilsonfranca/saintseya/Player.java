package com.wilsonfranca.saintseya;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Created by wilson.franca on 06/04/18.
 */
public class Player {

    private String name;

    private Constellation constellation;

    private int level;

    private int experience;

    private int healthPoints;

    private int hitPoints;

    private int recoveryXpPoints;

    private int recoveryHpPoints;

    public Player(String name, Constellation constellation) {
        this.name = name;
        this.constellation = constellation;
        loadConstellationData();
    }

    public boolean isDead() {
        return healthPoints == 0;
    }

    private void loadConstellationData() {

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

    public String getName() {
        return name;
    }

    public Constellation getConstellation() {
        return constellation;
    }

    @Override
    public String toString() {
        return String.format("name:%s constellation:%s health_points:%d hit_points:%d experience_points:%d recovery_xp:%d recovery_hp:%d",
                name, constellation.getDescription().toLowerCase(), healthPoints, hitPoints, experience, recoveryXpPoints, recoveryHpPoints);
    }

    public void addXp(int xp) {
        if(xp > 0 && experience < 5) {
            System.out.println(String.format("You got %d experience points!", xp));
            this.experience += xp;
            System.out.println(String.format("Now you have %d experience points!", this.experience));
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
            System.out.printf("You got %d health points!", hp);
            this.healthPoints += hp;
            System.out.printf("Now you have %d experience points!", this.experience);
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

    public void save() {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s.data", savesPath, this.getName().toLowerCase());
        Path path = Paths.get(stringPath);

        try(OutputStream out = new BufferedOutputStream(
                    Files.newOutputStream(path, CREATE, TRUNCATE_EXISTING))) {
            // Player data
            String s = this.toString();

            byte data[] = s.getBytes();

            out.write(data, 0, data.length);

            System.out.println(String.format("File saved at %s", path));

        } catch (IOException e) {
            throw new IllegalStateException("Error creating file.");
        }


    }
}
