package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;

public class Commandeco extends EssentialsCommand
{
    public Commandeco() {
        super("eco");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        EcoCommands cmd;
        double amount;
        try {
            cmd = EcoCommands.valueOf(args[0].toUpperCase());
            amount = Double.parseDouble(args[2].replaceAll("[^0-9\\.]", ""));
        }
        catch (Exception ex) {
            throw new NotEnoughArgumentsException(ex);
        }
        if (args[1].contentEquals("*")) {
            for (final Player p : server.getOnlinePlayers()) {
                final User u = this.ess.getUser(p);
                switch (cmd) {
                    case GIVE: {
                        u.giveMoney(amount);
                        break;
                    }
                    case TAKE: {
                        u.takeMoney(amount);
                        break;
                    }
                    case RESET: {
                        u.setMoney((amount == 0.0) ? ((double)this.ess.getSettings().getStartingBalance()) : amount);
                        break;
                    }
                }
            }
        }
        else {
            final User u2 = this.getPlayer(server, args, 1, true);
            switch (cmd) {
                case GIVE: {
                    u2.giveMoney(amount, sender);
                    break;
                }
                case TAKE: {
                    u2.takeMoney(amount, sender);
                    break;
                }
                case RESET: {
                    u2.setMoney((amount == 0.0) ? ((double)this.ess.getSettings().getStartingBalance()) : amount);
                    break;
                }
            }
        }
    }
    
    private enum EcoCommands
    {
        GIVE, 
        TAKE, 
        RESET;
    }
}
