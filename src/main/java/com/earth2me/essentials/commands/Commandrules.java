package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandrules extends EssentialsCommand
{
    public Commandrules() {
        super("rules");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        for (final String m : this.ess.getLines(sender, "rules", Util.i18n("noRules"))) {
            sender.sendMessage(m);
        }
    }
}
