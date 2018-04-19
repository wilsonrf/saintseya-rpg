package com.wilsonfranca.saintseya.menu;

import com.wilsonfranca.saintseya.GameEngine;

/**
 * Created by wilson on 15/04/18.
 */
public class MenuController {

    private final GameEngine gameEngine;

    public MenuController(final GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public void execute(int option) {
        switch (option) {
            case 1:
                gameEngine.newGame();
                break;
            case 2:
                gameEngine.loadGame();
                break;
            case 3:
                gameEngine.exitGame();
                break;
            default:
                gameEngine.menu();
        }
    }
}
