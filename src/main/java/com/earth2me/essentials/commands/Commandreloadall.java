package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandreloadall extends EssentialsCommand
{
    public Commandreloadall() {
        super("reloadall");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        server.reload();
        sender.sendMessage(Util.i18n("reloadAllPlugins"));
    }
}
