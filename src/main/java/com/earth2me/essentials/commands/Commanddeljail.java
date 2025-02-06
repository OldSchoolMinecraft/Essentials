package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commanddeljail extends EssentialsCommand
{
    public Commanddeljail() {
        super("deljail");
    }
    
    @Override
    protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.ess.getJail().delJail(args[0]);
        sender.sendMessage(Util.format("deleteJail", args[0]));
    }
}
