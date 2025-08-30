package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandseen extends EssentialsCommand
{
    public Commandseen() {
        super("seen");
    }
    
    @Override
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        try {
            final User u = this.getPlayer(server, args, 0);
            sender.sendMessage(Util.format("seenOnline", u.getDisplayName(), Util.formatDateDiff(u.getLastLogin())));
        }
        catch (NoSuchFieldException e) {
            final User u2 = this.ess.getOfflineUser(args[0]);
            if (u2 == null) {
                throw new Exception(Util.i18n("playerNotFound"));
            }
            sender.sendMessage(Util.format("seenOffline", u2.getDisplayName(), Util.formatDateDiff(u2.getLastLogout())));
        }
    }
}
