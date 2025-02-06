package com.earth2me.essentials;

import org.bukkit.*;
import com.earth2me.essentials.commands.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import java.net.*;

public interface IUser
{
    int getHealth();
    
    Location getLocation();
    
    boolean isOnline();
    
    void sendMessage(final String p0);
    
    long getLastTeleportTimestamp();
    
    boolean isAuthorized(final String p0);
    
    boolean isAuthorized(final IEssentialsCommand p0);
    
    boolean isAuthorized(final IEssentialsCommand p0, final String p1);
    
    void setLastTeleportTimestamp(final long p0);
    
    Location getLastLocation();
    
    Player getBase();
    
    double getMoney();
    
    void takeMoney(final double p0);
    
    void giveMoney(final double p0);
    
    PlayerInventory getInventory();
    
    void updateInventory();
    
    String getGroup();
    
    void setLastLocation();
    
    Location getHome(final String p0) throws Exception;
    
    Location getHome(final Location p0) throws Exception;
    
    String getName();
    
    InetSocketAddress getAddress();
    
    String getDisplayName();
    
    boolean isHidden();
}
