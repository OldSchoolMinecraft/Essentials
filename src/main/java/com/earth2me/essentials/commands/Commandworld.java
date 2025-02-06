package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandworld extends EssentialsCommand
{
    public Commandworld() {
        super("world");
    }
    
    @Override
    protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final List<World> worlds = (List<World>)server.getWorlds();
        World world3;
        if (args.length < 1) {
            World nether = server.getWorld(this.ess.getSettings().getNetherName());
            if (nether == null) {
                for (final World world2 : worlds) {
                    if (world2.getEnvironment() == World.Environment.NETHER) {
                        nether = world2;
                        break;
                    }
                }
                if (nether == null) {
                    return;
                }
            }
            world3 = ((user.getWorld() == nether) ? worlds.get(0) : nether);
        }
        else {
            world3 = this.ess.getWorld(EssentialsCommand.getFinalArg(args, 0));
            if (world3 == null) {
                user.sendMessage(Util.i18n("invalidWorld"));
                user.sendMessage(Util.format("possibleWorlds", server.getWorlds().size() - 1));
                user.sendMessage(Util.i18n("typeWorldName"));
                throw new NoChargeException();
            }
        }
        double factor;
        if (user.getWorld().getEnvironment() == World.Environment.NETHER && world3.getEnvironment() == World.Environment.NORMAL) {
            factor = this.ess.getSettings().getNetherRatio();
        }
        else if (user.getWorld().getEnvironment() != world3.getEnvironment()) {
            factor = 1.0 / this.ess.getSettings().getNetherRatio();
        }
        else {
            factor = 1.0;
        }
        Location loc = user.getLocation();
        loc = new Location(world3, loc.getBlockX() * factor + 0.5, (double)loc.getBlockY(), loc.getBlockZ() * factor + 0.5);
        final Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        user.getTeleport().teleport(loc, charge);
        throw new NoChargeException();
    }
}
