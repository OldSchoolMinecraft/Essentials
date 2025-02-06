package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandunban extends EssentialsCommand
{
    public Commandunban() {
        super("unban");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        String name;
        try {
            final User u = this.getPlayer(server, args, 0, true);
            name = u.getName();
        }
        catch (NoSuchFieldException e) {
            name = args[0];
        }
        this.ess.getBans().unbanByName(name);
        sender.sendMessage(Util.i18n("unbannedPlayer"));
    }
}
