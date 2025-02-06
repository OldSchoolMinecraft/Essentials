package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandtppos extends EssentialsCommand
{
    public Commandtppos() {
        super("tppos");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 3) {
            throw new NotEnoughArgumentsException();
        }
        final int x = Integer.parseInt(args[0]);
        final int y = Integer.parseInt(args[1]);
        final int z = Integer.parseInt(args[2]);
        final Location l = new Location(user.getWorld(), (double)x, (double)y, (double)z);
        final Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        user.sendMessage(Util.i18n("teleporting"));
        user.getTeleport().teleport(l, charge);
        throw new NoChargeException();
    }
}
