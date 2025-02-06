package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;
import org.bukkit.entity.*;

public class Commandtpall extends EssentialsCommand
{
    public Commandtpall() {
        super("tpall");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length >= 1) {
            final User p = this.getPlayer(server, args, 0);
            this.teleportAllPlayers(server, sender, p);
            return;
        }
        if (sender instanceof Player) {
            this.teleportAllPlayers(server, sender, this.ess.getUser(sender));
            return;
        }
        throw new NotEnoughArgumentsException();
    }
    
    private void teleportAllPlayers(final Server server, final CommandSender sender, final User p) {
        sender.sendMessage(Util.i18n("teleportAll"));
        for (final Player player : server.getOnlinePlayers()) {
            final User u = this.ess.getUser(player);
            if (p != u) {
                try {
                    u.getTeleport().now((Entity)p, false);
                }
                catch (Exception ex) {
                    this.ess.showError(sender, ex, this.getName());
                }
            }
        }
    }
}
