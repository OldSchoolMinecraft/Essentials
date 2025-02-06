package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.entity.*;

public class Commandtpaccept extends EssentialsCommand
{
    public Commandtpaccept() {
        super("tpaccept");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final User p = user.getTeleportRequest();
        if (p == null) {
            throw new Exception(Util.i18n("noPendingRequest"));
        }
        final Trade charge = new Trade(this.getName(), this.ess);
        if (user.isTeleportRequestHere()) {
            charge.isAffordableFor(user);
        }
        else {
            charge.isAffordableFor(p);
        }
        user.sendMessage(Util.i18n("requestAccepted"));
        p.sendMessage(Util.format("requestAcceptedFrom", user.getDisplayName()));
        if (user.isTeleportRequestHere()) {
            user.getTeleport().teleport((Entity)p, charge);
        }
        else {
            p.getTeleport().teleport((Entity)user, charge);
        }
        user.requestTeleport(null, false);
    }
}
