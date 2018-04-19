package com.wilsonfranca.saintseya.menu;

import com.wilsonfranca.saintseya.GameEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by wilson on 18/04/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class MenuControllerTest {

    MenuController menuController;

    @Mock
    GameEngine gameEngine;

    @Before
    public void before() {
        menuController = new MenuController(gameEngine);
    }

    @Test
    public void test_if_choose_1_and_call_create_new_game() {

        doNothing().when(gameEngine).newGame();

        menuController.execute(1);

        verify(gameEngine).newGame();
    }

    @Test
    public void test_if_choose_2_and_call_create_load_game() {

        doNothing().when(gameEngine).loadGame();

        menuController.execute(2);

        verify(gameEngine).loadGame();
    }

    @Test
    public void test_if_choose_3_and_call_create_exit_game() {

        doNothing().when(gameEngine).exitGame();

        menuController.execute(3);

        verify(gameEngine).exitGame();
    }

    @Test
    public void test_if_choose_other_number_and_call_menu() {

        doNothing().when(gameEngine).menu();

        menuController.execute(0);

        menuController.execute(10123123);

        verify(gameEngine, times(2)).menu();
    }
}
