package com.wilsonfranca.saintseya;

import java.util.Scanner;

/**
 * Created by wilson.franca on 06/04/18.
 */
public class Game {

    public void play() {

        MainMenu mainMenu = new MainMenu();
        System.out.println(mainMenu.banner());
        mainMenu.getMenuOptions()
                .stream()
                .forEach(menuOption -> System.out.println(String.format("%d) %s", menuOption.getId(), menuOption.getText())));
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        if(option > 0 && option != 3) {
            if(option == 1) {
                Campaign campaign = new Campaign();
                campaign.start();
            }

            if(option == 2) {
                //TODO load game
            }

        } else {
            goodBye();
        }
    }

    private void goodBye() {
        System.out.println("Good bye!");
    }
}
