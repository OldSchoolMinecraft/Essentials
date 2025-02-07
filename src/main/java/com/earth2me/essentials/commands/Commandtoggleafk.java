package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import org.bukkit.ChatColor;
import org.bukkit.Server;

public class Commandtoggleafk extends EssentialsCommand
{
    public Commandtoggleafk()
    {
        super("toggleafk");
    }

    public void run(final Server server, final User user, final String commandLabel, final String[] args)
    {
        user.setAfkDetectionOn(!user.isAFKDetectionOn());
        user.sendMessage(ChatColor.GRAY + "AFK detection is now " + (user.isAFKDetectionOn() ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));
    }
}
