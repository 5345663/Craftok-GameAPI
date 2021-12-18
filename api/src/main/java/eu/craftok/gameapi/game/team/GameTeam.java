package eu.craftok.gameapi.game.team;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.Game;
import eu.craftok.gameapi.game.player.GamePlayer;
import eu.craftok.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project game-api Created by Sithey
 */

public class GameTeam {

    private String name;
    private String prefix;
    private DyeColor dyeColor;
    private Pattern[] patterns;

    public GameTeam(){
        this.name = null;
        this.prefix = null;
        this.dyeColor = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setDyeColor(DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public void setPatterns(Pattern[] patterns) {
        this.patterns = patterns;
    }

    public ItemStack getItemStack(Game game) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        for (int i = 0; i < GameAPI.getInstance().getGameManagers().getSettings().getSizeTeams(); i++){
            lore.add("§b➤ " + (getGPlayers(game).size() < i + 1 ? "" : getGPlayers(game).get(i).getPlayer().getName()));
        }
        lore.add("");
        ItemCreator item = new ItemCreator(Material.BANNER);
        item.setBasecolor(getDyeColor()).setName(getName()).setAmount(getGPlayers(game).size()).setLores(lore);
        BannerMeta bannerMeta = item.getBannerMeta();
        List<Pattern> p = new ArrayList<>();
        Arrays.asList(patterns).forEach((pattern) -> {
            p.add(new Pattern(pattern.getColor() == DyeColor.BLACK ? getDyeColor() : pattern.getColor(), pattern.getPattern()));
        });
        bannerMeta.setPatterns(p);
        item.setBannerMeta(bannerMeta);
        return item.getItemstack();
    }

    public void sendMessage(Game game, String message){
        getGPlayers(game).forEach(gp -> {
           gp.getPlayer().sendMessage(message);
        });
    }

    public void createTeam(){
        GameAPI.getInstance().getGameTeamManager().getgTeams().add(this);
        org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if (scoreboard.getTeam(name) == null){
            Team team = scoreboard.registerNewTeam(name);
            team.setPrefix(prefix);
            team.setAllowFriendlyFire(false);
        }
    }

    public void deleteTeam(){
        GameAPI.getInstance().getGameTeamManager().getgTeams().remove(this);
        org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if (scoreboard.getTeam(name) != null){
            scoreboard.getTeam(name).unregister();
        }
    }

    public List<GamePlayer> getGPlayers(Game game) {
        List<GamePlayer> value = new ArrayList<>();
        for (GamePlayer gp : game.getGPlayers()){
            if (gp.getTeam() == this){
                value.add(gp);
            }
        }
        return value;
    }

    public List<GamePlayer> getAliveGPlayers(Game game){
        List<GamePlayer> value = new ArrayList<>();
        for (GamePlayer gp : getGPlayers(game)){
            if (gp.isPlaying()){
                value.add(gp);
            }
        }
        return value;
    }

}
