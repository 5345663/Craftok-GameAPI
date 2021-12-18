package eu.craftok.gameapi.event.player;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.player.GamePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Project game-api Created by Sithey
 */

public class PlayerDamageEvent implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity().getType().equals(EntityType.PLAYER)){
            GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer((Player) event.getEntity());
            gp.onDamage((Player) event.getEntity(), event, gp.getGame());
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event){
        if (event.getEntity().getType().equals(EntityType.PLAYER)){
            GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer((Player) event.getEntity());
            if (event.getDamager() instanceof Player) {
                gp.onDamageByPlayer(((Player) event.getEntity()), ((Player) event.getDamager()), event, gp.getGame());
            }else if (event.getDamager() instanceof Projectile){
                Projectile damager = (Projectile) event.getDamager();
                if (damager.getShooter() instanceof Player){
                    gp.onProjectilePlayerHit(((Player) damager.getShooter()), (Player) event.getEntity(), damager, event, gp.getGame());
                }
            }
        }
    }

}
