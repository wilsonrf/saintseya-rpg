package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.util.FilesLoader;

import java.util.Scanner;

/**
 * Created by wilson.franca on 06/04/18.
 */
public class Game {

    private final MainMenu mainMenu;

    private Campaign campaign;

    private FilesLoader filesLoader;

    public Game() {
        mainMenu = new MainMenu();
        filesLoader = new FilesLoader(this.getClass().getClassLoader());
    }

    public void play() {

        System.out.println(mainMenu.banner());
        System.out.println(mainMenu.options());
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        if(option > 0 && option != 3) {
            if(option == 1) {
                campaign = new Campaign();
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

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public void load() {
        filesLoader.loadSavedFile(this);
    }
}
