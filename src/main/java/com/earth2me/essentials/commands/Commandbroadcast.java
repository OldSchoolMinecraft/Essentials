package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandbroadcast extends EssentialsCommand
{
    public Commandbroadcast() {
        super("broadcast");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.ess.broadcastMessage(null, Util.format("broadcast", EssentialsCommand.getFinalArg(args, 0)));
    }
}
