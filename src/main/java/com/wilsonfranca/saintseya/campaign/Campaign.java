package com.wilsonfranca.saintseya.campaign;

import com.wilsonfranca.saintseya.util.FilesHelper;
import com.wilsonfranca.saintseya.util.Persistent;

import java.time.Instant;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Created by wilson.franca on 06/04/18.
 */
public class Campaign implements Persistent<Campaign> {

    private String id;

    private Instant dateCreated;

    private Instant savedDate;

    public Campaign(){
        this.dateCreated = Instant.now();
        this.savedDate = this.dateCreated;
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

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Instant getSavedDate() {
        return savedDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id:").append(this.id).append(";");
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
        FilesHelper filesHelper = new FilesHelper();
        filesHelper.save(this);
        //TODO save
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
