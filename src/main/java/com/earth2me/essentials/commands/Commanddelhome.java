package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commanddelhome extends EssentialsCommand
{
    public Commanddelhome() {
        super("delhome");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, String[] args) throws Exception {
        final String[] nameParts = args[0].split(":");
        if (nameParts[0].length() != args[0].length()) {
            args = nameParts;
        }
        User user = this.ess.getUser(sender);
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        String name;
        if (args.length > 1 && (user == null || user.isAuthorized("essentials.delhome.others"))) {
            user = this.getPlayer(server, args, 0);
            name = args[1];
        }
        else {
            if (user == null) {
                throw new NotEnoughArgumentsException();
            }
            name = args[0];
        }
        user.delHome(name.toLowerCase());
        sender.sendMessage(Util.format("deleteHome", name));
    }
}
