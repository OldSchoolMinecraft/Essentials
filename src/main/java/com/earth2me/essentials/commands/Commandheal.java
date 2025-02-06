package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;
import org.bukkit.entity.*;
import java.util.*;

public class Commandheal extends EssentialsCommand
{
    public Commandheal() {
        super("heal");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length > 0 && user.isAuthorized("essentials.heal.others")) {
            if (!user.isAuthorized("essentials.heal.cooldown.bypass")) {
                user.healCooldown();
            }
            this.healOtherPlayers(server, (CommandSender)user, args[0]);
            return;
        }
        if (!user.isAuthorized("essentials.heal.cooldown.bypass")) {
            user.healCooldown();
        }
        user.setHealth(20);
        user.sendMessage(Util.i18n("heal"));
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.healOtherPlayers(server, sender, args[0]);
    }
    
    private void healOtherPlayers(final Server server, final CommandSender sender, final String name) {
        final List<Player> players = (List<Player>)server.matchPlayer(name);
        if (players.isEmpty()) {
            sender.sendMessage(Util.i18n("playerNotFound"));
            return;
        }
        for (final Player p : players) {
            if (this.ess.getUser(p).isHidden()) {
                continue;
            }
            p.setHealth(20);
            sender.sendMessage(Util.format("healOther", p.getDisplayName()));
        }
    }
}
