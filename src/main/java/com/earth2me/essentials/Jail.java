package com.earth2me.essentials;

import java.util.logging.*;
import java.io.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.event.block.*;

public class Jail extends BlockListener implements IConf
{
    private static final Logger logger;
    private final EssentialsConf config;
    private final IEssentials ess;
    
    public Jail(final IEssentials ess) {
        this.ess = ess;
        (this.config = new EssentialsConf(new File(ess.getDataFolder(), "jail.yml"))).load();
    }
    
    public void setJail(final Location loc, final String jailName) throws Exception {
        this.config.setProperty(jailName.toLowerCase(), loc);
        this.config.save();
    }
    
    public Location getJail(final String jailName) throws Exception {
        if (jailName == null || this.config.getProperty(jailName.toLowerCase()) == null) {
            throw new Exception(Util.i18n("jailNotExist"));
        }
        final Location loc = this.config.getLocation(jailName.toLowerCase(), this.ess.getServer());
        return loc;
    }
    
    public void sendToJail(final User user, final String jail) throws Exception {
        if (!(user.getBase() instanceof OfflinePlayer)) {
            user.getTeleport().now(this.getJail(jail));
        }
        user.setJail(jail);
    }
    
    public void delJail(final String jail) throws Exception {
        this.config.removeProperty(jail.toLowerCase());
        this.config.save();
    }
    
    public List<String> getJails() throws Exception {
        return (List<String>)this.config.getKeys((String)null);
    }
    
    public void reloadConfig() {
        this.config.load();
    }
    
    public void onBlockBreak(final BlockBreakEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        if (user.isJailed()) {
            event.setCancelled(true);
        }
    }
    
    public void onBlockPlace(final BlockPlaceEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        if (user.isJailed()) {
            event.setCancelled(true);
        }
    }
    
    public void onBlockDamage(final BlockDamageEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        if (user.isJailed()) {
            event.setCancelled(true);
        }
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
    }
}
