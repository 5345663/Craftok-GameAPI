package eu.craftok.gameapi.game;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.host.GameHost;
import eu.craftok.gameapi.game.player.GamePlayer;
import eu.craftok.gameapi.game.scoreboard.GameScoreboard;
import eu.craftok.gameapi.game.settings.GameSettings;
import eu.craftok.gameapi.game.team.GameTeam;
import eu.craftok.gameapi.game.ui.GameTeamUi;
import eu.craftok.gameapi.game.ui.host.GameHostUi;
import eu.craftok.gameapi.game.world.GameWorld;
import eu.craftok.utils.CConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Project game-api Created by Sithey
 */

public class GameManager {

    private String name, task;
    private GameScoreboard gameScoreboard;
    private GameSettings gameSettings;
    private Plugin gameInstance;
    private GameHost gameHost;
    private List<Game> games;
    private List<CConfig> maps;
    private int amountofgame;

    public GameManager(String name, String task, GameSettings gameSettings, GameScoreboard gameScoreboard, Plugin gameInstance) {
        this.name = name;
        this.task = task;
        this.gameSettings = gameSettings;
        (this.gameScoreboard = gameScoreboard).uptadeAllTime();
        this.gameInstance = gameInstance;
        this.gameHost = new GameHost();
        this.games = new ArrayList<>();
        this.maps = new ArrayList<>();
        this.amountofgame = 0;
    }

    public GameScoreboard getGameScoreboard() {
        return gameScoreboard;
    }

    public GameHost getGameHost() {
        return gameHost;
    }

    /**
     * @return the name of the current game
     */

    public String getName() {
        return name;
    }

    /**
     *
     * @return the task name of the current game
     */

    public String getTask() {
        return task;
    }

    /**
     * When the server is starting
     */

    public void setupServer() {
        System.out.println("Setup du " + getName());

        if (getSettings().isHost()){
            getSettings().setStartTimer(10);
            getSettings().setGames(1);
        }

        if (Files.notExists(Paths.get(gameInstance.getDataFolder() +"/worlds"))){
            try{
                Files.createDirectories(Paths.get(gameInstance.getDataFolder() +"/worlds"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File folder = new File(gameInstance.getDataFolder() +"/worlds");
        List<File> list = new ArrayList<>(Arrays.asList(folder.listFiles()));
        if (list.isEmpty()){
            System.out.println("Aucune map dispnonible, le serveur vas redemarer");
            Bukkit.getServer().shutdown();
        }else{
            list.forEach(maps -> this.maps.add(new CConfig("worlds/" + maps.getName(), gameInstance)));
        }

        System.out.println(this.maps.size() + " Maps Ajouter");

        createGames();

        GameAPI.getInstance().getGameTeamManager().createTeams();
    }

    /**
     * Function to create games
     */

    public void createGames(){
        for (int i = 0; i < getSettings().getGames(); i++) {
            createGame();
        }
    }

    /**
     * When the server is stoping
     */

    public void stopServer(){
        System.out.println("Stop du " + getName());
        Bukkit.getServer().shutdown();
    }

    /**
     * When the server need to create a new Game
     */

    public void createGame() {
        Game game = newGame();
        if (getSettings().isHost()){
            maps.forEach(map -> getGameHost().getWorlds().add(newGameWorld(game, map)));
            game.setGameWorld(getGameHost().getWorlds().get(0));
        }else {
            game.setGameWorld(newGameWorld(game, getRandomMap()));
        }
        amountofgame++;
        System.out.println("Creation de la game #" + game.getUniqueId());
        getAllGames().add(game);
    }

    /**
     * When a game is stopping
     */

    public void deleteGame(Game game) {
        System.out.println("Suppression de la game #" + game.getUniqueId());
        getAllGames().remove(game);
        if (getAllGames().size() == 0){
            stopServer();
        }
    }

    /**
     * @return the amount of game started
     */

    public int getAmountofgame() {
        return amountofgame;
    }

    /**
     * @return a ready game
     */

    public Game getJoinableGame(){
        for (Game game : getAllGames()){
            if (game.getGameStatus().isLobby() && game.getPlayingGPlayers().size() < getSettings().getMaxPlayers()){
                return game;
            }
        }
        return null;
    }

    /**
     * @return Every settings to use for a game
     */

    public GameSettings getSettings() {
        return gameSettings;
    }

    /**
     * @return the GameScoreboard
     */

    public GameScoreboard getScoreboard() {
        return gameScoreboard;
    }

    /**
     * @return all available running game
     */

    public List<Game> getAllGames() {
        return games;
    }

    /**
     * @param world Bukkit world
     * @return The Game with this world
     */

    public Game getGameByWorld(World world){
        for (Game game : getAllGames()){
            if (game.getGameWorld().getWorld() == world){
                return game;
            }
        }
        return null;
    }

    public void openStatsInventory(Player player){

    }

    public void openTeamsInventory(Player player){
        new GameTeamUi(player, GameAPI.getInstance().getGamePlayerManager().getGPlayer(player).getGame()).openMenu();
    }

    public void openHostInventory(Player player){
        new GameHostUi(player).openMenu();
    }

    public List<CConfig> getMaps() {
        return maps;
    }

    private CConfig getRandomMap(){
        return maps.get(new Random().nextInt(maps.size()));
    }

    public Game newGame(){
        return new Game();
    }

    public GameTeam newGTeam(){
        return new GameTeam();
    }

    public GameWorld newGameWorld(Game game, CConfig config){
        return new GameWorld(game, config);
    }

    public GamePlayer newGPlayer(Player player){
        return new GamePlayer(player.getUniqueId());
    }
}
