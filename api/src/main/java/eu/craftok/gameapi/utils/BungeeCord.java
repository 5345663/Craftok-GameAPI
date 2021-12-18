package eu.craftok.gameapi.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.craftok.gameapi.GameAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * Project game-api Created by Sithey
 */

public class BungeeCord implements PluginMessageListener {

    public BungeeCord(){
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(GameAPI.getInstance(), "BungeeCord");
        messenger.registerIncomingPluginChannel(GameAPI.getInstance(), "BungeeCord", this);
        messenger.registerOutgoingPluginChannel(GameAPI.getInstance(), "Craftok");
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {

    }

    public void sendToLobby(Player player){
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            try {
                out.writeUTF("Connect");
                out.writeUTF("Lobby");
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.sendPluginMessage(GameAPI.getInstance(), "BungeeCord", out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToAnotherGame(Player player){
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            try {
                out.writeUTF("Connect");
                out.writeUTF(GameAPI.getInstance().getGameManagers().getTask());
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.sendPluginMessage(GameAPI.getInstance(), "BungeeCord", out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
