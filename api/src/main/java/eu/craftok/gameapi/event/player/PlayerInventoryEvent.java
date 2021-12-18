package eu.craftok.gameapi.event.player;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.Game;
import eu.craftok.gameapi.game.player.GamePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

/**
 * Project game-api Created by Sithey
 */

public class PlayerInventoryEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        ItemStack item = event.getItem();
        if (event.getAction() != Action.PHYSICAL && item != null) {
            if (item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null){
                GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getPlayer());
                gp.onInteract(event.getPlayer(), event.getItem(), event, gp.getGame());
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null){
            if (item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null) {
                GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer((Player) event.getWhoClicked());
                gp.onInventoryClick((Player) event.getWhoClicked(), item, event, gp.getGame());
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){
        GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getPlayer());
        gp.onConsume(event.getPlayer(), event.getItem(), event, gp.getGame());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getPlayer());
        gp.onDrop(event.getPlayer(), event, gp.getGame());
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event){
        GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getPlayer());
        gp.onPickup(event.getPlayer(), event, gp.getGame());
    }

    @EventHandler
    public void onChat(PlayerChatEvent event){
        event.setCancelled(true);
        GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getPlayer());
        gp.onChat(event.getPlayer(), event, gp.getGame());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getPlayer());
        gp.onBlockPlace(event.getPlayer(), event, gp.getGame());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getPlayer());
        gp.onBlockBreak(event.getPlayer(), event, gp.getGame());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer(event.getPlayer());
        if (gp.isVanished()){
            Game game = GameAPI.getInstance().getGameManagers().getGameByWorld(event.getTo().getWorld());
            if (gp.getGame() != game){
                gp.setGame(game);
                GameAPI.getInstance().getGamePlayerManager().refreshVisibility(game, event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event){
        if (event.getEntityType().equals(EntityType.PLAYER)) {
            GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer((Player) event.getEntity());
            gp.onFoodLevelChange(gp.getPlayer(), event, gp.getGame());
        }
    }
}
