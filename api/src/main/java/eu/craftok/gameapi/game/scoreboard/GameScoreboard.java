package eu.craftok.gameapi.game.scoreboard;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.player.GamePlayer;
import eu.craftok.gameapi.utils.scoreboard.SidebarManager;
import org.bukkit.Bukkit;

/**
 * Project game-api Created by Sithey
 */

public class GameScoreboard extends SidebarManager {

    @Override
    public void build(GamePlayer p, SidebarEditor e) {
        e.setTitle("§c§l" + GameAPI.getInstance().getGameManagers().getName());
    }

    public void uptadeAllTime() {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(GameAPI.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() != 0){
                    update();
                }
            }
        }, 5L, 5L);
    }
}
