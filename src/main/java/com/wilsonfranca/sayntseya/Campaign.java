package com.wilsonfranca.sayntseya;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;


/**
 * Created by wilson.franca on 06/04/18.
 */
public class Campaign {

    private Player player;

    private Instant dateCreated;

    public Campaign(){}

    public void start() {
        dateCreated = Instant.now();
        if(player == null) {
            newPlayerBanner();
            player = createKnight();
            System.out.println("Saving the player and the game...");
            save();
        }
        

    }

    private Player createKnight() {

        Player player = null;
        String name = null;
        Constellation constellation = null;
        boolean constellationChose = false;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("What will be the name of your Knight?");
            name = scanner.nextLine();
        } while (name == null || "".equals(name));

        do {
            try {

                System.out.println("What will be constellation of you Knight? Choose one of the bellow:");
                Arrays.asList(Constellation.values())
                        .stream()
                        .forEach(constell -> System.out.println(String.format("%d) %s", constell.getId(), constell.getDescription())));
                constellation = Constellation.getById(scanner.nextInt());
                scanner.nextLine();
                constellationBanner(constellation);
                System.out.println(String.format("Do you want to continue with %s constellation? [Y/N]", constellation.getDescription()));
                String constellationContinue = scanner.nextLine();
                constellationChose = "Y".equalsIgnoreCase(constellationContinue);
            } catch (IllegalArgumentException e) {
                // YOU ENTERED A INVALID ID
                System.out.println("You have chosen a invalid constellation! Try again.");
            }
        } while (constellation == null || !constellationChose);

        player = new Player(name, constellation);

        System.out.println(String.format("Nice! You're now %s Knight of %s", name, constellation.getDescription()));

        return player;
    }

    private void constellationBanner(Constellation constellation) {

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

    public void newPlayerBanner() {

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

    public void save() {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s_%s.data", savesPath, player.getName().toLowerCase(),
                new SimpleDateFormat("ddMMyyyyHHmmss").format(Date.from(dateCreated)));
        Path path = Paths.get(stringPath);

        if(!Files.exists(path)) {

            try (OutputStream out = new BufferedOutputStream(
                    Files.newOutputStream(Files.createFile(path), CREATE, APPEND))) {

                // Player data
                String s = player.toString();

                byte data[] = s.getBytes();

                out.write(data, 0, data.length);

                System.out.println(String.format("File saved at %s", path));

            } catch (IOException e) {
                throw new IllegalStateException("Error creating file.");
            }

        }
    }
}
