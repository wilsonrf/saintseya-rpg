package com.wilsonfranca.saintseya.player;

import com.wilsonfranca.saintseya.GameEngine;

/**
 * Created by wilson on 15/04/18.
 */
public class PlayerController {

    private final GameEngine gameEngine;

    public PlayerController(final GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void execute(Player player, boolean overwrite) {
        gameEngine.createKnight(player, overwrite);
    }

}
