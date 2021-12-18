package eu.craftok.gameapi.game.status;

/**
 * Project game-api Created by Sithey
 */

public enum EGameStatus {

    LOBBY(new GameStatus("Lobby", true, false, false)),
    GAME(new GameStatus("Game", false, true, false)),
    FINISH(new GameStatus("Finish", false, false, true));

    private GameStatus gameStatus;

    EGameStatus(GameStatus gameStatus){
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
