package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;

public class Commandtogglejail extends EssentialsCommand
{
    public Commandtogglejail() {
        super("togglejail");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final User p = this.getPlayer(server, args, 0, true);
        if (args.length >= 2 && !p.isJailed()) {
            if (p.getBase() instanceof OfflinePlayer) {
                if (sender instanceof Player && !this.ess.getUser(sender).isAuthorized("essentials.togglejail.offline")) {
                    sender.sendMessage(Util.i18n("mayNotJail"));
                    return;
                }
            }
            else if (p.isAuthorized("essentials.jail.exempt")) {
                sender.sendMessage(Util.i18n("mayNotJail"));
                return;
            }
            if (!(p.getBase() instanceof OfflinePlayer)) {
                this.ess.getJail().sendToJail(p, args[1]);
            }
            else {
                this.ess.getJail().getJail(args[1]);
            }
            p.setJailed(true);
            p.sendMessage(Util.i18n("userJailed"));
            p.setJail(null);
            p.setJail(args[1]);
            long timeDiff = 0L;
            if (args.length > 2) {
                final String time = EssentialsCommand.getFinalArg(args, 2);
                timeDiff = Util.parseDateDiff(time, true);
                p.setJailTimeout(timeDiff);
            }
            sender.sendMessage((timeDiff > 0L) ? Util.format("playerJailedFor", p.getName(), Util.formatDateDiff(timeDiff)) : Util.format("playerJailed", p.getName()));
            return;
        }
        if (args.length >= 2 && p.isJailed() && !args[1].equalsIgnoreCase(p.getJail())) {
            sender.sendMessage("§cPerson is already in jail " + p.getJail());
            return;
        }
        if (args.length >= 2 && p.isJailed() && args[1].equalsIgnoreCase(p.getJail())) {
            final String time2 = EssentialsCommand.getFinalArg(args, 2);
            final long timeDiff2 = Util.parseDateDiff(time2, true);
            p.setJailTimeout(timeDiff2);
            sender.sendMessage("Jail time extend to " + Util.formatDateDiff(timeDiff2));
            return;
        }
        if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase(p.getJail()))) {
            if (!p.isJailed()) {
                throw new NotEnoughArgumentsException();
            }
            p.setJailed(false);
            p.setJailTimeout(0L);
            p.sendMessage("§7You have been released");
            p.setJail(null);
            if (!(p.getBase() instanceof OfflinePlayer)) {
                p.getTeleport().back();
            }
            sender.sendMessage("§7Player " + p.getName() + " unjailed.");
        }
    }
}
