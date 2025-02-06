package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;

public class Commandr extends EssentialsCommand
{
    public Commandr() {
        super("r");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final String message = EssentialsCommand.getFinalArg(args, 0);
        final IReplyTo replyTo = (sender instanceof Player) ? this.ess.getUser(sender) : Console.getConsoleReplyTo();
        final String senderName = (sender instanceof Player) ? ((Player)sender).getDisplayName() : "Console";
        final CommandSender target = replyTo.getReplyTo();
        final String targetName = (target instanceof Player) ? ((Player)target).getDisplayName() : "Console";
        if (target == null) {
            throw new Exception(Util.i18n("foreverAlone"));
        }
        sender.sendMessage(Util.format("msgFormat", Util.i18n("me"), targetName, message));
        if (target instanceof Player) {
            final User u = this.ess.getUser(target);
            if (u.isIgnoredPlayer((sender instanceof Player) ? ((Player)sender).getName() : "Console")) {
                return;
            }
        }
        target.sendMessage(Util.format("msgFormat", senderName, Util.i18n("me"), message));
        replyTo.setReplyTo(target);
        if (target != sender) {
            if (target instanceof Player) {
                this.ess.getUser(target).setReplyTo(sender);
            }
            else {
                Console.getConsoleReplyTo().setReplyTo(sender);
            }
        }
    }
}
