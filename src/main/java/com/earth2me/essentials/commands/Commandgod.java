package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;
import org.bukkit.entity.*;
import java.util.*;

public class Commandgod extends EssentialsCommand
{
    public Commandgod() {
        super("god");
    }
    
    @Override
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.godOtherPlayers(server, sender, args[0]);
    }
    
    @Override
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length > 0 && user.isAuthorized("essentials.god.others")) {
            this.godOtherPlayers(server, (CommandSender)user, args[0]);
            return;
        }
        user.sendMessage(Util.format("godMode", user.toggleGodModeEnabled() ? Util.i18n("enabled") : Util.i18n("disabled")));
    }
    
    private void godOtherPlayers(final Server server, final CommandSender sender, final String name) {
        for (final Player p : server.matchPlayer(name)) {
            final User u = this.ess.getUser(p);
            if (u.isHidden()) {
                continue;
            }
            final boolean enabled = u.toggleGodModeEnabled();
            u.sendMessage(Util.format("godMode", enabled ? Util.i18n("enabled") : Util.i18n("disabled")));
            sender.sendMessage(Util.format("godMode", Util.format(enabled ? "godEnabledFor" : "godDisabledFor", p.getDisplayName())));
        }
    }
}
