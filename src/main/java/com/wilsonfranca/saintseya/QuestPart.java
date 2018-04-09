package com.wilsonfranca.saintseya;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wilson on 07/04/18.
 */
public class QuestPart {

    private Quest quest;

    private List<QuestPartOption> questPartOptions;

    public QuestPart(Quest quest, QuestPartOption... questPartOptions) {
        this.quest = quest;
        this.questPartOptions = new LinkedList<>();
        Arrays.asList(questPartOptions).stream().forEach(q -> this.questPartOptions.add(q));
    }

    public Quest getQuest() {
        return quest;
    }

    public List<QuestPartOption> getQuestPartOptions() {
        return questPartOptions;
    }
}
