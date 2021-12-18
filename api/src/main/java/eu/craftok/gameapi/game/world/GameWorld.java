package eu.craftok.gameapi.game.world;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import eu.craftok.gameapi.GameAPI;
import eu.craftok.gameapi.game.Game;
import eu.craftok.utils.CConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

/**
 * Project game-api Created by Sithey
 */

public class GameWorld {

    private final Game game;
    private final CConfig config;
    private final String name;
    private final World world;
    private int maxheight;
    private Location waiting, middle;

    public GameWorld(Game game, CConfig config){
        this.game = game;
        this.config = config;

        this.name = config.getFile().getName().replace(".yml", "");
        config.addValue("position.waiting", "0:64:0:0:0");
        config.addValue("position.middle", "0:64:0:0:0");
        config.addValue("border.size", 50d);
        final String[] waitingposparts = config.getValue("position.waiting").toString().split(":");
        final double waiting_x = Double.parseDouble(waitingposparts[0]);
        final double waiting_y = Double.parseDouble(waitingposparts[1]);
        final double waiting_z = Double.parseDouble(waitingposparts[2]);
        final SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SlimeLoader sqlLoader = plugin.getLoader("file");

        try {
            SlimePropertyMap prop = new SlimePropertyMap();

            prop.setString(SlimeProperties.DIFFICULTY, "normal");
            prop.setInt(SlimeProperties.SPAWN_X, (int) waiting_x);
            prop.setInt(SlimeProperties.SPAWN_Y, (int) waiting_y);
            prop.setInt(SlimeProperties.SPAWN_Z, (int) waiting_z);
            prop.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
            prop.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
            prop.setBoolean(SlimeProperties.PVP, true);
            SlimeWorld world = plugin.loadWorld(sqlLoader, name, true, prop).clone(name + game.getUniqueId());
            plugin.generateWorld(world);
        } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
            e.printStackTrace();
        }

        this.world = Bukkit.getWorld(name + game.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                initMap();
            }
        }.runTaskLater(GameAPI.getInstance(), 5);

    }

    /**
     * Initialisation of the world
     */

    public void initMap(){
        (waiting = stringToLocation(config.getValue("position.waiting").toString())).getChunk().load();
        (middle = stringToLocation(config.getValue("position.middle").toString())).getChunk().load();
        maxheight = world.getMaxHeight();
        world.getWorldBorder().setCenter(middle);
        world.getWorldBorder().setSize((Double) config.getValue("border.size"));
    }

    public void deleteMap(){
        final SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SlimeLoader sqlLoader = plugin.getLoader("file");
        try {
            sqlLoader.deleteWorld(getName());
        } catch (UnknownWorldException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the world name
     */

    public String getName() {
        return name;
    }

    /**
     * @return the configuration file
     */

    public CConfig getConfig() {
        return config;
    }

    /**
     * @return the game of this world
     */

    public Game getGame() {
        return game;
    }

    /**
     * @return the maximum height of the world
     */

    public int getMaxheight() {
        return maxheight;
    }

    /**
     * @return the Location of the center of the world
     * This is important for setup the worldborder center
     */

    public Location getMiddle() {
        return middle;
    }

    /**
     * @return the Location of the Waitig Lobby
     */

    public Location getWaiting() {
        return waiting;
    }

    /**
     * @return the Bukkit World
     */

    public World getWorld() {
        return world;
    }

    /**
     * @param string is config String
     * @return Used to transform string to Location
     */

    public Location stringToLocation(String string){
        String[] middleposparts = string.split(":");
        Location location = new Location(world, Double.parseDouble(middleposparts[0]), Double.parseDouble(middleposparts[1]), Double.parseDouble(middleposparts[2]));
        if (middleposparts.length == 5){
            location.setYaw(Float.parseFloat(middleposparts[3]));
            location.setPitch(Float.parseFloat(middleposparts[4]));
        }
        return location;
    }
}
