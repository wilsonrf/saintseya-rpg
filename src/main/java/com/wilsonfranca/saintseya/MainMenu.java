package com.wilsonfranca.saintseya;

import com.wilsonfranca.saintseya.util.FileLoadException;
import com.wilsonfranca.saintseya.util.FilesLoader;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wilson.franca on 06/04/18.
 */
public class MainMenu {

    private FilesLoader filesLoader;

    public MainMenu() {
        this.filesLoader = new FilesLoader(this.getClass().getClassLoader());
    }

    public List<MenuOption> getMenuOptions() {

        List<MenuOption> menuOptions ;

        try (Stream<String> stringStream = filesLoader.loadFileAsStringStream("data/menuoptions.txt")) {

            menuOptions = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .map(s -> new MenuOption(Integer.valueOf(s.substring(0, 1)), s.substring(1, s.length())))
                    .collect(Collectors.toList());

            return menuOptions;

        } catch (FileLoadException e) {
            throw new IllegalStateException("There is a problem loading the menu options file");
        }

    }

    public String banner() {

        String banner;

        try (Stream<String> stringStream = filesLoader.loadFileAsStringStream("misc/banner.txt")) {

            banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));

            return banner;

        } catch (FileLoadException e) {
            throw new IllegalStateException("There is a problem loading the banner file");
        }

    }

}
