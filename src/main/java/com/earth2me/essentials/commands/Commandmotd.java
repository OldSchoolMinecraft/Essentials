package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandmotd extends EssentialsCommand
{
    public Commandmotd() {
        super("motd");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        for (final String m : this.ess.getMotd(sender, Util.i18n("noMotd"))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
        }
    }
}
