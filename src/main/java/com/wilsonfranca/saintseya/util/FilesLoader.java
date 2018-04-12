package com.wilsonfranca.saintseya.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by wilson.franca on 11/04/18.
 */
public class FilesLoader {

    private final ClassLoader classLoader;

    public FilesLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Stream<String> loadFileAsStringStream(final String filePath) {

        URL url = classLoader.getResource(filePath);

        if(url != null) {

            Path path = Paths.get(url.getPath());

            try {

                return Files.lines(path);

            } catch (IOException e) {
                throw new FileLoadException("Error on loading file", path, e);
            }

        } else {
            throw new IllegalArgumentException(String.format("The file path %s does not exists", filePath));
        }

    }
}
