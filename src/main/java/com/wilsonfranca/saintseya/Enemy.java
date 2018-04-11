package com.wilsonfranca.saintseya;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by wilson on 10/04/18.
 */
public class Enemy {

    private String id;

    private String name;

    private int healthPoints;

    private int hitPoints;

   public Enemy(String... properties) {
       Arrays.asList(properties)
               .stream()
               .forEach(property -> {

                   if(property.contains("id")) {
                       this.id = property.substring(property.indexOf(":") + 1, property.length());
                   }

                   if(property.contains("name")) {
                       this.name = property.substring(property.indexOf(":") + 1, property.length());
                   }

                   if(property.contains("health_points")) {
                       this.healthPoints = Integer.valueOf(property.substring(property.indexOf(":") + 1, property.length()));
                   }

                   if(property.contains("hit_points")) {
                       this.hitPoints = Integer.valueOf(property.substring(property.indexOf(":") + 1, property.length()));
                   }
               });
   }

    public boolean isDead() {
        return healthPoints < 1;
    }

    public String getId() {
        return id;
    }

    public void hit(int hitPoints) {
        this.healthPoints -= hitPoints;
    }

    public String getName() {
        return name;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void attack(Player player) {
        Random random = new Random();
        int playerDice = random.ints(1, 6).findFirst().getAsInt();
        int enemyDice = random.ints(1, 6).findFirst().getAsInt();
        if(enemyDice > playerDice) {
            System.out.println(String.format("%s hit You!", this.getName()));
            player.hit(this.hitPoints);
            System.out.println(String.format("%s Health Points: %d", player.getName(), player.getHealthPoints()));
            System.out.println(String.format("%s Health Points: %d", this.getName(), this.getHealthPoints()));

        } else {
            System.out.println(System.out.format("%s didn't hit You!", this.getName()));
        }
    }
}
