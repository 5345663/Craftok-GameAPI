package eu.craftok.gameapi.command;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.GameManager;
import eu.craftok.gameapi.game.player.GamePlayer;
import eu.craftok.gameapi.game.player.GamePlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Project game-api Created by Sithey
 */

public class ReCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            GameManager gameManager = GameAPI.getInstance().getGameManagers();
            GamePlayerManager gamePlayerManager = GameAPI.getInstance().getGamePlayerManager();
            GamePlayer gp = gamePlayerManager.getGPlayer(player);
            if (!gp.getGame().getGameStatus().isLobby() && !gameManager.getSettings().isHost()){
                if (gameManager.getJoinableGame() == null) {
                    GameAPI.getInstance().getBungeeCord().sendToAnotherGame(gp.getPlayer());
                } else {
                    gp.onQuit(gp.getPlayer(), gp.getGame());
                    gp.onJoin(gp.getPlayer(), GameAPI.getInstance().getGameManagers().getJoinableGame());
                }
            }
        }
        return false;
    }


}
