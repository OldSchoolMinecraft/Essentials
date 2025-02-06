package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandbanip extends EssentialsCommand
{
    public Commandbanip() {
        super("banip");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.ess.getBans().banByIp(args[0]);
        sender.sendMessage(Util.i18n("banIpAddress"));
    }
}
