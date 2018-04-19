package com.wilsonfranca.saintseya.player;

import com.wilsonfranca.saintseya.util.FilesHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by wilson on 18/04/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {

    @InjectMocks
    PlayerService playerService;

    @Mock
    FilesHelper filesHelper;

    @Before
    public void before() {
        playerService = new PlayerService();
        playerService.setFilesHelper(filesHelper);
    }

    @Test
    public void test_load_successful_load_a_player() {
        String stringData = "name:player;constellation:andromeda;health_points:15;hit_points:2;experience_points:0;recovery_xp:1;recovery_hp:0";
        byte [] data = stringData.getBytes();
        when(filesHelper.load(anyString())).thenReturn(data);
        Player toLoad = new Player("player", Constellation.ANDROMEDA);
        Player loaded = playerService.load(toLoad);
        assertThat(loaded.getName()).isEqualTo("player");
        assertThat(loaded.getConstellation()).isEqualTo(Constellation.ANDROMEDA);
        assertThat(loaded.getHealthPoints()).isEqualTo(15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_try_load_a_null_player_and_got_illegal_argument_exception() {
        Player toLoad = null;
        playerService.load(toLoad);
        verify(filesHelper, never()).load(anyString());
    }

    @Test
    public void test_save_successful_a_player() {
        Player toSave = new Player("player", Constellation.ANDROMEDA);
        doNothing().when(filesHelper).save(anyString(), any());
        playerService.save(toSave);
        verify(filesHelper, times(1)).save(anyString(), any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_try_save_a_null_player_and_got_illegal_argument_exception() {
        Player toSave = null;
        playerService.save(toSave);
        verify(filesHelper, never()).save(anyString(), any());
    }

    @Test
    public void test_delete_all_data_successful_a_player() {
        Player toDelete = new Player("player", Constellation.ANDROMEDA);
        doNothing().when(filesHelper).deleteAllKnightData(anyString());
        playerService.deleteAllData(toDelete);
        verify(filesHelper, times(1)).deleteAllKnightData(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_try_delete_all_data_of_a_null_player_and_got_illegal_argument_exception() {
        Player toDelete = null;
        playerService.deleteAllData(toDelete);
        verify(filesHelper, never()).deleteAllKnightData(anyString());
    }
}
