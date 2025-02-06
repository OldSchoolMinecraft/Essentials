package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class Commandnick extends EssentialsCommand
{
    public Commandnick() {
        super("nick");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        if (!this.ess.getSettings().changeDisplayName()) {
            throw new Exception(Util.i18n("nickDisplayName"));
        }
        if (args.length > 1) {
            if (!user.isAuthorized("essentials.nick.others")) {
                throw new Exception(Util.i18n("nickOthersPermission"));
            }
            this.setOthersNickname(server, (CommandSender)user, args);
        }
        else {
            final String nick = args[0];
            if ("off".equalsIgnoreCase(nick) || user.getName().equalsIgnoreCase(nick)) {
                user.setDisplayName(user.getName());
                user.setNickname(null);
                user.sendMessage(Util.i18n("nickNoMore"));
                return;
            }
            if (nick.matches("[^a-zA-Z_0-9]")) {
                throw new Exception(Util.i18n("nickNamesAlpha"));
            }
            for (final Player p : server.getOnlinePlayers()) {
                if (user != p) {
                    final String dn = p.getDisplayName().toLowerCase();
                    final String n = p.getName().toLowerCase();
                    final String nk = nick.toLowerCase();
                    if (nk.equals(dn) || nk.equals(n)) {
                        throw new Exception(Util.i18n("nickInUse"));
                    }
                }
            }
            user.setDisplayName(this.ess.getSettings().getNicknamePrefix() + nick);
            user.setNickname(nick);
            user.sendMessage(Util.format("nickSet", user.getDisplayName() + "§7."));
        }
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        if (!this.ess.getSettings().changeDisplayName()) {
            sender.sendMessage(Util.i18n("nickDisplayName"));
            return;
        }
        this.setOthersNickname(server, sender, args);
    }
    
    private void setOthersNickname(final Server server, final CommandSender sender, final String[] args) throws Exception {
        final User target = this.getPlayer(server, args, 0);
        final String nick = args[1];
        if ("off".equalsIgnoreCase(nick) || target.getName().equalsIgnoreCase(nick)) {
            target.setDisplayName(target.getName());
            target.setNickname(null);
            target.sendMessage(Util.i18n("nickNoMore"));
        }
        else {
            target.setDisplayName(this.ess.getSettings().getNicknamePrefix() + nick);
            target.setNickname(nick);
            target.sendMessage(Util.format("nickSet", target.getDisplayName() + "§7."));
        }
        sender.sendMessage(Util.i18n("nickChanged"));
    }
}
