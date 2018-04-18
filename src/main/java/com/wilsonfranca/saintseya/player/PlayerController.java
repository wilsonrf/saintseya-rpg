package com.wilsonfranca.saintseya.player;

import com.wilsonfranca.saintseya.GameEngine;

/**
 * Created by wilson on 15/04/18.
 */
public class PlayerController {

    GameEngine gameEngine;

    public PlayerController(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void execute(Player player) {
        gameEngine.createKnight(player);
    }

}
