package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandsocialspy extends EssentialsCommand
{
    public Commandsocialspy() {
        super("socialspy");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        user.sendMessage("§7SocialSpy " + (user.toggleSocialSpy() ? Util.i18n("enabled") : Util.i18n("disabled")));
    }
}
