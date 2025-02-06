package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;

public class Commandtpaall extends EssentialsCommand
{
    public Commandtpaall() {
        super("tpaall");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length >= 1) {
            final User p = this.getPlayer(server, args, 0);
            this.teleportAAllPlayers(server, sender, p);
            return;
        }
        if (sender instanceof Player) {
            this.teleportAAllPlayers(server, sender, this.ess.getUser(sender));
            return;
        }
        throw new NotEnoughArgumentsException();
    }
    
    private void teleportAAllPlayers(final Server server, final CommandSender sender, final User p) {
        sender.sendMessage(Util.i18n("teleportAAll"));
        for (final Player player : server.getOnlinePlayers()) {
            final User u = this.ess.getUser(player);
            if (p != u) {
                if (u.isTeleportEnabled()) {
                    try {
                        u.requestTeleport(p, true);
                        u.sendMessage(Util.format("teleportHereRequest", p.getDisplayName()));
                        u.sendMessage(Util.i18n("typeTpaccept"));
                    }
                    catch (Exception ex) {
                        this.ess.showError(sender, ex, this.getName());
                    }
                }
            }
        }
    }
}
