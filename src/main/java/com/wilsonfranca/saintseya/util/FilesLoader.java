package com.wilsonfranca.saintseya.util;

import com.wilsonfranca.saintseya.campaign.Campaign;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

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

    public OutputStream loadSavedFile(Campaign campaign) {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s.data", savesPath, campaign.getId());

        Path path = Paths.get(stringPath);

        if(Files.exists(path)) {

            try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(path, READ))) {
                return out;
            } catch (IOException e) {
                throw new FileLoadException("Error on loading file", path, e);
            }

        } else {
            throw new FileLoadException("File not found!");
        }
    }

    public OutputStream saveFile(String filePath) {

        ClassLoader classLoader = getClass().getClassLoader();

        String savesString = classLoader.getResource("saves/saves.data").getPath();

        Path savesFilePath = Paths.get(savesString);

        String savesPath = savesFilePath.getParent().toString();

        String stringPath = String.format("%s/%s.data", savesPath, filePath);

        Path path = Paths.get(stringPath);

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(path, CREATE, TRUNCATE_EXISTING))) {
            return out;
        } catch (IOException e) {
            throw new FileLoadException("Error on saving file", path, e);
        }

    }

}
