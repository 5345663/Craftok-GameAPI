package eu.craftok.gameapi.game.ui.host;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.Game;
import eu.craftok.gameapi.game.GameManager;
import eu.craftok.gameapi.game.host.GameHost;
import eu.craftok.gameapi.game.player.GamePlayer;
import eu.craftok.gameapi.game.player.GamePlayerManager;
import eu.craftok.gameapi.game.settings.GameSettings;
import eu.craftok.gameapi.game.ui.GameTeamUi;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.inventory.CustomInventory;
import eu.craftok.utils.inventory.item.ActionItem;
import eu.craftok.utils.inventory.item.StaticItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Project game-api Created by Sithey
 */

public class GameHostUi extends CustomInventory {

    private static boolean visibility = false;
    private static boolean announce = false;

    public GameHostUi(Player p) {
        super(p, " §D┃ §5Configuration", 6, 1);
    }

    @Override
    public void setupMenu() {

        GameAPI gameAPI = GameAPI.getInstance();
        GameManager gameManager = gameAPI.getGameManagers();
        GamePlayerManager gPlayerManager = gameAPI.getGamePlayerManager();
        GamePlayer gp = gPlayerManager.getGPlayer(player);
        Game game = gp.getGame();
        GameSettings settings = gameManager.getSettings();
        GameHost gameHost = gameManager.getGameHost();

        remplirLignesInventory(new ItemCreator(Material.STAINED_GLASS_PANE).setDurability((short) 0).getItemstack(), 1);
        remplirLignesInventory(new ItemCreator(Material.STAINED_GLASS_PANE).setDurability((short) 0).getItemstack(), 6);
        int[] blueglass = {0, 9, 18, 27, 36};
        for (int slot : blueglass){
            addItem(new StaticItem(slot, new ItemCreator(Material.STAINED_GLASS_PANE).setDurability((short) 3).getItemstack()));
        }
        int[] redglass = {8, 17, 26, 35, 44};
        for (int slot : redglass){
            addItem(new StaticItem(slot, new ItemCreator(Material.STAINED_GLASS_PANE).setDurability((short) 14).getItemstack()));
        }

        addCloseItem(0);
        addCloseItem(8);

        addActionItem(new ActionItem(20, new ItemCreator(Material.SKULL_ITEM).setName("§cNombre de joueurs: §6" + settings.getMaxPlayers())
                .addLore("")
                .addLore("§e§l» §7Clique droit pour ajouter 1")
                .addLore("§e§l» §7Clique gauche pour enlever 1")
                .addLore("")
                .setAmount(settings.getMaxPlayers()).getItemstack()) {
                          @Override
                          public void onClick(InventoryClickEvent inventoryClickEvent) {
                                if (inventoryClickEvent.isLeftClick()){
                                    if (settings.getMaxPlayers() - 1 > settings.getSizeTeams()){
                                        settings.setMaxPlayers(settings.getMaxPlayers() - 1);
                                        reloadTeams();
                                        player.sendMessage("§c§lCRAFTOK §8§l» §bVous avez mis le nombres de joueurs total a §f" + settings.getMaxPlayers());
                                    }
                                }else if (inventoryClickEvent.isRightClick()){
                                    if (settings.getMaxPlayers() != settings.getHost_maxPlayers()){
                                        settings.setMaxPlayers(settings.getMaxPlayers() + 1);
                                        player.sendMessage("§c§lCRAFTOK §8§l» §bVous avez mis le nombres de joueurs total a §f" + settings.getMaxPlayers());
                                        reloadTeams();
                                    }
                                }
                                openMenu();
                          }
                      });

        addActionItem(new ActionItem(21, new ItemCreator(Material.BANNER).setName("§cNombre de joueurs par equipes: §6" + settings.getSizeTeams())
                .addLore("")
                .addLore("§e§l» §7Clique droit pour ajouter 1")
                .addLore("§e§l» §7Clique gauche pour enlever 1")
                .addLore("")
                .setAmount(settings.getSizeTeams()).getItemstack()) {
                          @Override
                          public void onClick(InventoryClickEvent inventoryClickEvent) {
                              if (inventoryClickEvent.isLeftClick()){
                                  if (settings.getSizeTeams() - 1 >= 1){
                                      settings.setSizeTeams(settings.getSizeTeams() - 1);
                                      player.sendMessage("§c§lCRAFTOK §8§l» §bVous avez mis le nombres de joueurs par equpes a §f" + settings.getSizeTeams());
                                      reloadTeams();
                                  }
                              }else if (inventoryClickEvent.isRightClick()){
                                  if (settings.getSizeTeams() + 1 < settings.getMaxPlayers() && settings.getSizeTeams() + 1 < settings.getHost_sizeTeams()){
                                      settings.setSizeTeams(settings.getSizeTeams() + 1);
                                      player.sendMessage("§c§lCRAFTOK §8§l» §bVous avez mis le nombres de joueurs par equpes a §f" + settings.getSizeTeams());
                                      reloadTeams();
                                  }
                              }
                              openMenu();
                          }
                      });

        addActionItem(new ActionItem(29, new ItemCreator(Material.SKULL_ITEM).setDurability((short) 3).setName("§cRegarder la partie: " + (gameHost.getHost().isPlaying() ? "§4Non" : "§aOui"))
                .addLore("")
                .addLore("§e§l» §7Clique ici")
                .addLore("")
                .setOwner(gameHost.getHost().getPlayer().getName()).getItemstack()) {
                        @Override
                        public void onClick(InventoryClickEvent inventoryClickEvent) {
                            gameHost.getHost().setPlaying(!gameHost.getHost().isPlaying());
                            if (gp.isPlaying()){
                                player.sendMessage("§c§lCRAFTOK §8§l» §bDesormais, vous jourez la partie");
                            }else{
                                player.sendMessage("§c§lCRAFTOK §8§l» §bDesormais, vous regarderez la partie");
                            }

                            openMenu();
                        }
                    });

        addMenuItem(30, new GameMapUi(player), new ItemCreator(Material.EMPTY_MAP).setName("§cMap actuel: §6" + game.getGameWorld().getName()).getItemstack(), "§e§l» §7Clique ici");

        addActionItem(new ActionItem(53, new ItemCreator(visibility ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK).setName("§cLe serveur est: " + (visibility ? "§aOuvert" : "§4Fermer"))
                .addLore("")
                .addLore("§e§l» §7Clique ici")
                .addLore("")
                .getItemstack()) {
            @Override
            public void onClick(InventoryClickEvent inventoryClickEvent) {
                player.performCommand("visibility toggle");
                visibility = !visibility;
                openMenu();
            }
        });

        addActionItem(new ActionItem(49, new ItemCreator(Material.SLIME_BALL).setName("§aDemarer la partie")
                .addLore("")
                .addLore("§e§l» §7Clique ici")
                .addLore("")
                .getItemstack()) {
            @Override
            public void onClick(InventoryClickEvent inventoryClickEvent) {
                if (game.getPlayingGPlayers().size() <= GameAPI.getInstance().getGameManagers().getSettings().getSizeTeams()){
                    player.sendMessage("§c§lCRAFTOK §8§l» §cLe serveur n'a pas assez de joueurs pour se lancer");
                    return;
                }
                game.getPlayerUtils().sendMessage("§c§lCRAFTOK §8§l» " + gp.getDisplayName() + " vient de lancer la partie");
                game.getPlayerUtils().sendSound(Sound.LEVEL_UP, 2f);
                game.startTimer();
                player.getInventory().setItem(0, null);
                player.closeInventory();
            }
        });

        addActionItem(new ActionItem(45, new ItemCreator(Material.NAME_TAG).setName("§cFaire une annonce aux Lobby's")
                .addLore("")
                .addLore("§e§l» §7Clique ici")
                .addLore("")
                .getItemstack()) {
            @Override
            public void onClick(InventoryClickEvent inventoryClickEvent) {
                if (!announce) {
                    announce = true;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            announce = false;
                        }
                    }.runTaskLater(GameAPI.getInstance(), 30 * 20);

                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("hostAnnounce");
                    out.writeUTF(player.getName());
                    out.writeUTF(game.getGameWorld().getName());
                    out.writeUTF(game.getPlayingGPlayers().size() + "/" + settings.getMaxPlayers());
                    String teammode = "Solo";
                    if (gameManager.getSettings().getSizeTeams() != 1)
                        teammode = "To" + gameManager.getSettings().getSizeTeams();
                    out.writeUTF(teammode);

                    player.sendPluginMessage(GameAPI.getInstance(), "Craftok", out.toByteArray());
                    openMenu();
                    player.sendMessage("§c§lCRAFTOK §8§l» §aVotre annonce a ete publier dans les lobby's");
                }else {
                    player.sendMessage("§c§lCRAFTOK §8§l» §cVous ne pouvez pas faire d'annonce pour le moment, veuillez reesayer plus tard");
                }
                player.closeInventory();
            }
        });
    }

    public void reloadTeams(){
        GameAPI.getInstance().getGameTeamManager().deleteTeam();
        GameAPI.getInstance().getGameTeamManager().createTeams();

        GameAPI.getInstance().getGameManagers().getGameHost().getHost().getGame().getGPlayers().forEach(gp -> {
            gp.setTeam(gp.getPlayer(), null);
            if (GameAPI.getInstance().getGameManagers().getSettings().getSizeTeams() != 1) {
                gp.getPlayer().getInventory().setItem(1, new ItemCreator(Material.BANNER).setName("§e§lChoix de l'equipe").getItemstack());
                gp.getPlayer().getInventory().setHeldItemSlot(1);
            }else{
                gp.getPlayer().getInventory().setItem(1, null);
            }
            if (gp.getPlayer().getOpenInventory().getTitle().equalsIgnoreCase(new GameTeamUi(gp.getPlayer(), gp.getGame()).getTitle())){
                gp.getPlayer().closeInventory();
            }
        });

    }
}
