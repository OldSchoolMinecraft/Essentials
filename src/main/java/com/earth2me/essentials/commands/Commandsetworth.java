package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.inventory.*;

public class Commandsetworth extends EssentialsCommand
{
    public Commandsetworth() {
        super("setworth");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        final ItemStack stack = this.ess.getItemDb().get(args[0]);
        this.ess.getWorth().setPrice(stack, Double.parseDouble(args[1]));
        user.sendMessage(Util.i18n("worthSet"));
    }
}
