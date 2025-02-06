package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;

public class Commandban extends EssentialsCommand
{
    public Commandban() {
        super("ban");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final User player = this.getPlayer(server, args, 0, true);
        if (player.getBase() instanceof OfflinePlayer) {
            if (sender instanceof Player && !this.ess.getUser(sender).isAuthorized("essentials.ban.offline")) {
                sender.sendMessage(Util.i18n("banExempt"));
                return;
            }
        }
        else if (player.isAuthorized("essentials.ban.exempt")) {
            sender.sendMessage(Util.i18n("banExempt"));
            return;
        }
        String banReason;
        if (args.length > 1) {
            banReason = EssentialsCommand.getFinalArg(args, 1);
            player.setBanReason(commandLabel);
        }
        else {
            banReason = Util.i18n("defaultBanReason");
        }
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
