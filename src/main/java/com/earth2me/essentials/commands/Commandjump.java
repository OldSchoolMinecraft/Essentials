package com.earth2me.essentials.commands;

import org.bukkit.entity.*;
import com.earth2me.essentials.*;
import org.bukkit.*;

public class Commandjump extends EssentialsCommand
{
    public Commandjump() {
        super("jump");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final Location cloc = user.getLocation();
        Location loc;
        try {
            loc = new TargetBlock((Player)user, 100, 2.65).getTargetBlock().getLocation();
            loc.setYaw(cloc.getYaw());
            loc.setPitch(cloc.getPitch());
            loc = new TargetBlock(loc).getPreviousBlock().getLocation();
            loc.setYaw(cloc.getYaw());
            loc.setPitch(cloc.getPitch());
            loc.setY(loc.getY() + 1.0);
        }
        catch (NullPointerException ex) {
            throw new Exception(Util.i18n("jumpError"), ex);
        }
        final Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        user.getTeleport().teleport(loc, charge);
    }
}
