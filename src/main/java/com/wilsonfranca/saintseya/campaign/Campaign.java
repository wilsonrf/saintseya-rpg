package com.wilsonfranca.saintseya.campaign;

import com.wilsonfranca.saintseya.Player;
import com.wilsonfranca.saintseya.Quest;
import com.wilsonfranca.saintseya.util.FilesHelper;
import com.wilsonfranca.saintseya.util.Persistent;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;


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
