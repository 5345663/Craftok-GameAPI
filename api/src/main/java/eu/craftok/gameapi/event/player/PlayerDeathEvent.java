package eu.craftok.gameapi.event.player;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.player.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Project game-api Created by Sithey
 */

public class PlayerDeathEvent implements Listener {

    @EventHandler
    public void onDeath(org.bukkit.event.entity.PlayerDeathEvent event){
        event.setDeathMessage(null);
        event.setKeepInventory(true);
        GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getEntity());
        gp.onDeath(event.getEntity(), gp.getGame());
        if (event.getEntity().getKiller() != null){
            GamePlayer gk = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getEntity().getKiller());
            gk.onKill(event.getEntity(), event.getEntity().getKiller(), event, gk.getGame());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getEntity().spigot().respawn();
            }
        }.runTaskLater(GameAPI.getInstance(), 2);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getPlayer());
        gp.onRespawn(event.getPlayer(), event, gp.getGame());
    }
}
