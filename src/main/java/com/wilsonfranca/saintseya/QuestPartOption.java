package com.wilsonfranca.saintseya;

/**
 * Created by wilson on 07/04/18.
 */
public class QuestPartOption {

    private QuestPart next;

    public QuestPartOption(QuestPart next) {
        this.next = next;
    }

    public QuestPart getNext() {
        return next;
    }
}
