package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandmsg extends EssentialsCommand
{
    public Commandmsg() {
        super("msg");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 2 || args[0].trim().isEmpty() || args[1].trim().isEmpty()) {
            throw new NotEnoughArgumentsException();
        }
        if (sender instanceof Player) {
            final User user = this.ess.getUser(sender);
            if (user.isMuted()) {
                throw new Exception(Util.i18n("voiceSilenced"));
            }
        }
        final String message = EssentialsCommand.getFinalArg(args, 1);
        final String translatedMe = Util.i18n("me");
        final IReplyTo replyTo = (sender instanceof Player) ? this.ess.getUser(sender) : Console.getConsoleReplyTo();
        final String senderName = (sender instanceof Player) ? ((Player)sender).getDisplayName() : "Console";
        if (args[0].equalsIgnoreCase("Console")) {
            sender.sendMessage(Util.format("msgFormat", translatedMe, "Console", message));
            final CommandSender cs = Console.getCommandSender(server);
            cs.sendMessage(Util.format("msgFormat", senderName, translatedMe, message));
            replyTo.setReplyTo(cs);
            Console.getConsoleReplyTo().setReplyTo(sender);
            return;
        }
        final List<Player> matches = (List<Player>)server.matchPlayer(args[0]);
        if (matches.isEmpty()) {
            throw new Exception(Util.i18n("playerNotFound"));
        }
        int i = 0;
        for (final Player p : matches) {
            final User u = this.ess.getUser(p);
            if (u.isHidden()) {
                ++i;
            }
        }
        if (i == matches.size()) {
            throw new Exception(Util.i18n("playerNotFound"));
        }
        for (final Player p : matches) {
            sender.sendMessage(Util.format("msgFormat", translatedMe, p.getDisplayName(), message));
            final User u = this.ess.getUser(p);
            if (sender instanceof Player) {
                if (u.isIgnoredPlayer(((Player)sender).getName())) {
                    continue;
                }
                if (u.isHidden()) {
                    continue;
                }
            }
            p.sendMessage(Util.format("msgFormat", senderName, translatedMe, message));
            replyTo.setReplyTo((CommandSender)this.ess.getUser(p));
            this.ess.getUser(p).setReplyTo(sender);
        }
    }
}
