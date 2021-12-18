package eu.craftok.gameapi.command;

import eu.craftok.gameapi.GameAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Project game-api Created by Sithey
 */

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            GameAPI.getInstance().getGameManagers().openStatsInventory((Player) sender);
        }
        return false;
    }


}
