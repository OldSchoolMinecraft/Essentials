package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandping extends EssentialsCommand
{
    public Commandping() {
        super("ping");
    }
    
    public void run(final Server server, final User player, final String commandLabel, final String[] args) throws Exception {
        player.sendMessage(Util.i18n("pong"));
    }
}
