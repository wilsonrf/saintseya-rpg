package com.wilsonfranca.sayntseya;

/**
 * Created by wilson.franca on 06/04/18.
 */
public enum Constellation {

    PEGASUS(1, "Pegasus"),
    DRAGON(2, "Dragon"),
    ANDROMEDA(3, "Andromeda"),
    SWAN(4, "Swan");

    private final String description;
    private final int id;

    Constellation(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static Constellation getById(int id) {
        for (Constellation constellation: values()) {
            if(constellation.id == id) {
                return constellation;
            }
        }
        throw new IllegalArgumentException("Constalation not valid");
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}
