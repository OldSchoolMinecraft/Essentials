package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandburn extends EssentialsCommand
{
    public Commandburn() {
        super("burn");
    }
    
    @Override
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        for (final Player p : server.matchPlayer(args[0])) {
            p.setFireTicks(Integer.parseInt(args[1]) * 20);
            sender.sendMessage(Util.format("burnMsg", p.getDisplayName(), Integer.parseInt(args[1])));
        }
    }
}
