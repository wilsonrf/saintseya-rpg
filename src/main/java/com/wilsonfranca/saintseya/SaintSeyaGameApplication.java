package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.campaign.CampaignController;
import com.wilsonfranca.saintseya.campaign.CampaignView;
import com.wilsonfranca.saintseya.menu.MenuController;
import com.wilsonfranca.saintseya.menu.MenuView;

/**
 * Created by wilson on 15/04/18.
 */
public class SaintSeyaGameApplication {

    public static void main(String args[]) {
        GameEngine gameEngine = new GameEngine();
        MenuController menuController = new MenuController(gameEngine);
        MenuView menuView = new MenuView(menuController, gameEngine);
        CampaignController campaignController = new CampaignController(gameEngine);
        CampaignView campaignView = new CampaignView(campaignController, gameEngine);
        menuView.show();

    }
}
