package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandtop extends EssentialsCommand
{
    public Commandtop() {
        super("top");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final int topX = user.getLocation().getBlockX();
        final int topZ = user.getLocation().getBlockZ();
        final int topY = user.getWorld().getHighestBlockYAt(topX, topZ);
        user.getTeleport().teleport(new Location(user.getWorld(), user.getLocation().getX(), (double)(topY + 1), user.getLocation().getZ()), new Trade(this.getName(), this.ess));
        user.sendMessage(Util.i18n("teleportTop"));
    }
}
