package com.wilsonfranca.saintseya;

import java.util.Scanner;

/**
 * Created by wilson on 10/04/18.
 */
public class Figth {

    private Player player;

    private Enemy enemy;

    private boolean ended;

    public Figth(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }


    public void start() {

        while (!ended && (!player.isDead() && !enemy.isDead())) {
            askForAction();
            enemy.attack(player);
        }
        if(player.isDead()) {
            System.out.println("You're dead!");
        }
        if(enemy.isDead()) {
            System.out.println("The enemy is dead!");
            player.won();
        }
        ended = true;
    }

    private void askForAction() {

        Scanner scanner = new Scanner(System.in);
        String option = null;
        while (option == null || (!"1".equals(option) && !"2".equals(option))) {
            System.out.println(String.format("::::::%s of %s what you want to do?::::::", player.getName(),
                    player.getConstellation().getDescription()));
            System.out.println("1) Attack");
            System.out.println("2) Run away");
            option = scanner.nextLine();
        }
        if("1".equals(option)) {
            player.attack(enemy);
        } else if("2".equals(option)) {
            ended = player.runAway(enemy);
        }
    }
}
