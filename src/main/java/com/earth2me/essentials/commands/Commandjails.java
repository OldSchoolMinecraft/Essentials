package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandjails extends EssentialsCommand
{
    public Commandjails() {
        super("jails");
    }
    
    @Override
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        sender.sendMessage("§7" + Util.joinList(" ", this.ess.getJail().getJails()));
    }
}
