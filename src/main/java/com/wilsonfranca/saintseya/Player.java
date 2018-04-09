package com.wilsonfranca.saintseya;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

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
        return String.format("name:%s constellation:%s health_points:%d hit_points:%d recovery_xp:%d recovery_hp:%d",
                name, constellation.getDescription().toLowerCase(), healthPoints, hitPoints, recoveryXpPoints, recoveryHpPoints);
    }
}
