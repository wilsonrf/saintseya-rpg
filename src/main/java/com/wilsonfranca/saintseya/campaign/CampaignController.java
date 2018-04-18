package com.wilsonfranca.saintseya.campaign;

import com.wilsonfranca.saintseya.GameEngine;
import com.wilsonfranca.saintseya.player.Player;

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

}
