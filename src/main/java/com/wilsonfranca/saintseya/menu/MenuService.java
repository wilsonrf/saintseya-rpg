package com.wilsonfranca.saintseya.menu;

import com.wilsonfranca.saintseya.util.FileLoadException;
import com.wilsonfranca.saintseya.util.FilesHelper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wilson on 15/04/18.
 */
public class MenuService {

    private FilesHelper filesHelper;

    public MenuService() {
        this.filesHelper = new FilesHelper();
    }

    private List<MenuOption> menuOptions() {
        List<MenuOption> menuOptions ;

        try (Stream<String> stringStream = filesHelper.loadFileAsStringStream("data/menuoptions.txt")) {

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

        try (Stream<String> stringStream = filesHelper.loadFileAsStringStream("misc/banner.txt")) {

            banner = stringStream
                    .filter(s -> !"".equals(s) && s != null)
                    .collect(Collectors.joining("\n"));

            return banner;

        } catch (FileLoadException e) {
            throw new IllegalStateException("There is a problem loading the banner file");
        }

    }

    public String options() {

        String options = this.menuOptions()
                .stream()
                .map(menuOption -> String.format("%d) %s", menuOption.getId(), menuOption.getText()))
                .collect(Collectors.joining("\n"));

        return options;
    }

}
