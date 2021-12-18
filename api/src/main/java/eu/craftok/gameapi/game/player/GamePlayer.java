package eu.craftok.gameapi.game.player;

import eu.craftok.core.common.CoreCommon;
import eu.craftok.core.common.user.User;
import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.Game;
import eu.craftok.gameapi.game.team.GameTeam;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.PlayerUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

/**
 * Project game-api Created by Sithey
 */

public class GamePlayer {

    private final UUID uuid;

    private boolean playing;
    private Game game;
    private GameTeam team;
    public GamePlayer(UUID uuid){
        this.uuid = uuid;
        this.playing = false;
        this.team = null;
        onJoin(getPlayer(), GameAPI.getInstance().getGameManagers().getJoinableGame());
    }

    /**
     * @return the current GPlayer uniqueID
     */

    public UUID getUniqueId() {
        return uuid;
    }

    /**
     * @return the current GPlayer Player
     */

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * @return the current GPlayer OfflinePlayer
     */

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    /**
     * @return the current GPlayer User
     */

    public User getUser() {
        return CoreCommon.getCommon().getUserManager().getUserByUniqueId(uuid);
    }

    /**
     * @return if the GPlayer is Playing
     */

    public boolean isPlaying() {
        return playing;
    }

    /**
     * @param value is the Playing statut of the current GPlayer
     */

    public void setPlaying(boolean value) {
        playing = value;
    }

    /**
     * @return the current GPlayer Game
     */

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getDisplayName() {
        return (getTeam() == null ? getUser().getDisplayName() : getTeam().getPrefix() + getPlayer().getName());
    }


    /**
     * @param team is the Team of the current GPlayer
     */

    public void setTeam(Player player, GameTeam team) {
        GameTeam latestTeam = this.team;
        if (team == null){
            if (latestTeam != null) {
                Team t = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(latestTeam.getName());
                if (t != null) {
                    Bukkit.getScoreboardManager().getMainScoreboard().getTeam(latestTeam.getName()).removeEntry(player.getName());
                }
                if (game.getGameStatus().isLobby())
                latestTeam.sendMessage(game, "§c§lCRAFTOK §8§l» §7Le joueur §c" + getDisplayName() + " §7vient de quitter l'equipe " + latestTeam.getName());
                this.team = null;
            }
            return;
        }
        if (team == this.team){
            return;
        }
        if (team.getAliveGPlayers(game).size() >= GameAPI.getInstance().getGameManagers().getSettings().getSizeTeams()) {
            getPlayer().sendMessage("§c§lCRAFTOK §8§l» §4L'equipe " + team.getName() + " §4est complete");
            return;
        }

        this.team = team;
        if (latestTeam != null) {
            Team t = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(latestTeam.getName());
            if (t != null) {
                Bukkit.getScoreboardManager().getMainScoreboard().getTeam(latestTeam.getName()).removeEntry(player.getName());
            }
            if (game.getGameStatus().isLobby())
                latestTeam.sendMessage(game, "§c§lCRAFTOK §8§l» §7Le joueur §c" + getDisplayName() + " §7vient de quitter l'equipe " + latestTeam.getName());
        }
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(team.getName()).addEntry(player.getName());
        if (game.getGameStatus().isLobby())
            this.team.sendMessage(game, "§c§lCRAFTOK §8§l» §7Le joueur §a" + getDisplayName() + " §7vient de rejoindre l'equipe " + this.team.getName());
    }

    /**
     * @return the current GPlayer team
     */

    public GameTeam getTeam() {
        return team;
    }

