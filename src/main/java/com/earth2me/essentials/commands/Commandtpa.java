package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandtpa extends EssentialsCommand
{
    public Commandtpa() {
        super("tpa");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final User p = this.getPlayer(server, args, 0);
        if (!p.isTeleportEnabled()) {
            throw new Exception(Util.format("teleportDisabled", p.getDisplayName()));
        }
        if (!p.isIgnoredPlayer(user.getName())) {
            p.requestTeleport(user, false);
            p.sendMessage(Util.format("teleportRequest", user.getDisplayName()));
            p.sendMessage(Util.i18n("typeTpaccept"));
            p.sendMessage(Util.i18n("typeTpdeny"));
        }
        user.sendMessage(Util.format("requestSent", p.getDisplayName()));
    }
}
