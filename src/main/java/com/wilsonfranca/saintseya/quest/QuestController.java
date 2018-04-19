package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.GameEngine;

/**
 * Created by wilson on 16/04/18.
 */
public class QuestController {

    private final GameEngine gameEngine;

    public QuestController(final GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void execute(Quest quest) {
        gameEngine.startQuest(quest);
    }

    public void execute(String nextId) {
        gameEngine.startQuestPart(nextId);
    }

    public void execute(Enemy enemy, String option) {
        switch (option) {
            case "1":
                gameEngine.attack(enemy);
                break;
            case "2":
                gameEngine.runAway();
                break;
            default:
                gameEngine.runAway();
                break;
        }
    }
}
