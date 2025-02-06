package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandsuicide extends EssentialsCommand
{
    public Commandsuicide() {
        super("suicide");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        user.setHealth(0);
        user.sendMessage(Util.i18n("suicideMessage"));
        this.ess.broadcastMessage(user, Util.format("suicideSuccess", user.getDisplayName()));
    }
}
