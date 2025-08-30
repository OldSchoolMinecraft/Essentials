package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandignore extends EssentialsCommand
{
    public Commandignore() {
        super("ignore");
    }
    
    @Override
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        User u;
        try {
            u = this.getPlayer(server, args, 0);
        }
        catch (NoSuchFieldException ex) {
            u = this.ess.getOfflineUser(args[0]);
        }
        if (u == null) {
            throw new Exception(Util.i18n("playerNotFound"));
        }
        final String name = u.getName();
        if (user.isIgnoredPlayer(name)) {
            user.setIgnoredPlayer(name, false);
            user.sendMessage(Util.format("unignorePlayer", u.getName()));
        }
        else {
            user.setIgnoredPlayer(name, true);
            user.sendMessage(Util.format("ignorePlayer", u.getName()));
        }
    }
}
