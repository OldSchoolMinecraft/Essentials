package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;

public class Commandtempban extends EssentialsCommand
{
    public Commandtempban() {
        super("tempban");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        final User player = this.getPlayer(server, args, 0, true);
        if (player.getBase() instanceof OfflinePlayer) {
            if (sender instanceof Player && !this.ess.getUser(sender).isAuthorized("essentials.tempban.offline")) {
                sender.sendMessage(Util.i18n("tempbanExempt"));
                return;
            }
        }
        else if (player.isAuthorized("essentials.tempban.exempt")) {
            sender.sendMessage(Util.i18n("tempbanExempt"));
            return;
        }
        final String time = EssentialsCommand.getFinalArg(args, 1);
        final long banTimestamp = Util.parseDateDiff(time, true);
        final String banReason = Util.format("tempBanned", Util.formatDateDiff(banTimestamp));
        player.setBanReason(banReason);
        player.setBanTimeout(banTimestamp);
        player.kickPlayer(banReason);
        this.ess.getBans().banByName(player.getName());
        final String senderName = (sender instanceof Player) ? ((Player)sender).getDisplayName() : "Console";
        for (final Player p : server.getOnlinePlayers()) {
            final User u = this.ess.getUser(p);
            if (u.isAuthorized("essentials.ban.notify")) {
                p.sendMessage(Util.format("playerBanned", senderName, player.getName(), banReason));
            }
        }
    }
}
