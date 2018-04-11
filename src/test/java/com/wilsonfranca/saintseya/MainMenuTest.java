package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.util.FilesLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by wilson.franca on 11/04/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

    @Mock
    FilesLoader filesLoader;

    @InjectMocks
    MainMenu mainMenu;

    @Test
    public void test_load_a_banner_as_string_if_file_exists() throws IOException {

        Stream<String> stringStream = Stream.of("banner");

        when(filesLoader.loadFileAsStringStream(anyString())).thenReturn(stringStream);

        String banner = mainMenu.banner();

        assertThat(banner).isNotEmpty();
        assertThat(banner).isNotNull();
        assertThat(banner).isEqualTo("banner");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_try_to_load_a_not_existing_banner_file_and_fail() throws IOException {

        when(filesLoader.loadFileAsStringStream(anyString())).thenThrow(IllegalArgumentException.class);

        String banner = mainMenu.banner();

        assertThat(banner).isNull();
    }
}
