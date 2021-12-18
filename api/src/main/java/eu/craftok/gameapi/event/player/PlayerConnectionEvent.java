package eu.craftok.gameapi.event.player;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.player.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Project game-api Created by Sithey
 */

public class PlayerConnectionEvent implements Listener {

    @EventHandler (priority = EventPriority.HIGH)
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event){
        event.setJoinMessage(null);

        GameAPI.getInstance().getGameManagers().newGPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);

        GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getPlayer());
        gp.onQuit(event.getPlayer(), gp.getGame());
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        if (GameAPI.getInstance().getGameManagers().getJoinableGame() == null){
            event.setResult(PlayerLoginEvent.Result.KICK_FULL);
            event.setKickMessage("Le serveur est complet");
        }
    }

}
