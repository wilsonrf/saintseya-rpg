package com.wilsonfranca.saintseya.campaign;

import com.wilsonfranca.saintseya.*;
import com.wilsonfranca.saintseya.player.Constellation;
import com.wilsonfranca.saintseya.player.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wilson on 15/04/18.
 */
public class CampaignView implements Observer {

    CampaignController campaignController;
    GameEngine gameEngine;
    Scanner scanner;

    private String name;
    private Constellation constellation;

    public CampaignView(CampaignController campaignController, GameEngine gameEngine) {
        this.campaignController = campaignController;
        this.gameEngine = gameEngine;
        this.gameEngine.addObserver(this);
        this.scanner = new Scanner(System.in);
    }

    public void show() {
        if (this.gameEngine.getPlayer() == null) {

            newPlayerBanner();

            do {
                System.out.println("What will be the name of your Knight?");
                askForPlayerName();
            } while (this.name == null || "".equals(this.name));

            boolean constellationChose = false;

            do {

                try {

                    System.out.println("What will be constellation of you Knight? Choose one of the bellow:");
                    Arrays.asList(Constellation.values())
                            .stream()
                            .forEach(constell -> System.out.println(String.format("%d) %s", constell.getId(), constell.getDescription())));
                    askForConstellation();
                    scanner.nextLine();
                    constellationBanner(constellation);
                    System.out.println(String.format("Do you want to continue with %s constellation? [Y/N]", constellation.getDescription()));
                    String constellationContinue = scanner.nextLine();
                    constellationChose = "Y".equalsIgnoreCase(constellationContinue);
                } catch (IllegalArgumentException e) {
                    System.out.println("You have chosen a invalid constellation! Try again.");
                }
            } while (constellation == null || !constellationChose);

            Player player = new Player(this.name, this.constellation);

            if (player.exists()) {
                String overwrite;
                do {
                    System.out.println(String.format("The player %s of %s already exists!", name, constellation.getDescription()));
                    System.out.println("[O]verwrite | [R]estart");
                    overwrite = scanner.nextLine();
                } while (overwrite == null || (!"R".equalsIgnoreCase(overwrite) && !"O".equalsIgnoreCase(overwrite)));
                if ("O".equalsIgnoreCase(overwrite)) {
                    System.out.println(String.format("Nice! You're now %s Knight of %s", name, constellation.getDescription()));
                    campaignController.execute(player);
                }
            } else {
                System.out.println(String.format("Nice! You're now %s Knight of %s", name, constellation.getDescription()));
                campaignController.execute(player);
            }

        }
    }

    protected void newPlayerBanner() {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource("misc/newplayer_banner.txt").getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            String banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));
            System.out.println(banner);

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the menu options file");
        }

    }

    protected void constellationBanner(Constellation constellation) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("character/%s_banner.txt",
                constellation.getDescription().toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            String banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));
            System.out.println(banner);

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the menu options file");
        }
    }

    private void askForConstellation() {
        this.constellation = Constellation.getById(scanner.nextInt());
    }

    private void askForPlayerName() {
        this.name = scanner.nextLine();
    }

    @Override
    public void update(Observable o, Object arg) {
        String action = (String) arg;
        if("newCampaign".equalsIgnoreCase(action) ||
                "startQuest".equalsIgnoreCase(action) ||
                "startQuestPart".equalsIgnoreCase(action)) {
            show();
        }
    }
}
