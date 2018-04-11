package com.wilsonfranca.saintseya.util;

import org.junit.Test;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by wilson.franca on 11/04/18.
 */
public class FilesLoaderTest {

    @Test
    public void test_load_a_stream_of_text_file_with_content_and_validate_it() throws IOException {

        Stream<String> stringStream = new FilesLoader().loadFileAsStringStream("misc/banner_test_text.txt");

        assertThat(stringStream).isNotNull();

        assertThat(stringStream.findFirst().get()).isEqualTo("Banner test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_not_load_a_stream_text_of_a_non_existing_file_and_get_exception() throws IOException {

        Stream<String> stringStream = new FilesLoader().loadFileAsStringStream("not_existing_file");

    }
}
