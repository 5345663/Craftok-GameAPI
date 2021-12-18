package eu.craftok.gameapi.game;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.player.GamePlayer;
import eu.craftok.gameapi.game.status.EGameStatus;
import eu.craftok.gameapi.game.status.GameStatus;
import eu.craftok.gameapi.game.task.FinishGameTask;
import eu.craftok.gameapi.game.task.StartingGameTask;
import eu.craftok.gameapi.game.team.GameTeam;
import eu.craftok.gameapi.game.world.GameWorld;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.PlayerUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Project game-api Created by Sithey
 */

public class Game {
    private final int uniqueId;
    private int timer;
    private GameWorld gameWorld;
    private GameStatus gameStatus;
    private final List<GamePlayer> gPlayers;
    private final List<GamePlayer> gWinners;

    public Game(){
        this.uniqueId = GameAPI.getInstance().getGameManagers().getAmountofgame() + 1;
        this.timer = GameAPI.getInstance().getGameManagers().getSettings().startTimer();
        this.gameWorld = null;
        this.gameStatus = null;
        this.gPlayers = new ArrayList<>();
        this.gWinners = new ArrayList<>();
    }

    /**
     * The uniqueId used to find the game
     *
     * @return the game uniqueId
     */

    public int getUniqueId() {
        return uniqueId;
    }

    /**
     * The function when the server need to start the Starting Task
     */

    public void startTimer() {
        new StartingGameTask(this).runTaskTimer(GameAPI.getInstance(), 0, 20);
    }

    /**
     * The function when the server need to start the Finish Task
     */

    public void stopTimer(){
        new FinishGameTask(this).runTaskTimer(GameAPI.getInstance(), 0, 20);
    }

    /**
     * When the game is Starting
     */

    public void onStart() {
        setGameStatus(EGameStatus.GAME.getGameStatus());
        getGPlayers().forEach(gp -> {
           gp.getPlayer().closeInventory();
           gp.getPlayer().getInventory().clear();
           if (!gp.isPlaying()){
               gp.getPlayer().setGameMode(GameMode.SPECTATOR);
           }
        });
        if (GameAPI.getInstance().getGameManagers().getSettings().getSizeTeams() > 1){
            GameAPI.getInstance().getGameTeamManager().fillAllTeams(this);
        }
    }

    /**
     * When the game is Finish
     *
     * @param winners the list of winners
     */

    public void onWin(List<GamePlayer> winners) {
        this.gWinners.addAll(winners);
        setGameStatus(EGameStatus.FINISH.getGameStatus());
        this.gWinners.forEach(gp -> {
            gp.getPlayer().setGameMode(GameMode.CREATIVE);
            PlayerUtils utils = new PlayerUtils(gp.getPlayer());
            gp.getPlayer().getInventory().setItem(4, new ItemCreator(Material.NETHER_STAR).setName("§a[ ➼ Rejouer]").getItemstack());
            gp.getPlayer().sendMessage("");
            gp.getPlayer().sendMessage("§7§m                              §a§l VICTOIRE §7§m                              ");
            gp.getPlayer().sendMessage(" ");
            gp.getPlayer().sendMessage("    §fVous avez §a§lGAGNER §r§fmais ce n'est pas fini !");
            gp.getPlayer().sendMessage("  §fEntraîne toi en rejouant de deviens plus fort.");
            gp.getPlayer().sendMessage(" ");
            TextComponent componentRePlay = new TextComponent("    §a[ ➼ Rejouer]");
            gp.getPlayer().sendMessage(" ");
            componentRePlay.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/re"));
            componentRePlay.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lREJOUER UNE PARTIE\n§fContinuer à jouer en " + GameAPI.getInstance().getGameManagers().getName() +"\n§fdans une autre partie.\n\n§6§l» §eClique: §fRejouer").create()));
            gp.getPlayer().spigot().sendMessage(componentRePlay);
            utils.sendTitle(5, 100, 5, "§c§LVICTOIRE", "§rVous venez de gagner la partie");
        });
        setTimer(GameAPI.getInstance().getGameManagers().getSettings().stopTimer());
        stopTimer();
    }

    /**
     * The function when the game is stopped
     */

    public void onStop(){
        getGPlayers().forEach(gp -> GameAPI.getInstance().getBungeeCord().sendToLobby(gp.getPlayer()));
        GameAPI.getInstance().getGameManagers().deleteGame(this);
    }

    /**
     * @return The list of online players
     */

    public List<GamePlayer> getGPlayers() {
        return gPlayers;
    }

    /**
     * @return The list of online playings players
     */

    public List<GamePlayer> getPlayingGPlayers(){
        List<GamePlayer> values = new ArrayList<>();
        for (GamePlayer gp : getGPlayers()){
            if (gp.isPlaying())
                values.add(gp);
        }
        return values;
    }

    /**
     * @return The list of online spectating players
     */

    public List<GamePlayer> getSpectatingGPlayers(){
        List<GamePlayer> values = new ArrayList<>();
        for (GamePlayer gp : getGPlayers()){
            if (!gp.isPlaying())
                values.add(gp);
        }
        return values;
    }

    public List<GameTeam> getGTeams(){
        List<GameTeam> values = new ArrayList<>();
        for (GamePlayer gp : getPlayingGPlayers()){
            if (gp.getTeam() != null && !values.contains(gp.getTeam())){
                values.add(gp.getTeam());
            }
        }
        return values;
    }

    /**
     * @return the list of winners
     */
    public List<GamePlayer> getgWinners() {
        return gWinners;
    }

    /**
     * Send a message to every players in the game
     * @param message the message
     */

    public void broadcastMessage(String message){
        getGPlayers().forEach(gp -> gp.getPlayer().sendMessage(message));
    }

    /**
     * @return the Status of the current game
     */

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Set the status of the current game
     * @param gameStatus next status
     */

    public void setGameStatus(GameStatus gameStatus){
        this.gameStatus = gameStatus;
    }

    /**
     * @return the time of the current game
     */

    public int getTimer() {
        return timer;
    }

    public void addTimer(){
        timer++;
    }

    public void remTimer() { timer--; }

    public void setTimer(int timer){
        this.timer = timer;
    }

    /**
     * @return the GameWorld
     */

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    /**
     * Set a new GameWorld
     * @param gameWorld the next gameworld
     */

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        getGPlayers().forEach(gp -> gp.getPlayer().teleport(gameWorld.getWaiting()));
    }

    /**
     * @return the PlayerUtils of all players of this game
     */

    public PlayerUtils getPlayerUtils(){
        List<Player> players = new ArrayList<>();
        for (GamePlayer gp : getGPlayers()){
            players.add(gp.getPlayer());
        }
        return new PlayerUtils(players);
    }
}
