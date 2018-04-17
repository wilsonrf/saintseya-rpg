package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.GameEngine;
import com.wilsonfranca.saintseya.Quest;

/**
 * Created by wilson on 16/04/18.
 */
public class QuestController {

    GameEngine gameEngine;

    public QuestController(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void execute(Quest quest) {
        gameEngine.startQuest(quest);
    }

    public void execute(String nextId) {
        gameEngine.startQuestPart(nextId);
    }
}
