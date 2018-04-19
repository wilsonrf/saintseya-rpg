package com.wilsonfranca.saintseya.player;

import com.wilsonfranca.saintseya.util.FilesHelper;

import java.util.Objects;

/**
 * Created by wilson.franca on 18/04/18.
 */
public class PlayerService {

    public static final String PLAYER_CAN_T_BE_NULL = "Player can't be null";

    private FilesHelper filesHelper;

    public PlayerService() {
        this.filesHelper = new FilesHelper();
    }

    public void save(Player player) {
        if(Objects.isNull(player)) {
            throw new IllegalArgumentException(PLAYER_CAN_T_BE_NULL);
        }
        filesHelper.save(player.getPersistentPath(), player.getPersistentData());
    }

    public void deleteAllData(Player player) {
        if(Objects.isNull(player)) {
            throw new IllegalArgumentException(PLAYER_CAN_T_BE_NULL);
        }
        filesHelper.deleteAllKnightData(player.getName());
    }

    public Player load(Player player) {
        if(Objects.isNull(player)) {
            throw new IllegalArgumentException(PLAYER_CAN_T_BE_NULL);
        }
        return new Player(filesHelper.load(player.getPersistentPath()));
    }

    public void setFilesHelper(FilesHelper filesHelper) {
        this.filesHelper = filesHelper;
    }
}