    public boolean isVanished() {
        for (MetadataValue meta : getPlayer().getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    public void onJoin(Player player, Game game) {
        player.setMaxHealth(20.0D);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setSaturation(12.8F);
        player.setMaximumNoDamageTicks(20);
        player.setFireTicks(0);
        player.setFallDistance(0.0F);
        player.setLevel(0);
        player.setExp(0.0F);
        player.setWalkSpeed(0.2F);
        player.getInventory().setHeldItemSlot(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.closeInventory();
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
        player.updateInventory();

        player.setPlayerListName(player.getName());

        GameAPI.getInstance().getGameManagers().getScoreboard().join(this);
        GameAPI.getInstance().getGamePlayerManager().getAllGPlayers().add(this);

        if (game == null){
            game = GameAPI.getInstance().getGameManagers().getAllGames().get(0);
        }

        setGame(game);

        game.getGPlayers().add(this);

        GameAPI.getInstance().getGamePlayerManager().refreshVisibility(game, player);

        if (game.getGameStatus().isLobby()){
            setPlaying(!isVanished());
            if (isPlaying()) {
                player.setGameMode(GameMode.ADVENTURE);

                player.getInventory().setItem(8, new ItemCreator(Material.BED).setName("§c§lQuitter").getItemstack());

                if (GameAPI.getInstance().getGameManagers().getSettings().getSizeTeams() != 1) {
                    player.getInventory().setItem(1, new ItemCreator(Material.BANNER).setName("§e§lChoix de l'equipe").getItemstack());
                    player.getInventory().setHeldItemSlot(1);
                }

                if (!GameAPI.getInstance().getGameManagers().getSettings().isHost()) {
                    player.getInventory().setItem(0, new ItemCreator(Material.NETHER_STAR).setName("§e§lStats").getItemstack());
                    if (game.getPlayingGPlayers().size() >= GameAPI.getInstance().getGameManagers().getSettings().getMinPlayers()) {

                        if (game.getTimer() == GameAPI.getInstance().getGameManagers().getSettings().startTimer()) {
                            game.remTimer();
                            game.startTimer();
                        }

                        if (game.getTimer() == GameAPI.getInstance().getGameManagers().getSettings().getMaxPlayers()) {
                            game.setTimer(3);
                        }
                    }

                }else{
                    if (GameAPI.getInstance().getGameManagers().getGameHost().getHost() == null || GameAPI.getInstance().getGameManagers().getGameHost().getHost().getUniqueId().compareTo(player.getUniqueId()) == 0){
                        player.getInventory().setItem(0, new ItemCreator(Material.COMMAND).setName("§e§lConfiguration de l'host").getItemstack());
                        GameAPI.getInstance().getGameManagers().getGameHost().setHost(this);
                        player.getInventory().setHeldItemSlot(0);
                    }
                }
            }

            new BukkitRunnable(){
                @Override
                public void run() {
                    player.teleport(getGame().getGameWorld().getWaiting());
                }
            }.runTaskLater(GameAPI.getInstance(), 2);

        }else{
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(getGame().getGameWorld().getMiddle());
        }

        if (isPlaying()){
            game.broadcastMessage("§c§lCRAFTOK §8§l≫ §r" + getDisplayName() + " §7a rejoint la partie §a(" + game.getPlayingGPlayers().size() + "/" + GameAPI.getInstance().getGameManagers().getSettings().getMaxPlayers() + ")");
            new PlayerUtils(player).sendTitle(5, 70, 5, "§b[ §f§l" + GameAPI.getInstance().getGameManagers().getName() +" §c]", "");
        }

    }

    public void onDeath(Player player, Game game) {
        if (!GameAPI.getInstance().getGameManagers().getSettings().isKeep_inventory()){
            Location location = player.getLocation();
            for (int slot = 0; slot < 39; slot++) {
                ItemStack item = getPlayer().getInventory().getItem(slot);
                if (item != null) {
                    location.getWorld().dropItem(location, item).setPickupDelay(0);
                }
            }
        }
    }

    public void onRespawn(Player player, PlayerRespawnEvent event, Game game){
        event.setRespawnLocation(game.getGameWorld().getMiddle());
    }

    public void onQuit(Player player, Game game) {
        GameAPI.getInstance().getGameManagers().getScoreboard().leave(this);
        GameAPI.getInstance().getGamePlayerManager().getAllGPlayers().remove(this);
        game.getGPlayers().remove(this);

        if (game.getGameStatus().isGame()){
            onLost(player, game);
        }else{
            game.broadcastMessage("§c§lCRAFTOK §8§l≫ §r" + getDisplayName() + " §7a quitte la partie §a(" + getGame().getPlayingGPlayers().size() + "/" + GameAPI.getInstance().getGameManagers().getSettings().getMaxPlayers() + ")");
        }

        setTeam(player,null);
    }

    public void onKill(Player player, Player killer, PlayerDeathEvent event, Game game) {

    }

    public void onLost(Player player, Game game){
        setPlaying(false);
        if (getTeam() != null){
            setTeam(player, null);
        }
        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.getInventory().setItem(4, new ItemCreator(Material.NETHER_STAR).setName("§a[ ➼ Rejouer]").getItemstack());
        player.sendMessage("");
        player.sendMessage("§7§m                              §c§l SPECTATEUR §7§m                              ");
        player.sendMessage(" ");
        player.sendMessage("    §fVous avez §c§lPERDU §r§fmais n'abandonnez pas !");
        player.sendMessage("  §fEntraîne toi en rejouant de deviens plus fort.");
        player.sendMessage(" ");
        TextComponent componentRePlay = new TextComponent("    §a[ ➼ Rejouer]");
        player.sendMessage(" ");
        componentRePlay.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/re"));
        componentRePlay.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lREJOUER UNE PARTIE\n§fContinuer à jouer en " + GameAPI.getInstance().getGameManagers().getName() +"\n§fdans une autre partie.\n\n§6§l» §eClique: §fRejouer").create()));
        player.spigot().sendMessage(componentRePlay);

        if (isWin(game)){
            game.onWin(game.getPlayingGPlayers());
        }
    }

    public boolean isWin(Game game){
        if (!game.getGameStatus().isGame()){
            return false;
        }
        int team_size = GameAPI.getInstance().getGameManagers().getSettings().getSizeTeams();
        return (team_size == 1 && game.getPlayingGPlayers().size() == 1) || (team_size != 1 && game.getGTeams().size() == 1);
    }

    public void onProjectilePlayerHit(Player player, Player target, Projectile projectile, EntityDamageByEntityEvent event, Game game) {

    }

    public void onInteract(Player player, ItemStack item, PlayerInteractEvent event, Game game) {
        if (game.getGameStatus().isLobby()){

            if (item.getType().equals(Material.BED))
                GameAPI.getInstance().getBungeeCord().sendToLobby(player);

            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§e§lChoix de l'equipe"))
                GameAPI.getInstance().getGameManagers().openTeamsInventory(player);

            if (item.getType().equals(Material.NETHER_STAR))
                GameAPI.getInstance().getGameManagers().openStatsInventory(player);

            if (item.getType().equals(Material.COMMAND))
                GameAPI.getInstance().getGameManagers().openHostInventory(player);

        }else{

            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§a[ ➼ Rejouer]"))
                player.performCommand("re");

        }
    }

    public void onInventoryClick(Player player, ItemStack item, InventoryClickEvent event, Game game){
        if (game.getGameStatus().isLobby()){

            if (item.getType().equals(Material.BED))
                GameAPI.getInstance().getBungeeCord().sendToLobby(player);

            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§e§lChoix de l'equipe"))
                GameAPI.getInstance().getGameManagers().openTeamsInventory(player);

            if (item.getType().equals(Material.NETHER_STAR))
                GameAPI.getInstance().getGameManagers().openStatsInventory(player);

            if (item.getType().equals(Material.COMMAND))
                GameAPI.getInstance().getGameManagers().openHostInventory(player);

        }else{

            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§a[ ➼ Rejouer]"))
                player.performCommand("re");

        }
    }

    public void onConsume(Player player, ItemStack item, PlayerItemConsumeEvent e, Game game) {

    }

    public void onDamageByPlayer(Player player, Player attacker, EntityDamageByEntityEvent event, Game game) {

    }

    public void onDamage(Player player, EntityDamageEvent event, Game game) {
        if (!game.getGameStatus().isGame() || !isPlaying()){
            event.setCancelled(true);
        }
    }

    public void onBlockPlace(Player player, BlockPlaceEvent event, Game game) {
        if (!game.getGameStatus().isGame() || !isPlaying()){
            event.setCancelled(true);
        }
    }

    public void onBlockBreak(Player player, BlockBreakEvent event, Game game) {
        if (!game.getGameStatus().isGame() || !isPlaying()){
            event.setCancelled(true);
        }
    }

    public void onDrop(Player player, PlayerDropItemEvent event, Game game) {
        if (!game.getGameStatus().isGame() || !isPlaying()){
            event.setCancelled(true);
        }
    }

    public void onPickup(Player player, PlayerPickupItemEvent event, Game game) {
        if (!game.getGameStatus().isGame() || !isPlaying()){
            event.setCancelled(true);
        }
    }

    public void onFoodLevelChange(Player player, FoodLevelChangeEvent event, Game game){
        if (!game.getGameStatus().isGame() || !isPlaying()){
            event.setCancelled(true);
            event.setFoodLevel(20);
        }
    }

    public void onChat(Player player, PlayerChatEvent event, Game game) {
        if (isPlaying() || !game.getGameStatus().isGame())
        game.broadcastMessage(getDisplayName() + "§f: " + event.getMessage());
        else game.getSpectatingGPlayers().forEach(gp -> gp.getPlayer().sendMessage("§7[SPEC] " + getDisplayName() + "§f: " + event.getMessage()));
    }

    public void addCoins(int coins, String reason){
        if (!GameAPI.getInstance().getGameManagers().getSettings().isHost()) {
            getPlayer().sendMessage("§c§lCRAFTOK §8§l» §eCoins §7+§6" + coins + " (§d"+ reason + "§6)");
            getUser().addCoins(coins);
        }
    }
}
