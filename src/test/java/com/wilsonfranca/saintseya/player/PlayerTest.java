package com.wilsonfranca.saintseya.player;

import com.wilsonfranca.saintseya.util.FilesHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by wilson.franca on 13/04/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerTest {

    @Mock
    FilesHelper filesHelper;

    Player player;

    @Before
    public void setup() {
        player = new Player("player", Constellation.ANDROMEDA);
        player.setFilesHelper(filesHelper);
    }

    @Test
    public void test_load_successful_load_a_player() {
        String stringData = "name:player constellation:andromeda health_points:15 hit_points:2 experience_points:0 recovery_xp:1 recovery_hp:0";
        byte [] data = stringData.getBytes();
        when(filesHelper.load(anyString())).thenReturn(data);
        Player loaded = new Player(filesHelper.load("player"));
        assertThat(loaded.getName()).isEqualTo("player");
        assertThat(loaded.getConstellation()).isEqualTo(Constellation.ANDROMEDA);
        assertThat(loaded.getHealthPoints()).isEqualTo(15);
    }
}
