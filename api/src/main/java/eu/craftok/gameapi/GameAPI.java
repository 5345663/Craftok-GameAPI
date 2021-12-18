package eu.craftok.gameapi;

import eu.craftok.core.common.CoreCommon;
import eu.craftok.gameapi.command.ReCommand;
import eu.craftok.gameapi.command.StartCommand;
import eu.craftok.gameapi.command.StatsCommand;
import eu.craftok.gameapi.event.player.PlayerConnectionEvent;
import eu.craftok.gameapi.event.player.PlayerDamageEvent;
import eu.craftok.gameapi.event.player.PlayerDeathEvent;
import eu.craftok.gameapi.event.player.PlayerInventoryEvent;
import eu.craftok.gameapi.event.server.MotdEvent;
import eu.craftok.gameapi.event.world.WorldCancelEvent;
import eu.craftok.gameapi.game.GameManager;
import eu.craftok.gameapi.game.player.GamePlayerManager;
import eu.craftok.gameapi.game.team.GameTeamManager;
import eu.craftok.gameapi.utils.BungeeCord;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Project game-api Created by Sithey
 */

public class GameAPI extends JavaPlugin {

    private static GameAPI instance;

    private CoreCommon common;
    private BungeeCord bungeeCord;

    private GameManager gameManager;
    private GamePlayerManager gamePlayerManager;
    private GameTeamManager gameTeamManager;

    @Override
    public void onEnable() {
        instance = this;

        this.common = CoreCommon.getCommon();
        this.bungeeCord = new BungeeCord();

        registerListeners();
        registerCommands();
    }

    public GameAPI registerGame(GameManager gameManager, GamePlayerManager gamePlayerManager, GameTeamManager gameTeamManager){

        this.gameManager = gameManager;
        if (gamePlayerManager == null)
            gamePlayerManager = new GamePlayerManager();
        this.gamePlayerManager = gamePlayerManager;
        if (gameTeamManager == null)
            gameTeamManager = new GameTeamManager();
        this.gameTeamManager = gameTeamManager;

        return this;
    }

    public static GameAPI getInstance() {
        return instance;
    }

    private void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerConnectionEvent(), this);
        pm.registerEvents(new PlayerDamageEvent(), this);
        pm.registerEvents(new PlayerDeathEvent(), this);
        pm.registerEvents(new PlayerInventoryEvent(), this);
        pm.registerEvents(new WorldCancelEvent(), this);
        pm.registerEvents(new MotdEvent(), this);
    }

    private void registerCommands(){
        getCommand("start").setExecutor(new StartCommand());
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("re").setExecutor(new ReCommand());
    }

    public BungeeCord getBungeeCord() {
        return bungeeCord;
    }

    public CoreCommon getCommon() {
        return common;
    }

    public GameManager getGameManagers() {
        return gameManager;
    }

    public GamePlayerManager getGamePlayerManager() {
        return gamePlayerManager;
    }


    public GameTeamManager getGameTeamManager() {
        return gameTeamManager;
    }


}
