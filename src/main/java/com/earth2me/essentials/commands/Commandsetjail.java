package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandsetjail extends EssentialsCommand
{
    public Commandsetjail() {
        super("setjail");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.ess.getJail().setJail(user.getLocation(), args[0]);
        user.sendMessage(Util.format("jailSet", args[0]));
    }
}
