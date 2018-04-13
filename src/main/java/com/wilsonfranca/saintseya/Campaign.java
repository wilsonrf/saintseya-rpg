package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.util.FilesHelper;
import com.wilsonfranca.saintseya.util.Persistent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by wilson.franca on 06/04/18.
 */
public class Campaign implements Persistent<Campaign> {

    private String id;

    private Player player;

    private Set<Quest> quests;

    private Instant dateCreated;

    private Instant savedDate;

    public Campaign(){
        this.dateCreated = Instant.now();
        this.savedDate = this.dateCreated;
        this.quests = new HashSet<>();
    }

    public Campaign(final Player player, final Quest... quests) {
        this();
        this.player = player;
        this.quests = Arrays.asList(quests).stream().collect(Collectors.toSet());
    }

    private Campaign(String... properties) {
        Arrays.asList(properties)
                .stream()
                .forEach(property -> {

                    if(property.contains("id")) {
                        this.id = property.substring(property.indexOf(":") + 1, property.length());
                    }

                    if(property.contains("player")) {
                        String playerName = property.substring(property.indexOf(":") + 1, property.length());
                        // Player player;
                    }
                });
    }

    private Campaign(byte[] bytes) {
        this(new String(bytes));
    }

    public String getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Instant getSavedDate() {
        return savedDate;
    }

    public Set<Quest> getQuests() {
        return quests;
    }


    public void start() {
        dateCreated = Instant.now();
        if(player == null) {
            newPlayerBanner();
            player = createKnight();
        }

        Quest quest = new Quest(player, "fenix_quest");
        this.quests.add(quest);
        save();
        quest.start();
    }

    private Player createKnight() {

        Player player = null;
        String name = null;
        Constellation constellation = null;
        boolean constellationChose = false;
        Scanner scanner = new Scanner(System.in);

        do {
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

            if (player.exists()) {
                String overwrite;
                do {
                    System.out.printf("The player %s of %s already exists!", name, constellation.getDescription());
                    System.out.println("[O]verwrite | [R]estart");
                    overwrite = scanner.nextLine();
                } while (overwrite == null || (!"R".equalsIgnoreCase(overwrite) && !"O".equalsIgnoreCase(overwrite)));
                if ("O".equalsIgnoreCase(overwrite)) {
                    System.out.println(String.format("Nice! You're now %s Knight of %s", name, constellation.getDescription()));
                    player.save();
                } else {
                    player = null;
                }
            } else {
                System.out.println(String.format("Nice! You're now %s Knight of %s", name, constellation.getDescription()));
                player.save();
            }
        } while (player == null);

        return player;
    }

    protected Player loadKnight() {

        return null;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id:").append(this.id).append(";")
                .append("player:").append(this.player.getName()).append(";");
        return sb.toString();
    }

    @Override
    public String getPersistentPath() {
        return this.getId();
    }

    @Override
    public byte[] getPersistentData() {
        return toString().getBytes();
    }

    @Override
    public void save() {
        // only save if it has a quest
        if(quests != null && !quests.isEmpty()) {
            this.savedDate = Instant.now();
            this.id = this.getPlayer().getName().toLowerCase() + "_campaign";
            FilesHelper filesHelper = new FilesHelper();
            filesHelper.save(this);
        } else {
            throw new IllegalStateException("Problem saving a Campaign without a quest");
        }

    }

    @Override
    public Campaign load() {
        System.out.println("What's the name of the Knight you played?");
        Scanner scanner = new Scanner(System.in);
        String knightName = scanner.nextLine();
        FilesHelper filesHelper = new FilesHelper();
        byte[] bytes = filesHelper.load(knightName + "_campaign");
        Campaign campaign = new Campaign(bytes);
        return campaign;
    }

}
