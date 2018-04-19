package com.wilsonfranca.saintseya.menu;

import com.wilsonfranca.saintseya.GameEngine;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Created by wilson on 15/04/18.
 */
public class MenuView implements Observer {

    private final MenuController menuController;
    private final GameEngine gameEngine;
    private final Scanner scanner;

    public MenuView(final MenuController menuController, final GameEngine gameEngine) {
        this.menuController = menuController;
        this.gameEngine = gameEngine;
        this.gameEngine.addObserver(this);
        this.scanner = new Scanner(System.in);
    }

    public void init() {
        menuController.execute(0);
    }

    public void show() {
        if(this.gameEngine.getMenu() == null) {
            menuController.execute(0);
        } else {
            System.out.println(gameEngine.getMenu());
            askForOption();
        }
    }


    private void showExit() {
        System.out.println("Bye Bye!");
    }

    private void askForOption() {
        int option = scanner.nextInt();
        menuController.execute(option);
    }

    @Override
    public void update(Observable o, Object arg) {
        String action = (String) arg;
        if("menu".equalsIgnoreCase(action)) {
            show();
        } else if("exitGame".equalsIgnoreCase(action)) {
            showExit();
        }
    }
}
