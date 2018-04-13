package com.wilsonfranca.saintseya.util;

/**
 * Created by wilson.franca on 13/04/18.
 */
public interface Persistent <T> {

    String getPersistentPath();
    byte[] getPersistentData();
    void save();
    T load();
}
