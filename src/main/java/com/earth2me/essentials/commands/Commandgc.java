package com.earth2me.essentials.commands;

import org.bukkit.command.*;
import com.earth2me.essentials.*;
import org.bukkit.*;
import java.util.*;

public class Commandgc extends EssentialsCommand
{
    public Commandgc() {
        super("gc");
    }
    
    @Override
    protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        sender.sendMessage(Util.format("gcmax", Runtime.getRuntime().maxMemory() / 1024L / 1024L));
        sender.sendMessage(Util.format("gcfree", Runtime.getRuntime().freeMemory() / 1024L / 1024L));
        sender.sendMessage(Util.format("gctotal", Runtime.getRuntime().totalMemory() / 1024L / 1024L));
        for (final World w : server.getWorlds()) {
            sender.sendMessage(((w.getEnvironment() == World.Environment.NETHER) ? "Nether" : "World") + " \"" + w.getName() + "\": " + w.getLoadedChunks().length + Util.i18n("gcchunks") + w.getEntities().size() + Util.i18n("gcentities"));
        }
    }
}
