package com.wilsonfranca.saintseya;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wilson.franca on 12/04/18.
 */
public class GameTest {

    private Game game;

    @Before
    public void before() {
        game = new Game();
    }

    @Test
    public void test_load_a_previous_saved_game_stressful() {

        game.load("player_campaign");

        assertThat(game.getCampaign().getId()).isEqualTo("player_campaign");

        assertThat(game.getCampaign().getPlayer().getName().toLowerCase()).isEqualTo("player");
    }
}
