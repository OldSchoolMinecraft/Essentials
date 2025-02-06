package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.entity.*;

public class Commandtphere extends EssentialsCommand
{
    public Commandtphere() {
        super("tphere");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final User p = this.getPlayer(server, args, 0);
        if (!p.isTeleportEnabled()) {
            throw new Exception(Util.format("teleportDisabled", p.getDisplayName()));
        }
        p.getTeleport().teleport((Entity)user, new Trade(this.getName(), this.ess));
        user.sendMessage(Util.i18n("teleporting"));
        p.sendMessage(Util.i18n("teleporting"));
        throw new NoChargeException();
    }
}
