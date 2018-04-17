package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.campaign.Campaign;
import com.wilsonfranca.saintseya.campaign.CampaignService;
import com.wilsonfranca.saintseya.menu.MenuService;
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
        notifyObservers("startQuest");
    }

    public void startQuest(Quest quest) {
        this.quest = quest;
        //save
        QuestPart part = questService.questPart(this.quest.getId(), this.quest.getCurrent().getId());
        this.getQuest().setCurrent(part);
        setChanged();
        notifyObservers("startQuestPart");
    }

    public Quest getQuest() {
        return quest;
    }


    public void startQuestPart(String partId) {
        QuestPart part = questService.questPart(this.quest.getId(), partId);
        this.getQuest().setCurrent(part);
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
                Fight fight = new Fight(player, part.getEnemy());
                //contiue fight logic
            }

        } else {
            setChanged();
            notifyObservers("startQuestPart");
        }
    }
}
