package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;

public class Commandtp extends EssentialsCommand
{
    public Commandtp() {
        super("tp");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        switch (args.length) {
            case 0: {
                throw new NotEnoughArgumentsException();
            }
            case 1: {
                final User p = this.getPlayer(server, args, 0);
                if (!p.isTeleportEnabled()) {
                    throw new Exception(Util.format("teleportDisabled", p.getDisplayName()));
                }
                user.sendMessage(Util.i18n("teleporting"));
                final Trade charge = new Trade(this.getName(), this.ess);
                charge.isAffordableFor(user);
                user.getTeleport().teleport((Entity)p, charge);
                throw new NoChargeException();
            }
            case 2: {
                if (!user.isAuthorized("essentials.tpohere")) {
                    throw new Exception("You need access to /tpohere to teleport other players.");
                }
                user.sendMessage(Util.i18n("teleporting"));
                final User target = this.getPlayer(server, args, 0);
                final User toPlayer = this.getPlayer(server, args, 1);
                target.getTeleport().now((Entity)toPlayer, false);
                target.sendMessage(Util.format("teleportAtoB", user.getDisplayName(), toPlayer.getDisplayName()));
                break;
            }
        }
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        sender.sendMessage(Util.i18n("teleporting"));
        final User target = this.getPlayer(server, args, 0);
        final User toPlayer = this.getPlayer(server, args, 1);
        target.getTeleport().now((Entity)toPlayer, false);
        target.sendMessage(Util.format("teleportAtoB", "Console", toPlayer.getDisplayName()));
    }
}
