package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.entity.*;

public class Commandrealname extends EssentialsCommand
{
    public Commandrealname() {
        super("realname");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final String whois = args[0].toLowerCase();
        for (final Player p : server.getOnlinePlayers()) {
            final User u = this.ess.getUser(p);
            if (!u.isHidden()) {
                final String displayName = ChatColor.stripColor(u.getDisplayName()).toLowerCase();
                if (whois.equals(displayName) || displayName.equals(ChatColor.stripColor(this.ess.getSettings().getNicknamePrefix()) + whois) || whois.equalsIgnoreCase(u.getName())) {
                    user.sendMessage(u.getDisplayName() + " " + Util.i18n("is") + " " + u.getName());
                }
            }
        }
    }
}
