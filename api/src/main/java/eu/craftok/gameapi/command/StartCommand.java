package eu.craftok.gameapi.command;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.Game;
import eu.craftok.gameapi.game.player.GamePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Project game-api Created by Sithey
 */

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            if (sender.hasPermission("craftok.forcestart")) {
                GamePlayer gp = GameAPI.getInstance().getGamePlayerManager().getGPlayer((Player) sender);
                Game game = gp.getGame();
                if (game.getGameStatus().isLobby()) {
                    if (game.getPlayingGPlayers().size() <= GameAPI.getInstance().getGameManagers().getSettings().getSizeTeams()){
                        return false;
                    }
                    if (game.getTimer() == GameAPI.getInstance().getGameManagers().getSettings().startTimer()) {
                        game.remTimer();
                        game.startTimer();
                    }
                    game.setTimer(3);
                }
            }
        }
        return false;
    }
}
