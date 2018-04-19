package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.*;
import com.wilsonfranca.saintseya.player.Player;
import com.wilsonfranca.saintseya.util.FileLoadException;
import com.wilsonfranca.saintseya.util.FilesHelper;

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

    private final QuestController questController;
    private final GameEngine gameEngine;
    private final Scanner scanner;
    private FilesHelper filesHelper;

    public QuestView(final QuestController questController, final GameEngine gameEngine) {
        this.questController = questController;
        this.gameEngine = gameEngine;
        this.gameEngine.addObserver(this);
        this.scanner = new Scanner(System.in);
        this.filesHelper = new FilesHelper();
    }

    public void setFilesHelper(FilesHelper filesHelper) {
        this.filesHelper = filesHelper;
    }

    protected void show() {

        if (this.gameEngine.getQuest() == null) {
            // Create Quest here
            // Hard coded quest fenix_quest
            //TODO support more quests
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

            if (enemy.isDamaged()) {
                System.out.println(String.format("You hit %s!", enemy.getName()));

            }

            if (player.isLeveledUp()) {
                System.out.println("Level UP!");
                System.out.printf("You're now on level %d.", this.gameEngine.getPlayer().getLevel());
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
        } else if (player.isDead()) {
            System.out.println(String.format("Knight %s of %s is dead!", player.getName(),
                    player.getConstellation().getDescription().toLowerCase()));
            System.out.println("Game Over.");
        } else if (enemy.isDead()) {
            System.out.println(String.format("You have killed %s!", enemy.getName()));
            show();
        } else {
            show();
        }
    }

    private void showDidntRanAway() {
        System.out.println("You didn't ran away");
        showBattle();
    }

    private void showRanAway() {
        System.out.println("You ran away!");
        show();
    }

    private void showAlredyBattle() {
        System.out.println("You already fight this enemy!");
        show();
    }

    private void questBanner(Quest quest) {

        String path = String.format("quest/%s_banner.txt", quest.getId().toLowerCase());

        try (Stream<String> stringStream = filesHelper.loadFileAsStringStream(path)) {

            String banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));
            System.out.println(banner);

        } catch (FileLoadException e) {
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
            showRanAway();
        } else if("didntRanWay".equalsIgnoreCase(action)) {
            showDidntRanAway();
        } else if("battleAlreadyCompleted".equalsIgnoreCase(action)) {
            showAlredyBattle();
        }
    }

}
