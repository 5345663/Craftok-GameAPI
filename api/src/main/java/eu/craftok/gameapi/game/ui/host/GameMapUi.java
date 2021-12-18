package eu.craftok.gameapi.game.ui.host;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.Game;
import eu.craftok.gameapi.game.world.GameWorld;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.inventory.CustomInventory;
import eu.craftok.utils.inventory.item.ActionItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Project game-api Created by Sithey
 */

public class GameMapUi extends CustomInventory {
    public GameMapUi(Player p) {
        super(p, "Choisir une map", GameAPI.getInstance().getGameManagers().getMaps().size() / 9 + 2, 1);
    }

    @Override
    public void setupMenu() {
        remplirLignesInventory(new ItemCreator(Material.STAINED_GLASS_PANE).setDurability((short) 0).getItemstack(), 1);
        addReturnItem(0, new GameHostUi(player));
        addActionItem(new ActionItem(0, new ItemCreator(Material.ARROW).setName(ChatColor.DARK_GREEN + "Revenir en arrière").getItemstack()) {
                          @Override
                          public void onClick(InventoryClickEvent inventoryClickEvent) {
                              GameAPI.getInstance().getGameManagers().openHostInventory(player);
                          }
                      });
                addCloseItem(8);
        int i = 9;
        Game game = GameAPI.getInstance().getGamePlayerManager().getGPlayer(player).getGame();
        for (GameWorld world : GameAPI.getInstance().getGameManagers().getGameHost().getWorlds()){
            addActionItem(new ActionItem(i, new ItemCreator(game.getGameWorld() == world ? Material.GRASS: Material.DIRT).setName((game.getGameWorld() == world ? ChatColor.GREEN : ChatColor.RED) + world.getName()).getItemstack()) {
                              @Override
                              public void onClick(InventoryClickEvent inventoryClickEvent) {
                                  if (game.getGameWorld() == world){
                                      return;
                                  }
                                  game.setGameWorld(world);
                                  GameAPI.getInstance().getGameManagers().openHostInventory(player);
                              }
                          });
                    i++;
        }
    }
}
