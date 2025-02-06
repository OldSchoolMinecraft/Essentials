package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandlightning extends EssentialsCommand
{
    public Commandlightning() {
        super("lightning");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        User user = null;
        if (sender instanceof Player) {
            user = this.ess.getUser(sender);
        }
        if (args.length < 1 & user != null) {
            user.getWorld().strikeLightning(user.getTargetBlock(null, 600).getLocation());
            return;
        }
        if (server.matchPlayer(args[0]).isEmpty()) {
            throw new Exception(Util.i18n("playerNotFound"));
        }
        for (final Player p : server.matchPlayer(args[0])) {
            sender.sendMessage(Util.format("lightningUse", p.getDisplayName()));
            p.getWorld().strikeLightning(p.getLocation());
            if (!this.ess.getUser(p).isGodModeEnabled()) {
                p.setHealth((p.getHealth() < 5) ? 0 : (p.getHealth() - 5));
            }
            if (this.ess.getSettings().warnOnSmite()) {
                p.sendMessage(Util.i18n("lightningSmited"));
            }
        }
    }
}
