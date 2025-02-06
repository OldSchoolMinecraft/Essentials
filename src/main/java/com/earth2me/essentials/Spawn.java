package com.earth2me.essentials;

import java.util.logging.*;
import java.io.*;
import org.bukkit.*;
import java.util.*;

public class Spawn implements IConf
{
    private static final Logger logger;
    private final EssentialsConf config;
    private final Server server;
    
    public Spawn(final Server server, final File dataFolder) {
        final File configFile = new File(dataFolder, "spawn.yml");
        this.server = server;
        (this.config = new EssentialsConf(configFile)).load();
    }
    
    public void setSpawn(final Location loc, final String group) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("world", loc.getWorld().getName());
        map.put("x", loc.getX());
        map.put("y", loc.getY());
        map.put("z", loc.getZ());
        map.put("yaw", loc.getYaw());
        map.put("pitch", loc.getPitch());
        this.config.setProperty(group, (Object)map);
        this.config.save();
        if ("default".equals(group)) {
            loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        }
    }
    
    public Location getSpawn(String group) {
        if (this.config.getProperty(group) == null) {
            group = "default";
        }
        if (this.config.getProperty(group) == null) {
            for (final World w : this.server.getWorlds()) {
                if (w.getEnvironment() != World.Environment.NORMAL) {
                    continue;
                }
                return w.getSpawnLocation();
            }
        }
        final String worldId = this.config.getString(group + ".world", "");
        World world = this.server.getWorlds().get((this.server.getWorlds().size() > 1) ? 1 : 0);
        for (final World w2 : this.server.getWorlds()) {
            if (w2.getEnvironment() != World.Environment.NORMAL) {
                continue;
            }
            world = w2;
            break;
        }
        for (final World w2 : this.server.getWorlds()) {
            if (!w2.getName().equals(worldId)) {
                continue;
            }
            world = w2;
            break;
        }
        final double x = this.config.getDouble(group + ".x", this.config.getDouble("default.x", 0.0));
        final double y = this.config.getDouble(group + ".y", this.config.getDouble("default.y", 0.0));
        final double z = this.config.getDouble(group + ".z", this.config.getDouble("default.z", 0.0));
        final float yaw = (float)this.config.getDouble(group + ".yaw", this.config.getDouble("default.yaw", 0.0));
        final float pitch = (float)this.config.getDouble(group + ".pitch", this.config.getDouble("default.pitch", 0.0));
        final Location retval = new Location(world, x, y, z, yaw, pitch);
        if (y < 1.0) {
            retval.setY((double)world.getHighestBlockYAt(retval));
        }
        return retval;
    }
    
    @Override
    public void reloadConfig() {
        this.config.load();
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
    }
}
