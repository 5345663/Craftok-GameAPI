package eu.craftok.gameapi.game.host;

import eu.craftok.gameapi.game.player.GamePlayer;
import eu.craftok.gameapi.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Project game-api Created by Sithey
 */

public class GameHost {

    private GamePlayer host;
    private List<GameWorld> worlds;

    public GameHost(){
        this.host = null;
        this.worlds = new ArrayList<>();
    }

    /**
     * @return the Host of the Game
     */

    public GamePlayer getHost() {
        return host;
    }

    /**
     *
     * @param host is the next Host
     */

    public void setHost(GamePlayer host) {
        this.host = host;
    }

    public List<GameWorld> getWorlds() {
        return worlds;
    }
}
