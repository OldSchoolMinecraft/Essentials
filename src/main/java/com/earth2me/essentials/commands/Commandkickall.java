package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;

public class Commandkickall extends EssentialsCommand
{
    public Commandkickall() {
        super("kickall");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        for (final Player p : server.getOnlinePlayers()) {
            if (!(sender instanceof Player) || !p.getName().equalsIgnoreCase(((Player)sender).getName())) {
                p.kickPlayer((args.length > 0) ? EssentialsCommand.getFinalArg(args, 0) : Util.i18n("kickDefault"));
            }
        }
    }
}
