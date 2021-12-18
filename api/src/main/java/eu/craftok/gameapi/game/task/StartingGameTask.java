package eu.craftok.gameapi.game.task;

import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.Game;
import eu.craftok.utils.PlayerUtils;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Project game-api Created by Sithey
 */

public class StartingGameTask extends BukkitRunnable {

    private Game game;

    public StartingGameTask(Game game){
        this.game = game;
    }

    @Override
    public void run() {
        PlayerUtils util = game.getPlayerUtils();

        int timer = getGame().getTimer();

        util.sendActionBar("§fDébut dans §b" + timer + "§bs");

        if (timer == 30 || timer == 15 || timer == 10 || timer == 5 || timer == 4 || timer == 3 || timer == 2) {
            if (timer == 5){
                util.sendTitle(5, 20, 5, "§a❺", "");
            }
            if (timer == 4){
                util.sendTitle(5, 20, 5, "§e➍", "");
            }
            if (timer == 3){
                util.sendTitle(5, 20, 5, "§e➌", "");
            }
            if (timer == 2){
                util.sendTitle(5, 20, 5, "§6➋", "");
            }
            util.sendMessage("§c§lCRAFTOK §8§l» §7La partie va commencer dans §c" + timer + "§7 secondes");
            util.sendSound(Sound.NOTE_PIANO, 2f);
        }

        if (timer == 1){
            util.sendMessage("§c§lCRAFTOK §8§l» §7La partie va commencer dans §c" + timer + "§7 seconde");
            util.sendTitle(5, 20, 5, "§c➊", "");
            util.sendSound(Sound.NOTE_PIANO, 2f);
        }

        if (timer == 0) {
            cancel();
            util.sendSound(Sound.ORB_PICKUP, 2f);
            getGame().onStart();
            return;
        }

        if (!GameAPI.getInstance().getGameManagers().getSettings().isHost() && timer > 3 && getGame().getPlayingGPlayers().size() < GameAPI.getInstance().getGameManagers().getSettings().getMinPlayers()) {
            cancel();
            getGame().setTimer(GameAPI.getInstance().getGameManagers().getSettings().startTimer());
            return;
        }

        getGame().remTimer();
    }

    public Game getGame() {
        return game;
    }
}
