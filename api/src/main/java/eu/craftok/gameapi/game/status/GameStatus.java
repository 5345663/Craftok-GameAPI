package eu.craftok.gameapi.game.status;

/**
 * Project game-api Created by Sithey
 */

public class GameStatus {

    private final String name;
    private final boolean lobby, game, finish;

    public GameStatus(String name, boolean lobby, boolean game, boolean finish){
        this.name = name;
        this.lobby = lobby;
        this.game = game;
        this.finish = finish;
    }

    /**
     * @return the name of the current Status
     */

    public final String getName() {
        return name;
    }

    /**
     * @return if the Status is in lobby
     */

    public final boolean isLobby() {
        return lobby;
    }

    /**
     * @return if the Status is in game
     */

    public final boolean isGame() {
        return game;
    }

    /**
     * @return if the Status is in finish
     */

    public final boolean isFinish() {
        return finish;
    }

}
