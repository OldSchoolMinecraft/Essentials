package com.earth2me.essentials.commands;

import com.earth2me.essentials.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class Commandantioch extends EssentialsCommand
{
    public Commandantioch() {
        super("antioch");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        this.ess.broadcastMessage(user, "...lobbest thou thy Holy Hand Grenade of Antioch towards thy foe,");
        this.ess.broadcastMessage(user, "who being naughty in My sight, shall snuff it.");
        final Location loc = new TargetBlock((Player)user).getTargetBlock().getLocation();
        loc.getWorld().spawn(loc, (Class)TNTPrimed.class);
    }
}
