package eu.craftok.gameapi.game.ui;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.Game;
import eu.craftok.gameapi.game.team.GameTeam;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.inventory.CustomInventory;
import eu.craftok.utils.inventory.item.ActionItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Project game-api Created by Sithey
 */

public class GameTeamUi extends CustomInventory {

    private Game game;

    public GameTeamUi(Player p, Game game) {
        super(p, "Choix de l'equipe", 6, (GameAPI.getInstance().getGameTeamManager().getgTeams().size() / 28) + 1);
        this.game = game;
    }

    @Override
    public void setupMenu() {
        remplirLignesInventory(new ItemCreator(Material.STAINED_GLASS_PANE).setDurability((short) 0).getItemstack(), 1);
        int slots = 9;
        int categories = 1;
        addActionItem(new ActionItem(0, new ItemCreator(Material.PAPER).setName("Team aleatoire").getItemstack()) {
            @Override
            public void onClick(InventoryClickEvent inventoryClickEvent) {
                GameAPI.getInstance().getGamePlayerManager().getGPlayer(player).setTeam(player, null);
                openMenuAll();
            }
        });
        addPageItem(4);
        addCloseItem(8);
        for (GameTeam team : GameAPI.getInstance().getGameTeamManager().getgTeams()){
            addActionItem(new ActionItem(categories, slots, team.getItemStack(game)) {
                @Override
                public void onClick(InventoryClickEvent inventoryClickEvent) {
                    GameAPI.getInstance().getGamePlayerManager().getGPlayer(player).setTeam(player, team);
                    openMenuAll();
                }
            });
            if (slots == 53){
                slots = 8;
                categories++;
            }
            slots++;
        }
    }
}
