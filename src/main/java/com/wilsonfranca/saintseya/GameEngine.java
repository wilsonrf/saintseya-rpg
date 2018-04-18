package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.player.PlayerService;
import com.wilsonfranca.saintseya.quest.Battle;
import com.wilsonfranca.saintseya.quest.Enemy;
import com.wilsonfranca.saintseya.menu.MenuService;
import com.wilsonfranca.saintseya.player.Player;
import com.wilsonfranca.saintseya.quest.Quest;
import com.wilsonfranca.saintseya.quest.QuestPart;
import com.wilsonfranca.saintseya.quest.QuestService;

import java.util.Observable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wilson on 15/04/18.
 */
public class GameEngine extends Observable {

    private String menu;

    private Player player;

    private Quest quest;

    protected MenuService menuService;

    protected QuestService questService;

    protected PlayerService playerService;

    public GameEngine() {
        this.menuService = new MenuService();
        this.questService = new QuestService();
        this.playerService = new PlayerService();
    }

    public Quest getQuest() {
        return quest;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMenu() {
        return menu;
    }

    public void menu() {
        this.menu = Stream.of(menuService.banner(), menuService.options())
                .collect(Collectors.joining("\n"));
        setChanged();
        notifyObservers("menu");
    }

    public void newGame() {
        setChanged();
        notifyObservers("newGame");
    }

    public void loadGame() {
        setChanged();
        notifyObservers("loadGame");
    }

    public void exitGame() {
        setChanged();
        notifyObservers("exitGame");
    }

    public void createKnight(Player player) {
        this.player = player;
        playerService.save(this.player);
        setChanged();
        notifyObservers("startQuest");
    }

    public void startQuest(Quest quest) {
        // try to load
        this.quest = questService.load(player, quest);
        QuestPart part = questService.questPart(this.quest.getId(), this.quest.getQuestPart().getId());
        this.getQuest().setQuestPart(part);
        questService.save(this.player, quest);
        setChanged();
        notifyObservers("startQuestPart");
    }

    public void startQuestPart(String partId) {
        QuestPart part = questService.questPart(this.quest.getId(), partId);
        this.getQuest().setQuestPart(part);
        questService.save(player, part);
        if (part.hasReward()) {
            if (!questService.isPartloadedAndCompleted(player, part)) {
                player.addXp(part.getReward().getXp());
                player.addHp(part.getReward().getHp());
            }
            part.complete();
            setChanged();
            notifyObservers("rewardedQuest");
        } else if (part.hasEnemy()) {
            if (!questService.isPartloadedAndCompleted(player, part)) {
                Battle battle = new Battle(player, part.getEnemy());
                this.quest.getQuestPart().setBattle(battle);
                setChanged();
                notifyObservers("battle");
            } else {
                setChanged();
                notifyObservers("battleAlreadyCompleted");
            }

        } else {
            setChanged();
            notifyObservers("startQuestPart");
        }
    }

    public void attack(Enemy enemy) {
        player.attack(enemy);
        if(enemy.isDead()) {
            player.won();
        } else {
            enemy.attack(this.getPlayer());
        }
        setChanged();
        notifyObservers("battle");
    }

    public void runAway() {
        boolean run = player.runAway();
        if(run) {
            quest.getQuestPart().getBattle().end();
            setChanged();
            notifyObservers("ranAway");
        } else {
            setChanged();
            notifyObservers("didntRanWay");
        }
    }
}
