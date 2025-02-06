package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;
import org.bukkit.entity.*;
import java.util.*;

public class Commandext extends EssentialsCommand
{
    public Commandext() {
        super("ext");
    }
    
    @Override
    protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.extinguishPlayers(server, sender, args[0]);
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            user.setFireTicks(0);
            user.sendMessage(Util.i18n("extinguish"));
            return;
        }
        this.extinguishPlayers(server, (CommandSender)user, commandLabel);
    }
    
    private void extinguishPlayers(final Server server, final CommandSender sender, final String name) throws Exception {
        for (final Player p : server.matchPlayer(name)) {
            p.setFireTicks(0);
            sender.sendMessage(Util.format("extinguishOthers", p.getDisplayName()));
        }
    }
}
