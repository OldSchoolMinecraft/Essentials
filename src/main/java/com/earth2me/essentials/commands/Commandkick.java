package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;

public class Commandkick extends EssentialsCommand
{
    public Commandkick() {
        super("kick");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final User player = this.getPlayer(server, args, 0);
        if (player.isAuthorized("essentials.kick.exempt")) {
            throw new Exception(Util.i18n("kickExempt"));
        }
        final String kickReason = (args.length > 1) ? EssentialsCommand.getFinalArg(args, 1) : Util.i18n("kickDefault");
        player.kickPlayer(kickReason);
        final String senderName = (sender instanceof Player) ? ((Player)sender).getDisplayName() : "Console";
        for (final Player p : server.getOnlinePlayers()) {
            final User u = this.ess.getUser(p);
            if (u.isAuthorized("essentials.kick.notify")) {
                p.sendMessage(Util.format("playerKicked", senderName, player.getName(), kickReason));
            }
        }
    }
}
