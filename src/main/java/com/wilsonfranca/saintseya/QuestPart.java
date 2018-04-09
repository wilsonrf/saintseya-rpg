package com.wilsonfranca.saintseya;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wilson on 07/04/18.
 */
public class QuestPart {

    private String id;

    private String parent;

    private String next;

    private boolean completed;

    private LinkedList<QuestPartOption> questPartOptions;

    public QuestPart(String... properties){
        Arrays.asList(properties)
                .stream()
                .forEach(property -> {
                    if(property.contains("id")) {
                        this.id = property.substring(property.indexOf(":") + 1, property.length());
                    }
                    if(property.contains("parent")) {
                        this.parent = property.substring(property.indexOf(":") + 1, property.length());
                    }
                    if(property.contains("next")) {
                        this.next = property.substring(property.indexOf(":") + 1, property.length());
                    }
                });
    }


    public List<QuestPartOption> getQuestPartOptions() {
        return questPartOptions;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void start() {
        while (!this.isCompleted()) {

        }
    }
}
