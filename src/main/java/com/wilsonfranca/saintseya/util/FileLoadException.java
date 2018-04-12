package com.wilsonfranca.saintseya.util;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by wilson on 11/04/18.
 */
public class FileLoadException extends RuntimeException {

    private Path path;

    public FileLoadException(String message, Path path, IOException e) {
        super(message, e);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
