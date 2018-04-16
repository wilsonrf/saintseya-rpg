package com.wilsonfranca.saintseya.campaign;

import com.wilsonfranca.saintseya.GameEngine;
import com.wilsonfranca.saintseya.Player;
import com.wilsonfranca.saintseya.Quest;
import com.wilsonfranca.saintseya.QuestPart;

/**
 * Created by wilson on 15/04/18.
 */
public class CampaignController {

    GameEngine gameEngine;

    public CampaignController(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void execute(Player player) {
        gameEngine.createKnight(player);
    }

    public void execute(Quest quest) {
        gameEngine.startQuest(quest);
    }

    public void execute(QuestPart questPart) {
        gameEngine.startQuestPart(questPart);
    }
}
