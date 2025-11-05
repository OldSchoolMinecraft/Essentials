package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandpay extends EssentialsCommand
{
    public Commandpay() {
        super("pay");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        final double amount = Double.parseDouble(args[1].replaceAll("[^0-9\\.]", ""));
        Boolean foundUser = false;
        for (final Player p : server.matchPlayer(args[0])) {
            final User u = this.ess.getUser(p);
            if (u.isHidden()) {
                continue;
            }
            user.payUser(u, amount);
            foundUser = true;
            break;
        }
        if (!foundUser) {
            throw new NoSuchFieldException(Util.i18n("playerNotFound"));
        }
    }
}
