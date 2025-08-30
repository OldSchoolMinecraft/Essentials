package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandback extends EssentialsCommand
{
    public Commandback() {
        super("back");
    }
    
    @Override
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        user.sendMessage(Util.i18n("backUsageMsg"));
        user.getTeleport().back(charge);
    }
}
