package eu.craftok.gameapi.game.settings;

import eu.craftok.utils.CConfig;

/**
 * Project game-api Created by Sithey
 */

public class GameSettings {

    private CConfig config;

    private int games, minPlayers, maxPlayers, sizeTeams, startTimer, stopTimer;
    private int host_maxPlayers, host_sizeTeams;

    private boolean host, keep_inventory;

    public GameSettings(CConfig config){
        this.config = config;

        initSettings();
    }

    /**
     * Initiation of the settings file
     */

    public void initSettings(){
        config.addValue("games", 20);
        games = (int) config.getValue("games");
        config.addValue("players.minimum", 4);
        minPlayers = (int) config.getValue("players.minimum");
        config.addValue("players.maximum", 8);
        maxPlayers = (int) config.getValue("players.maximum");
        config.addValue("teams.size", 1);
        sizeTeams = (int) config.getValue("teams.size");
        config.addValue("timer.start", 120);
        startTimer = (int) config.getValue("timer.start");
        config.addValue("timer.stop", 10);
        stopTimer = (int) config.getValue("timer.stop");
        config.addValue("host.enabled", false);
        host = (boolean) config.getValue("host.enabled");
        config.addValue("host.maxplayers", 100);
        host_maxPlayers = (int) config.getValue("host.maxplayers");
        config.addValue("host.maxteamsize", 5);
        host_sizeTeams = (int) config.getValue("host.maxteamsize");
        config.addValue("keep_inventory", true);
        keep_inventory = (boolean) config.getValue("keep_inventory");
    }

    /**
     *
     * @return the file Configuration
     */
    public CConfig getConfig(){
        return config;
    }


    /**
     * Set the maximum games per server
     * @param games the next amount
     */

    public void setGames(int games) {
        this.games = games;
    }

    /**
     * @return the amount of games before a completely stop
     */

    public int getGames() {
        return games;
    }

    /**
     * @return minimum players to start
     */


    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * @return maximum players per games
     */

    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Set the amount of the maximum players per game
     * @param maxPlayers the next amount
     */

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * @return the amount of players per teams
     */

    public int getSizeTeams() {
        return sizeTeams;
    }

    /**
     * Set the amounts of players per teams
     * @param sizeTeams the next amount
     */

    public void setSizeTeams(int sizeTeams) {
        this.sizeTeams = sizeTeams;
    }

    /**
     * @return the time before the beginning of the game
     */

    public int startTimer() {
        return startTimer;
    }

    /**
     * Set the amount of time before the starting task
     * @param startTimer
     */

    public void setStartTimer(int startTimer) {
        this.startTimer = startTimer;
    }

    /**
     * @return the time when the game is finished and be removed
     */

    public int stopTimer() {
        return stopTimer;
    }


    /**
     * @return if the server is in Host mode
     */

    public boolean isHost() { return host; }

    public int getHost_maxPlayers() {
        return host_maxPlayers;
    }

    public int getHost_sizeTeams() {
        return host_sizeTeams;
    }

    public boolean isKeep_inventory() {
        return keep_inventory;
    }


}
