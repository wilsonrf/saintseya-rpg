package com.wilsonfranca.saintseya.quest;

import com.wilsonfranca.saintseya.GameEngine;
import com.wilsonfranca.saintseya.util.FilesHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by wilson on 18/04/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class QuestViewTest {

    @InjectMocks
    QuestView questView;

    @Mock
    QuestController questController;

    @Mock
    FilesHelper filesHelper;

    @Mock
    GameEngine gameEngine;

    @Before
    public void before() {
        questView = new QuestView(questController, gameEngine);
        questView.setFilesHelper(filesHelper);
    }

    @Test
    public void test_show_when_quest_is_null(){
        doNothing().when(questController).execute(any(Quest.class));
        when(filesHelper.loadFileAsStringStream(anyString())).thenReturn(Stream.empty());
        when(gameEngine.getQuest()).thenReturn(null);
        questView.show();
        verify(questController).execute(any(Quest.class));
    }

}
