package com.wilsonfranca.saintseya.quest;

import java.util.Arrays;

/**
 * Created by wilson.franca on 10/04/18.
 */
public class Reward {

    private String id;

    private int xp;

    private int hp;

    public Reward(String... properties) {
        Arrays.asList(properties)
                .stream()
                .forEach(property -> {

                    if(property.contains("id")) {
                        this.id = property.substring(property.indexOf(':') + 1, property.length());
                    }

                    if(property.contains("xp")) {
                        this.xp = Integer.valueOf(property.substring(property.indexOf(':') + 1, property.length()));
                    }

                    if(property.contains("hp")) {
                        this.hp = Integer.valueOf(property.substring(property.indexOf(':') + 1, property.length()));
                    }
                });
    }

    public String getId() {
        return id;
    }

    public int getXp() {
        return xp;
    }

    public int getHp() {
        return hp;
    }
}
