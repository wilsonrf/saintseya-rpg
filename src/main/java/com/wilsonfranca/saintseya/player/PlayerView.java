package com.wilsonfranca.saintseya.player;

import com.wilsonfranca.saintseya.GameEngine;
import com.wilsonfranca.saintseya.util.FilesHelper;

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
public class PlayerView implements Observer {

    public static final String NICE_YOU_RE_NOW_S_KNIGHT_OF_S = "Nice! You're now %s Knight of %s";
    private final PlayerController playerController;
    private final GameEngine gameEngine;
    private Scanner scanner;

    public PlayerView(final PlayerController playerController, final GameEngine gameEngine) {
        this.playerController = playerController;
        this.gameEngine = gameEngine;
        this.gameEngine.addObserver(this);
        this.scanner = new Scanner(System.in);
    }

    public void show() {

        newPlayerBanner();

        String name = null;

        do {
            System.out.println("What will be the name of your Knight?");
            name = askForPlayerName();
        } while (name == null || "".equals(name));

        boolean constellationChose = false;

        Constellation constellation = null;

        do {

            try {

                System.out.println("What will be constellation of you Knight? Choose one of the bellow:");
                Arrays.asList(Constellation.values())
                        .stream()
                        .forEach(constell -> System.out.println(String.format("%d) %s", constell.getId(), constell.getDescription())));
                constellation = askForConstellation();
                scanner.nextLine();
                constellationBanner(constellation);
                System.out.println(String.format("Do you want to continue with %s constellation? [Y/N]", constellation.getDescription()));
                String constellationContinue = scanner.nextLine();
                constellationChose = "Y".equalsIgnoreCase(constellationContinue);
            } catch (IllegalArgumentException e) {
                System.out.println("You have chosen a invalid constellation! Try again.");
            }
        } while (constellation == null || !constellationChose);

        Player player = new Player(name, constellation);

        if (player.exists()) {
            String overwrite;
            do {
                System.out.println(String.format("The player %s of %s already exists!", name, constellation.getDescription()));
                System.out.println("[O]verwrite | [R]estart");
                overwrite = scanner.nextLine();
            } while (overwrite == null || (!"R".equalsIgnoreCase(overwrite) && !"O".equalsIgnoreCase(overwrite)));
            if ("O".equalsIgnoreCase(overwrite)) {
                System.out.println(String.format(NICE_YOU_RE_NOW_S_KNIGHT_OF_S, name, constellation.getDescription()));
                playerController.execute(player, true);
            }
        } else {
            System.out.println(String.format(NICE_YOU_RE_NOW_S_KNIGHT_OF_S, name, constellation.getDescription()));
            playerController.execute(player, false);
        }
    }

    private void showLoadGame() {
        String name = null;
        do {
            System.out.println("What is the name of your Knight?");
            name = askForPlayerName();
        } while (name == null || "".equals(name));

        Constellation constellation = null;
        do {

            try {

                System.out.println("What is the constellation of you knight?");
                Arrays.asList(Constellation.values())
                        .stream()
                        .forEach(constell -> System.out.println(String.format("%d) %s", constell.getId(), constell.getDescription())));
                constellation = askForConstellation();
                scanner.nextLine();
            } catch (IllegalArgumentException e) {
                System.out.println("You have chosen a invalid constellation! Try again.");
            }
        } while (constellation == null);

        Player player = new Player(name, constellation);

        if (player.exists()) {
            System.out.println(String.format("You loaded a previous saved game of your %s of %s Knight successful!",
                    player.getName(), player.getConstellation().getDescription()));
            FilesHelper filesHelper = new FilesHelper();
            Player loaded = new Player(filesHelper.load(player.getPersistentPath()));
            playerController.execute(loaded, false);
        } else {
            System.out.println(String.format("We didn't found any saved game of %s of %s Knight.",
                    player.getName(), player.getConstellation().getDescription()));
            String option = null;
            do {
                System.out.println("[T]ry again | [N]ew player");
                option = scanner.nextLine();
            } while (option == null || (!"T".equalsIgnoreCase(option) && !"N".equalsIgnoreCase(option)));

            if("T".equalsIgnoreCase(option)) {
                System.out.println("Let's try again!");
                showLoadGame();
            }

            if("N".equalsIgnoreCase(option)) {
                System.out.println(String.format(NICE_YOU_RE_NOW_S_KNIGHT_OF_S, name, constellation.getDescription()));
                playerController.execute(player, false);
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

    private Constellation askForConstellation() {
        return Constellation.getById(scanner.nextInt());
    }

    private String askForPlayerName() {
        return scanner.nextLine();
    }

    @Override
    public void update(Observable o, Object arg) {
        String action = (String) arg;
        if("newGame".equalsIgnoreCase(action)) {
            show();
        } else if ("loadGame".equalsIgnoreCase(action)) {
            showLoadGame();
        }
    }
}
