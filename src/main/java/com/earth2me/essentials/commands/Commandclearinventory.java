package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.*;
import java.util.*;
import org.bukkit.command.*;

public class Commandclearinventory extends EssentialsCommand
{
    public Commandclearinventory() {
        super("clearinventory");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length > 0 && user.isAuthorized("essentials.clearinventory.others")) {
            if (args[0].length() >= 3) {
                final List<Player> online = (List<Player>)server.matchPlayer(args[0]);
                if (!online.isEmpty()) {
                    for (final Player p : online) {
                        p.getInventory().clear();
                        user.sendMessage(Util.format("inventoryClearedOthers", p.getDisplayName()));
                    }
                    return;
                }
                throw new Exception(Util.i18n("playerNotFound"));
            }
            else {
                final Player p2 = server.getPlayer(args[0]);
                if (p2 == null) {
                    throw new Exception(Util.i18n("playerNotFound"));
                }
                p2.getInventory().clear();
                user.sendMessage(Util.format("inventoryClearedOthers", p2.getDisplayName()));
            }
        }
        else {
            user.getInventory().clear();
            user.sendMessage(Util.i18n("inventoryCleared"));
        }
    }
    
    @Override
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        if (args[0].length() >= 3) {
            final List<Player> online = (List<Player>)server.matchPlayer(args[0]);
            if (!online.isEmpty()) {
                for (final Player p : online) {
                    p.getInventory().clear();
                    sender.sendMessage(Util.format("inventoryClearedOthers", p.getDisplayName()));
                }
                return;
            }
            throw new Exception(Util.i18n("playerNotFound"));
        }
        else {
            final Player u = server.getPlayer(args[0]);
            if (u != null) {
                u.getInventory().clear();
                sender.sendMessage(Util.format("inventoryClearedOthers", u.getDisplayName()));
                return;
            }
            throw new Exception(Util.i18n("playerNotFound"));
        }
    }
}
