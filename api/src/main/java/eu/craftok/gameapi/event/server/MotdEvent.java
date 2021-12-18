package eu.craftok.gameapi.event.server;

import eu.craftok.gameapi.GameAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * Project game-api Created by Sithey
 */

public class MotdEvent implements Listener {


    @EventHandler
    public void onMotd(ServerListPingEvent event){
        if (GameAPI.getInstance().getGameManagers().getSettings().isHost()){
            return;
        }
        if (GameAPI.getInstance().getGameManagers().getJoinableGame() == null){
            event.setMotd("INGAME");
        }else{
            event.setMotd("WAITING");
        }
    }
}
