package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandtpdeny extends EssentialsCommand
{
    public Commandtpdeny() {
        super("tpdeny");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final User p = user.getTeleportRequest();
        if (p == null) {
            throw new Exception(Util.i18n("noPendingRequest"));
        }
        user.sendMessage(Util.i18n("requestDenied"));
        p.sendMessage(Util.format("requestDeniedFrom", user.getDisplayName()));
        user.requestTeleport(null, false);
    }
}
