package com.wilsonfranca.saintseya.util;

import com.wilsonfranca.saintseya.Campaign;
import com.wilsonfranca.saintseya.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by wilson.franca on 11/04/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FilesLoader.class})
public class FilesLoaderTest {

    FilesLoader filesLoader;

    @InjectMocks
    Game game;

    @Mock
    Campaign campaign;

    @Before
    public void before() {
        filesLoader = new FilesLoader(this.getClass().getClassLoader());
    }

    @Test
    public void test_load_a_stream_of_text_file_with_content_and_validate_it() throws IOException {


        Stream<String> stringStream = filesLoader.loadFileAsStringStream("misc/banner_test_text.txt");

        assertThat(stringStream).isNotNull();

        assertThat(stringStream.findFirst().get()).isEqualTo("Banner test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_not_load_a_stream_text_of_a_non_existing_file_and_get_exception() throws IOException {


        Stream<String> stringStream = filesLoader.loadFileAsStringStream("not_existing_file");

    }

    @Test(expected = FileLoadException.class)
    public void test_not_load_a_stream_if_there_is_ioexception_and_got_file_load_exption() throws IOException {

        mockStatic(Files.class);

        when(Files.lines(any())).thenThrow(IOException.class);

        Stream<String> stringStream = filesLoader.loadFileAsStringStream("misc/banner_test_text.txt");

    }

    @Test
    public void test_load_a_outputstream_of_a_game_save_successful() throws IOException {

        mockStatic(Files.class);

        when(campaign.getId()).thenReturn("player_campaing");

        when(Files.exists(any(Path.class))).thenReturn(true);

        when(Files.newOutputStream(any(), any())).thenReturn(new ByteArrayOutputStream());

        OutputStream outputStream = filesLoader.loadSavedFile(game);

        assertThat(outputStream).isNotNull();
    }

    @Test(expected = FileLoadException.class)
    public void test_try_to_load_a_outputstream_of_a_game_which_not_exists_and_got_file_load_exception() {

        mockStatic(Files.class);

        when(campaign.getId()).thenReturn("player_campaing");

        when(Files.exists(any(Path.class))).thenReturn(false);

        OutputStream outputStream = filesLoader.loadSavedFile(game);

    }

    @Test(expected = FileLoadException.class)
    public void test_try_to_load_a_outputstream_of_a_existing_saved_game_but_got_io_exception_and_return_file_load_exception() throws IOException {

        mockStatic(Files.class);

        when(campaign.getId()).thenReturn("player_campaing");

        when(Files.exists(any(Path.class))).thenReturn(true);

        when(Files.newOutputStream(any(), any())).thenThrow(IOException.class);

        OutputStream outputStream = filesLoader.loadSavedFile(game);
    }
}
