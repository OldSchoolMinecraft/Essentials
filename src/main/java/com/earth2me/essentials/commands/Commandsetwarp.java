package com.earth2me.essentials.commands;

import com.earth2me.essentials.*;
import org.bukkit.*;

public class Commandsetwarp extends EssentialsCommand
{
    public Commandsetwarp() {
        super("setwarp");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final Location loc = user.getLocation();
        this.ess.getWarps().setWarp(args[0], loc);
        user.sendMessage(Util.format("warpSet", args[0]));
    }
}
