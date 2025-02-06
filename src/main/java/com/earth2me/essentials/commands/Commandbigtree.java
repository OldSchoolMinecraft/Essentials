package com.earth2me.essentials.commands;

import org.bukkit.entity.*;
import com.earth2me.essentials.*;
import org.bukkit.*;

public class Commandbigtree extends EssentialsCommand
{
    public Commandbigtree() {
        super("bigtree");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        TreeType tree;
        if (args.length > 0 && args[0].equalsIgnoreCase("redwood")) {
            tree = TreeType.TALL_REDWOOD;
        }
        else {
            if (args.length <= 0 || !args[0].equalsIgnoreCase("tree")) {
                throw new NotEnoughArgumentsException();
            }
            tree = TreeType.BIG_TREE;
        }
        final int[] ignore = { 8, 9 };
        final Location loc = new TargetBlock((Player)user, 300, 0.2, ignore).getTargetBlock().getLocation();
        final Location safeLocation = Util.getSafeDestination(loc);
        final boolean success = user.getWorld().generateTree(safeLocation, tree);
        if (success) {
            user.sendMessage(Util.i18n("bigTreeSuccess"));
            return;
        }
        throw new Exception(Util.i18n("bigTreeFailure"));
    }
}
