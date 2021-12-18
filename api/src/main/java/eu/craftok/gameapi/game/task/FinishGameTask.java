package eu.craftok.gameapi.game.task;

import eu.craftok.gameapi.game.Game;
import eu.craftok.utils.firework.FireworkBuilder;
import eu.craftok.utils.firework.FireworkUtils;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Project game-api Created by Sithey
 */

public class FinishGameTask extends BukkitRunnable {

    private Game game;

    public FinishGameTask(Game game){
        this.game = game;
    }

    @Override
    public void run() {

        int timer = getGame().getTimer();

        if (timer == 30 || timer == 15 || timer == 10 || timer == 5 || timer == 4 || timer == 3 || timer == 2) {
            game.getPlayerUtils().sendMessage("§c§lCRAFTOK §8§l» §7La partie va s'arreter dans §c" + timer + "§7 secondes");
        }

        if (timer == 1){
            game.getPlayerUtils().sendMessage("§c§lCRAFTOK §8§l» §7La partie va s'arreter dans §c" + timer + "§7 seconde");
        }

        if (timer == 0){
            game.onStop();
            cancel();
            return;
        }

        game.getgWinners().forEach(gp -> {
           if (gp.getGame().equals(game) && gp.getPlayer() != null){
               FireworkBuilder.summonInstantFirework(FireworkUtils.getRandomFireworkEffect(), gp.getPlayer().getLocation());
           }
        });

        getGame().remTimer();
    }

    public Game getGame() {
        return game;
    }
}
