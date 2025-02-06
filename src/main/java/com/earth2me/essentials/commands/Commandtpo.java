package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.*;

public class Commandtpo extends EssentialsCommand
{
    public Commandtpo() {
        super("tpo");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final User p = this.getPlayer(server, args, 0, true);
        if (p.getBase() instanceof OfflinePlayer) {
            throw new NoSuchFieldException(Util.i18n("playerNotFound"));
        }
        if (!p.isHidden() || user.isAuthorized("essentials.teleport.hidden")) {
            user.getTeleport().now((Entity)p, false);
            user.sendMessage(Util.i18n("teleporting"));
            return;
        }
        throw new NoSuchFieldException(Util.i18n("playerNotFound"));
    }
}
