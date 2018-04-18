package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.util.Persistent;

import java.time.Instant;
import java.util.Arrays;

/**
 * Created by wilson on 07/04/18.
 */
public class Quest implements Persistent<Quest> {

    private String id;

    private Instant createdDate;

    private Instant lastSaveDate;

    private boolean started;

    private boolean completed;

    private QuestPart questPart;

    public Quest(String name) {
        this.id = name;
        this.createdDate = Instant.now();
        this.lastSaveDate = Instant.now();
        this.started = false;
        this.completed = false;
    }

    public Quest(String... properties) {
        Arrays.asList(properties)
                .stream()
                .map(s -> s.split(";"))
                .flatMap(Arrays::stream)
                .forEach(property -> {

                    if(property.contains("id")) {
                        this.id = property.substring(property.indexOf(":") + 1, property.length());
                    }

                    if(property.contains("createdDate")) {
                        long millis = Long.valueOf(property.substring(property.indexOf(":") + 1, property.length()));
                        this.createdDate = Instant.ofEpochMilli(millis);
                    }

                    if(property.contains("lastSaveDate")) {
                        long millis = Long.valueOf(property.substring(property.indexOf(":") + 1, property.length()));
                        this.lastSaveDate = Instant.ofEpochMilli(millis);
                    }

                    if(property.contains("started")) {
                        this.started = Boolean.getBoolean(property.substring(property.indexOf(":") + 1, property.length()));
                    }

                    if(property.contains("completed")) {
                        this.completed = Boolean.getBoolean(property.substring(property.indexOf(":") + 1, property.length()));
                    }

                    if(property.contains("currentPartId")) {
                        this.questPart = new QuestPart(property.substring(property.indexOf(":") + 1, property.length()));
                    }

                });
    }

    public Quest(byte[] data) {
        this(new String[] { new String(data) });
    }

    public String getId() {
        return id;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isCompleted() {
        return completed;
    }

    public QuestPart getQuestPart() {
        if(questPart == null) {
            questPart = new QuestPart(String.format("%s_part_%s", this.id.toLowerCase(), "1"));
        }
        return questPart;
    }

    public void setQuestPart(QuestPart questPart) {
        this.questPart = questPart;
    }

    public String getNext(String nextId) {
        return this.getQuestPart().getId()+"_"+nextId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id:").append(id).append(";");
        sb.append("createdDate:").append(createdDate.toEpochMilli()).append(";");
        sb.append("lastSaveDate:").append(lastSaveDate.toEpochMilli()).append(";");
        sb.append("started:").append(started).append(";");
        sb.append("completed:").append(completed).append(";");
        if (questPart != null) {
            sb.append("currentPartId:").append(questPart.getId()).append(";");
        }
        return sb.toString();
    }

    @Override
    public byte[] getPersistentData() {
        return toString().getBytes();
    }

}
