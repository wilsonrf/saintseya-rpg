package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.util.FilesLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by wilson.franca on 12/04/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    private Game game;

    @Mock
    private Campaign campaign;

    @Mock
    private MainMenu mainMenu;

    @Mock
    private FilesLoader filesLoader;

    @Test
    public void test_load_a_previous_saved_game_stressful() {


        game = new Game();

        game.setCampaign(campaign);

        game.load();

        when(campaign.getId()).thenReturn("player_campaign");

        assertThat(game.getCampaign().getId()).isEqualTo("player_campaign");

        assertThat(game.getCampaign().getPlayer().getName().toLowerCase()).isEqualTo("player");
    }
}
