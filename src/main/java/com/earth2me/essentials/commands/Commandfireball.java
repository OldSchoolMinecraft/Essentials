package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.entity.*;
import org.bukkit.util.*;

public class Commandfireball extends EssentialsCommand
{
    public Commandfireball() {
        super("fireball");
    }
    
    @Override
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final Vector direction = user.getEyeLocation().getDirection().multiply(2);
        user.getWorld().spawn(user.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), (Class)Fireball.class);
    }
}
