package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.GameEngine;
import com.wilsonfranca.saintseya.Quest;
import com.wilsonfranca.saintseya.QuestPart;

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
        } else if (this.gameEngine.getQuest().getCurrent() != null) {
            partBanner(this.gameEngine.getQuest().getCurrent());
            String nextId;
            if (this.gameEngine.getQuest().getCurrent().getNext() == null) {
                nextId = this.gameEngine.getQuest().getNext(scanner.nextLine());
            } else {
                nextId = this.gameEngine.getQuest().getCurrent().getNext();
            }
            questController.execute(nextId);
        }

    }

    private void showRewarded() {

        System.out.println("You got some experience and health points!");
        System.out.println(String.format("Now you have %d experience and %d health poitns!",
                this.gameEngine.getPlayer().getExperience(), this.gameEngine.getPlayer().getHealthPoints()));
        partBanner(this.gameEngine.getQuest().getCurrent());
        String nextId;
        if (this.gameEngine.getQuest().getCurrent().getNext() == null) {
            nextId = this.gameEngine.getQuest().getNext(scanner.nextLine());
        } else {
            nextId = this.gameEngine.getQuest().getCurrent().getNext();
        }
        questController.execute(nextId);
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
        } else if("rewardedQuest".equalsIgnoreCase(action)) {

        } else if("startQuest".equalsIgnoreCase(action) ||
                "startQuestPart".equalsIgnoreCase(action)) {
            show();
        }
    }

}
