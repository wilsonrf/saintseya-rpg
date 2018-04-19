package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.player.PlayerController;
import com.wilsonfranca.saintseya.player.PlayerView;
import com.wilsonfranca.saintseya.menu.MenuController;
import com.wilsonfranca.saintseya.menu.MenuView;
import com.wilsonfranca.saintseya.quest.QuestController;
import com.wilsonfranca.saintseya.quest.QuestView;

/**
 * Created by wilson on 15/04/18.
 */
public class SaintSeyaGameApplication {

    public static void main(String args[]) {
        GameEngine gameEngine = new GameEngine();
        MenuController menuController = new MenuController(gameEngine);
        MenuView menuView = new MenuView(menuController, gameEngine);
        PlayerController playerController = new PlayerController(gameEngine);
        new PlayerView(playerController, gameEngine);
        QuestController questController = new QuestController(gameEngine);
        new QuestView(questController, gameEngine);
        menuView.init();
    }
}
