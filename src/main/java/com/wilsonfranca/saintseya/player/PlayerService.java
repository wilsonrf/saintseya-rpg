package com.wilsonfranca.saintseya.player;

import com.wilsonfranca.saintseya.util.FilesHelper;

/**
 * Created by wilson.franca on 18/04/18.
 */
public class PlayerService {

    private FilesHelper filesHelper;

    public PlayerService() {
        this.filesHelper = new FilesHelper();
    }

    public void save(Player player) {
        filesHelper.save(player.getPersistentPath(), player.getPersistentData());
    }

    public void deleteAllData(Player player) {
        filesHelper.deleteAllKnightData(player.getName());
    }
}
