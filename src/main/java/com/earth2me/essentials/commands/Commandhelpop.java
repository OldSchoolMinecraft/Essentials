package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import java.util.logging.*;
import org.bukkit.entity.*;

public class Commandhelpop extends EssentialsCommand
{
    public Commandhelpop() {
        super("helpop");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final String message = Util.format("helpOp", user.getDisplayName(), EssentialsCommand.getFinalArg(args, 0));
        Commandhelpop.logger.log(Level.INFO, message);
        for (final Player p : server.getOnlinePlayers()) {
            final User u = this.ess.getUser(p);
            if (u.isAuthorized("essentials.helpop.receive")) {
                u.sendMessage(message);
            }
        }
    }
}
