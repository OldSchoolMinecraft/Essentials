package com.earth2me.essentials.commands;

import com.earth2me.essentials.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.entity.*;

public class Commandmail extends EssentialsCommand
{
    public Commandmail() {
        super("mail");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length >= 1 && "read".equalsIgnoreCase(args[0])) {
            final List<String> mail = user.getMails();
            if (mail.isEmpty()) {
                throw new Exception(Util.i18n("noMail"));
            }
            for (final String s : mail) {
                user.sendMessage(s);
            }
            throw new Exception(Util.i18n("mailClear"));
        }
        else if (args.length >= 3 && "send".equalsIgnoreCase(args[0])) {
            if (!user.isAuthorized("essentials.mail.send")) {
                throw new Exception(Util.i18n("noMailSendPerm"));
            }
            final Player player = server.getPlayer(args[1]);
            User u;
            if (player != null) {
                u = this.ess.getUser(player);
            }
            else {
                u = this.ess.getOfflineUser(args[1]);
            }
            if (u == null) {
                throw new Exception(Util.format("playerNeverOnServer", args[1]));
            }
            if (!u.isIgnoredPlayer(user.getName())) {
                u.addMail(ChatColor.stripColor(user.getDisplayName()) + ": " + EssentialsCommand.getFinalArg(args, 2));
            }
            user.sendMessage(Util.i18n("mailSent"));
        }
        else {
            if (args.length >= 1 && "clear".equalsIgnoreCase(args[0])) {
                user.setMails(null);
                throw new Exception(Util.i18n("mailCleared"));
            }
            throw new NotEnoughArgumentsException();
        }
    }
}
