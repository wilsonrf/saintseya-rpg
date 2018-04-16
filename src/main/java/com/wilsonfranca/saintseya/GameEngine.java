package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.campaign.Campaign;
import com.wilsonfranca.saintseya.menu.MenuService;

import java.util.Observable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wilson on 15/04/18.
 */
public class GameEngine extends Observable {

    private Campaign campaign;

    private Player player;

    protected MenuService menuService;


    public GameEngine() {
        this.menuService = new MenuService();
    }

    public String menu() {
        return Stream.of(menuService.banner(), menuService.options())
                .collect(Collectors.joining("\n"));
    }

    public void newGame() {
        this.campaign = new Campaign();
        setChanged();
        notifyObservers("newCampaign");
    }

    public void loadGame() {

    }

    public void exitGame() {

    }

    public Player getPlayer() {
        return player;
    }

    public void createKnight(Player player) {
        this.player = player;
        //save
        setChanged();
        notifyObservers("newQuest");
    }
}
