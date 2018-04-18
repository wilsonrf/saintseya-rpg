package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.battle.Battle;
import com.wilsonfranca.saintseya.battle.Enemy;
import com.wilsonfranca.saintseya.campaign.Campaign;
import com.wilsonfranca.saintseya.campaign.CampaignService;
import com.wilsonfranca.saintseya.player.Player;
import com.wilsonfranca.saintseya.menu.MenuService;
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

    private Campaign campaign;

    private Player player;

    private Quest quest;

    protected MenuService menuService;

    protected CampaignService campaignService;

    protected QuestService questService;

    public GameEngine() {
        this.menuService = new MenuService();
        this.campaignService = new CampaignService();
        this.questService = new QuestService();
    }

    public Campaign getCampaign() { return campaign; }

    public Quest getQuest() {
        return quest;
    }

    public Player getPlayer() {
        return player;
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

    public void createKnight(Player player) {
        this.player = player;
        player.save();
        setChanged();
        notifyObservers("startQuest");
    }

    public void startQuest(Quest quest) {
        this.quest = quest;
        //save
        QuestPart part = questService.questPart(this.quest.getId(), this.quest.getQuestPart().getId());
        this.getQuest().setQuestPart(part);
        setChanged();
        notifyObservers("startQuestPart");
    }

    public void startQuestPart(String partId) {
        QuestPart part = questService.questPart(this.quest.getId(), partId);
        this.getQuest().setQuestPart(part);
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
            setChanged();
            notifyObservers("enemyDead");
        } else {
            enemy.attack(this.getPlayer());
        }
        if(player.isDead()) {
            setChanged();
            notifyObservers("playerDead");
        } else {
            setChanged();
            notifyObservers("battle");
        }

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
