package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.*;
import com.wilsonfranca.saintseya.battle.Battle;
import com.wilsonfranca.saintseya.battle.Enemy;
import com.wilsonfranca.saintseya.player.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wilson on 16/04/18.
 */
public class QuestView implements Observer {

    QuestController questController;
    GameEngine gameEngine;
    Scanner scanner;

    public QuestView(QuestController questController, GameEngine gameEngine) {
        this.questController = questController;
        this.gameEngine = gameEngine;
        this.gameEngine.addObserver(this);
        this.scanner = new Scanner(System.in);
    }

    private void show() {

        if (this.gameEngine.getQuest() == null) {
            // Create Quest here
            Quest quest = new Quest("fenix_quest");
            questBanner(quest);
            questController.execute(quest);
        } else if (this.gameEngine.getQuest().getQuestPart() != null) {
            String nextId;
            if (this.gameEngine.getQuest().getQuestPart().getNext() == null) {
                partBanner(this.gameEngine.getQuest().getQuestPart());
                nextId = this.gameEngine.getQuest().getNext(scanner.nextLine());
            } else {
                nextId = this.gameEngine.getQuest().getQuestPart().getNext();
            }
            questController.execute(nextId);
        }

    }

    private void showRewarded() {

        partBanner(this.gameEngine.getQuest().getQuestPart());

        System.out.println("You got some experience and health points!");
        System.out.println(String.format("Now you have %d experience and %d health poitns!",
                this.gameEngine.getPlayer().getExperience(), this.gameEngine.getPlayer().getHealthPoints()));
        String nextId;
        if (this.gameEngine.getQuest().getQuestPart().getNext() == null) {
            nextId = this.gameEngine.getQuest().getNext(scanner.nextLine());
        } else {
            nextId = this.gameEngine.getQuest().getQuestPart().getNext();
        }
        questController.execute(nextId);
    }

    private void showBattle() {
        Battle battle = this.gameEngine.getQuest().getQuestPart().getBattle();
        Player player = this.gameEngine.getPlayer();
        Enemy enemy = this.gameEngine.getQuest().getQuestPart().getEnemy();
        if (!battle.isEnded() && (!player.isDead() && !enemy.isDead())) {
            partBanner(this.gameEngine.getQuest().getQuestPart());
            if (player.isDamaged()) {
                System.out.println(String.format("%s hit You!", enemy.getName()));
            }

            if (enemy.isHitted()) {
                System.out.println(String.format("You hit %s!", enemy.getName()));

            }

            System.out.println(String.format("%s Health Points: %d", player.getName(), player.getHealthPoints()));
            System.out.println(String.format("%s Health Points: %d", enemy.getName(), enemy.getHealthPoints()));

            String option = null;
            while (option == null || (!"1".equals(option) && !"2".equals(option))) {
                System.out.println(String.format("::::::%s of %s what you want to do?::::::", player.getName(),
                        player.getConstellation().getDescription()));
                System.out.println("1) Attack");
                System.out.println("2) Run away");
                option = scanner.nextLine();
            }
            questController.execute(enemy, option);
        } else {
            show();
        }
    }

    private void showDidntRunAway() {
        System.out.println("You didn't ran away");
        showBattle();
    }

    private void showRunAway() {
        System.out.println("You ran away!");
        show();
    }

    private void questBanner(Quest quest) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("quest/%s_banner.txt",
                quest.getId().toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            String banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));
            System.out.println(banner);

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest banner file");
        }
    }

    private void partBanner(QuestPart questPart) {

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource(String.format("quest/%s.txt",
                questPart.getId().toLowerCase())).getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            String banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));
            System.out.println(banner);

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the quest banner file");
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        String action = (String) arg;
        if("rewardedQuest".equalsIgnoreCase(action)) {
            showRewarded();
        } else if("battle".equalsIgnoreCase(action)) {
            showBattle();
        } else if("startQuest".equalsIgnoreCase(action) ||
                "startQuestPart".equalsIgnoreCase(action)) {
            show();
        } else if("ranAway".equalsIgnoreCase(action)) {
            showRunAway();
        } else if("didntRanWay".equalsIgnoreCase(action)) {
            showDidntRunAway();
        }
    }

}
