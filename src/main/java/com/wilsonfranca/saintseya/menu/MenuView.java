package com.wilsonfranca.saintseya.menu;

import com.wilsonfranca.saintseya.GameEngine;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Created by wilson on 15/04/18.
 */
public class MenuView implements Observer {

    MenuController menuController;
    GameEngine gameEngine;

    public MenuView(MenuController menuController, GameEngine gameEngine) {
        this.menuController = menuController;
        this.gameEngine = gameEngine;
        this.gameEngine.addObserver(this);
    }

    public void show() {
        System.out.println(gameEngine.menu());
        askForOption();
    }

    private void askForOption() {
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        menuController.execute(option);
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
