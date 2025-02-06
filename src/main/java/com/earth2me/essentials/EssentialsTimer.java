package com.earth2me.essentials;

import org.bukkit.entity.*;
import java.util.*;

public class EssentialsTimer implements Runnable
{
    private final transient IEssentials ess;
    private final transient Set<User> onlineUsers;
    
    EssentialsTimer(final IEssentials ess) {
        this.onlineUsers = new HashSet<User>();
        this.ess = ess;
    }
    
    @Override
    public void run() {
        final long currentTime = System.currentTimeMillis();
        for (final Player player : this.ess.getServer().getOnlinePlayers()) {
            final User user = this.ess.getUser(player);
            this.onlineUsers.add(user);
            user.setLastOnlineActivity(currentTime);
            user.checkActivity();
        }
        final Iterator<User> iterator = this.onlineUsers.iterator();
        while (iterator.hasNext()) {
            final User user2 = iterator.next();
            if (user2.getLastOnlineActivity() < currentTime && user2.getLastOnlineActivity() > user2.getLastLogout()) {
                user2.setLastLogout(user2.getLastOnlineActivity());
                iterator.remove();
            }
            else {
                user2.checkMuteTimeout(currentTime);
                user2.checkJailTimeout(currentTime);
            }
        }
    }
}
