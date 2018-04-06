package com.wilsonfranca.sayntseya;

/**
 * Created by wilson.franca on 06/04/18.
 */
public class Player {

    private String name;

    private Constellation constellation;

    public Player(String name, Constellation constellation) {
        this.name = name;
        this.constellation = constellation;
    }

    public String getName() {
        return name;
    }

    public Constellation getConstellation() {
        return constellation;
    }
}
