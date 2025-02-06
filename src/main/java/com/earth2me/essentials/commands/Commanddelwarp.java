package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commanddelwarp extends EssentialsCommand
{
    public Commanddelwarp() {
        super("delwarp");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.ess.getWarps().delWarp(args[0]);
        sender.sendMessage(Util.format("deleteWarp", args[0]));
    }
}
