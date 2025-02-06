package com.earth2me.essentials;

import com.google.common.base.*;
import java.util.concurrent.*;
import com.google.common.collect.*;
import java.io.*;
import org.bukkit.entity.*;
import java.util.*;

public class UserMap implements Function<String, User>, IConf
{
    private final transient IEssentials ess;
    private final transient ConcurrentMap<String, User> users;
    
    public UserMap(final IEssentials ess) {
        this.users = (ConcurrentMap<String, User>)new MapMaker().softValues().makeComputingMap((Function)this);
        this.loadAllUsersAsync(this.ess = ess);
    }
    
    private void loadAllUsersAsync(final IEssentials ess) {
        ess.scheduleAsyncDelayedTask(new Runnable() {
            @Override
            public void run() {
                final File userdir = new File(ess.getDataFolder(), "userdata");
                if (!userdir.exists()) {
                    return;
                }
                for (final String string : userdir.list()) {
                    if (string.endsWith(".yml")) {
                        final String name = string.substring(0, string.length() - 4);
                        try {
                            UserMap.this.users.get(name.toLowerCase());
                        }
                        catch (NullPointerException ex) {}
                    }
                }
            }
        });
    }
    
    public boolean userExists(final String name) {
        return this.users.containsKey(name.toLowerCase());
    }
    
    public User getUser(final String name) throws NullPointerException {
        return this.users.get(name.toLowerCase());
    }
    
    public User apply(final String name) {
        for (final Player player : this.ess.getServer().getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return new User(player, this.ess);
            }
        }
        final File userFolder = new File(this.ess.getDataFolder(), "userdata");
        final File userFile = new File(userFolder, Util.sanitizeFileName(name) + ".yml");
        if (userFile.exists()) {
            return new User((Player)new OfflinePlayer(name, this.ess), this.ess);
        }
        return null;
    }
    
    public void reloadConfig() {
        for (final User user : this.users.values()) {
            user.reloadConfig();
        }
    }
    
    public void removeUser(final String name) {
        this.users.remove(name.toLowerCase());
    }
    
    public Set<User> getAllUsers() {
        final Set<User> userSet = new HashSet<User>();
        for (final String name : this.users.keySet()) {
            userSet.add(this.users.get(name));
        }
        return userSet;
    }
    
    public int getUniqueUsers() {
        return this.users.size();
    }
}
