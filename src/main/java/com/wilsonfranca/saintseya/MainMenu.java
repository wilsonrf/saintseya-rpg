package com.wilsonfranca.saintseya;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wilson.franca on 06/04/18.
 */
public class MainMenu {


    public List<MenuOption> getMenuOptions() {

        List<MenuOption> menuOptions ;

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource("data/menuoptions.txt").getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            menuOptions = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(s -> new MenuOption(Integer.valueOf(s.substring(0, 1)), s.substring(1, s.length())))
                    .collect(Collectors.toList());

            return menuOptions;

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the menu options file");
        }

    }

    public String banner() {

        String banner;

        ClassLoader classLoader = getClass().getClassLoader();

        String path = classLoader.getResource("misc/banner.txt").getPath();

        try (Stream<String> stringStream = Files.lines(Paths.get(path))) {

            banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));

            return banner;

        } catch (IOException e) {
            throw new IllegalStateException("There is a problem loading the banner file");
        }

    }

}
