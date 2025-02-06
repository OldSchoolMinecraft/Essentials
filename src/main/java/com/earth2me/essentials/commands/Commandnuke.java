package com.earth2me.essentials.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;

public class Commandnuke extends EssentialsCommand
{
    public Commandnuke() {
        super("nuke");
    }
    
    @Override
    protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws NoSuchFieldException, NotEnoughArgumentsException {
        List<Player> targets;
        if (args.length > 0) {
            targets = new ArrayList<Player>();
            int pos = 0;
            for (final String arg : args) {
                targets.add((Player)this.getPlayer(server, args, pos));
                ++pos;
            }
        }
        else {
            targets = Arrays.asList(server.getOnlinePlayers());
        }
        this.ess.getTNTListener().enable();
        for (final Player player : targets) {
            if (player == null) {
                continue;
            }
            player.sendMessage("May death rain upon them");
            final Location loc = player.getLocation();
            final World world = loc.getWorld();
            for (int x = -10; x <= 10; x += 5) {
                for (int z = -10; z <= 10; z += 5) {
                    final Location tntloc = new Location(world, (double)(loc.getBlockX() + x), 127.0, (double)(loc.getBlockZ() + z));
                    final TNTPrimed tnt = (TNTPrimed)world.spawn(tntloc, (Class)TNTPrimed.class);
                }
            }
        }
    }
}
