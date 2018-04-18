package com.wilsonfranca.saintseya.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Created by wilson.franca on 13/04/18.
 */
public class FilesHelper {

    public void save(String fileName, byte[] data) {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s.data", savesPath, fileName);

        Path path = Paths.get(stringPath);

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(path, CREATE, TRUNCATE_EXISTING))) {

            out.write(data, 0, data.length);

        } catch (IOException e) {
            throw new IllegalStateException("Error creating file.");
        }
    }

    public byte[] load(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s.data", savesPath, fileName);

        Path path = Paths.get(stringPath);

        if(Files.exists(path)) {

            try {

                return Files.readAllBytes(path);

            } catch (IOException e) {
                throw new FileLoadException("Error loading file", path, e);
            }

        } else {
            return new byte[0];
        }
    }

    public Stream<String> loadFileAsStringStream(final String filePath) {

        ClassLoader classLoader = getClass().getClassLoader();

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
