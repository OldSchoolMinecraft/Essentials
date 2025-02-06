package com.earth2me.essentials;

import org.bukkit.plugin.*;
import org.bukkit.command.*;
import org.bukkit.*;
import org.bukkit.scheduler.*;
import com.earth2me.essentials.register.payment.*;
import com.earth2me.essentials.perm.*;

public interface IEssentials extends Plugin
{
    void addReloadListener(final IConf p0);
    
    void reload();
    
    boolean onCommandEssentials(final CommandSender p0, final Command p1, final String p2, final String[] p3, final ClassLoader p4, final String p5, final String p6);
    
    User getUser(final Object p0);
    
    User getOfflineUser(final String p0);
    
    World getWorld(final String p0);
    
    int broadcastMessage(final IUser p0, final String p1);
    
    ISettings getSettings();
    
    BukkitScheduler getScheduler();
    
    String[] getMotd(final CommandSender p0, final String p1);
    
    String[] getLines(final CommandSender p0, final String p1, final String p2);
    
    Jail getJail();
    
    Warps getWarps();
    
    Worth getWorth();
    
    Backup getBackup();
    
    Spawn getSpawn();
    
    Methods getPaymentMethod();
    
    int scheduleAsyncDelayedTask(final Runnable p0);
    
    int scheduleSyncDelayedTask(final Runnable p0);
    
    int scheduleSyncDelayedTask(final Runnable p0, final long p1);
    
    int scheduleSyncRepeatingTask(final Runnable p0, final long p1, final long p2);
    
    BanWorkaround getBans();
    
    TNTExplodeListener getTNTListener();
    
    PermissionsHandler getPermissionsHandler();
    
    void showError(final CommandSender p0, final Throwable p1, final String p2);
    
    ItemDb getItemDb();
    
    UserMap getUserMap();
}
