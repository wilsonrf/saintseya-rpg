package com.wilsonfranca.saintseya.menu;

/**
 * Created by wilson on 04/04/18.
 */
public class MenuOption {

    private Integer id;
    private String text;

    public MenuOption(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
