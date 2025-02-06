package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandkill extends EssentialsCommand
{
    public Commandkill() {
        super("kill");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        for (final Player p : server.matchPlayer(args[0])) {
            p.setHealth(0);
            sender.sendMessage(Util.format("kill", p.getDisplayName()));
        }
    }
}
