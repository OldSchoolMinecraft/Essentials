package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandafk extends EssentialsCommand
{
    public Commandafk() {
        super("afk");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length > 0 && user.isAuthorized("essentials.afk.others")) {
            final User afkUser = this.ess.getUser(this.ess.getServer().matchPlayer(args[0]));
            if (afkUser != null) {
                this.toggleAfk(afkUser);
            }
        }
        else {
            this.toggleAfk(user);
        }
    }
    
    private final void toggleAfk(final User user) {
        if (!user.toggleAfk()) {
            if (!user.isHidden()) {
                this.ess.broadcastMessage(user, Util.format("userIsNotAway", user.getDisplayName()));
            }
            user.updateActivity(false);
        }
        else if (!user.isHidden()) {
            this.ess.broadcastMessage(user, Util.format("userIsAway", user.getDisplayName()));
        }
    }
}
