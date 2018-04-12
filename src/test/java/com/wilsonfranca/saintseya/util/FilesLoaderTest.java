package com.wilsonfranca.saintseya.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wilson.franca on 11/04/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Files.class})
public class FilesLoaderTest {

    FilesLoader filesLoader;

    ClassLoader classLoader;

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

//    @Test(expected = FileLoadException.class)
//    public void test_not_load_a_stream_if_there_is_ioexception_and_got_file_load_exption() {
//
//        mockStatic(Files.class);
//
//        doThrow(new IOException()).when(Files.class);
//
//        Stream<String> stringStream = filesLoader.loadFileAsStringStream("misc/banner_test_text.txt");
//
//    }
}
