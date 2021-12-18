package eu.craftok.gameapi.game.player;

import eu.craftok.core.common.user.User;
import eu.craftok.gameapi.game.Game;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Project game-api Created by Sithey
 */

public class GamePlayerManager {

    private List<GamePlayer> gPlayers;

    public GamePlayerManager(){
        this.gPlayers = new ArrayList<>();
    }

    /**
     * @return all GPlayers in the server
     */

    public List<GamePlayer> getAllGPlayers() {
        return gPlayers;
    }

    /**
     * @return all Playings GPlayers in the server
     */

    public List<GamePlayer> getPlayingGPlayers() {
        List<GamePlayer> values = new ArrayList<>();
        for (GamePlayer gp : getAllGPlayers()){
            if (gp.isPlaying())
                values.add(gp);
        }
        return values;
    }

    /**
     * @return all Spectating GPlayers in the server
     */

    public List<GamePlayer> getSpectatingGPlayers() {
        List<GamePlayer> values = new ArrayList<>();
        for (GamePlayer gp : getAllGPlayers()){
            if (!gp.isPlaying())
                values.add(gp);
        }
        return values;
    }

    /**
     * @param game is a running Game
     * @return all GPlayers in the game
     */

    public List<GamePlayer> getAllGPlayersPerGame(Game game) {
        return game.getGPlayers();
    }

    /**
     * @param game is a running Game
     * @return all Playings GPlayers in the game
     */

    public List<GamePlayer> getPlayingGPlayersPerGame(Game game) {
        return game.getPlayingGPlayers();
    }

    /**
     * @param game is a running Game
     * @return all Spectating GPlayers in the game
     */

    public List<GamePlayer> getSpectatingGPlayersPerGame(Game game) {
        return game.getSpectatingGPlayers();
    }

    public void refreshVisibility(Game game, Player player){
        getAllGPlayers().forEach(gp -> {
            if (gp.isVanished()){
                player.hidePlayer(gp.getPlayer());
            }
            if (gp.getGame() != game){
                player.hidePlayer(gp.getPlayer());
                gp.getPlayer().hidePlayer(player);
            }else{
                player.showPlayer(gp.getPlayer());
                if (!getGPlayer(player).isVanished())
                gp.getPlayer().showPlayer(player);
            }
        });
    }

    /**
     * @param player Used to look a GPlayer by Player
     * @return the GPlayer of this player
     */

    public GamePlayer getGPlayer(Player player) {
        return getGPlayer(player.getUniqueId());
    }

    /**
     * @param uuid Used to look a GPlayer by UUID
     * @return the GPlayer of this player
     */

    public GamePlayer getGPlayer(UUID uuid) {
        for (GamePlayer gp : getAllGPlayers()){
            if (gp.getUniqueId().compareTo(uuid) == 0){
                return gp;
            }
        }
        return null;
    }

    /**
     * @param user Used to look a GPlayer by User
     * @return the GPlayer of this player
     */

    public GamePlayer getGPlayer(User user) {
        return getGPlayer(user.getUniqueId());
    }

}
