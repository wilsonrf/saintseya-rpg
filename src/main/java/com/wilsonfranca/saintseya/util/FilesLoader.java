package com.wilsonfranca.saintseya.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by wilson.franca on 11/04/18.
 */
public class FilesLoader {

    public Stream<String> loadFileAsStringStream(final String filePath) throws IOException {

        ClassLoader classLoader = this.getClass().getClassLoader();

        URL url = classLoader.getResource(filePath);

        if(url != null) {
            return Files.lines(Paths.get(url.getPath()));
        } else {
            throw new IllegalArgumentException(String.format("The file path %s does not exists", filePath));
        }

    }
}
