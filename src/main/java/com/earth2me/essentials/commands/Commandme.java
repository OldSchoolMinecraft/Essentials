package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandme extends EssentialsCommand
{
    public Commandme() {
        super("me");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (user.isMuted()) {
            throw new Exception(Util.i18n("voiceSilenced"));
        }
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final StringBuilder message = new StringBuilder();
        message.append("* ");
        message.append(user.getDisplayName());
        message.append(' ');
        for (int i = 0; i < args.length; ++i) {
            message.append(args[i]);
            message.append(' ');
        }
        this.ess.broadcastMessage(user, message.toString());
    }
}
