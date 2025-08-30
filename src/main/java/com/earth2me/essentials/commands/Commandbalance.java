package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandbalance extends EssentialsCommand
{
    public Commandbalance() {
        super("balance");
    }
    
    @Override
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        sender.sendMessage(Util.format("balance", Util.formatCurrency(this.getPlayer(server, args, 0, true).getMoney(), this.ess)));
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final double bal = ((args.length < 1 || (!user.isAuthorized("essentials.balance.others") && !user.isAuthorized("essentials.balance.other"))) ? user : this.getPlayer(server, args, 0, true)).getMoney();
        user.sendMessage(Util.format("balance", Util.formatCurrency(bal, this.ess)));
    }
}
