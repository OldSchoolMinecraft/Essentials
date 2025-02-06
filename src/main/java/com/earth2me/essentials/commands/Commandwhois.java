package com.earth2me.essentials.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandwhois extends EssentialsCommand
{
    public Commandwhois() {
        super("whois");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        boolean showhidden = false;
        if (sender instanceof Player) {
            if (this.ess.getUser(sender).isAuthorized("essentials.list.hidden")) {
                showhidden = true;
            }
        }
        else {
            showhidden = true;
        }
        final String whois = args[0].toLowerCase();
        final int prefixLength = ChatColor.stripColor(this.ess.getSettings().getNicknamePrefix()).length();
        for (final Player p : server.getOnlinePlayers()) {
            final User u = this.ess.getUser(p);
            if (!u.isHidden() || showhidden) {
                final String dn = ChatColor.stripColor(u.getNick());
                if (whois.equalsIgnoreCase(dn) || whois.equalsIgnoreCase(dn.substring(prefixLength)) || whois.equalsIgnoreCase(u.getName())) {
                    sender.sendMessage("");
                    sender.sendMessage(Util.format("whoisIs", u.getDisplayName(), u.getName()));
                    sender.sendMessage(Util.format("whoisHealth", u.getHealth()));
                    sender.sendMessage(Util.format("whoisLocation", u.getLocation().getWorld().getName(), u.getLocation().getBlockX(), u.getLocation().getBlockY(), u.getLocation().getBlockZ()));
                    if (!this.ess.getSettings().isEcoDisabled()) {
                        sender.sendMessage(Util.format("whoisMoney", Util.formatCurrency(u.getMoney(), this.ess)));
                    }
                    sender.sendMessage(u.isAfk() ? Util.i18n("whoisStatusAway") : Util.i18n("whoisStatusAvailable"));
                    sender.sendMessage(Util.format("whoisIPAddress", u.getAddress().getAddress().toString()));
                    final String location = u.getGeoLocation();
                    if (location != null && (!(sender instanceof Player) || this.ess.getUser(sender).isAuthorized("essentials.geoip.show"))) {
                        sender.sendMessage(Util.format("whoisGeoLocation", location));
                    }
                }
            }
        }
    }
}
