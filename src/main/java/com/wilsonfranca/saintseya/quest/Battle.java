package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.player.Player;

/**
 * Created by wilson on 10/04/18.
 */
public class Battle {

    private Player player;

    private Enemy enemy;

    private boolean ended;

    public Battle(final Player player, final Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    public boolean isEnded() {
        return ended;
    }

    public void end() {
        this.ended = true;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }
}
