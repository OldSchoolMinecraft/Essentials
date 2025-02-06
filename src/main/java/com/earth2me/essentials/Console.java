package com.earth2me.essentials;

import org.bukkit.command.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.*;

public final class Console implements IReplyTo
{
    private static Console instance;
    private CommandSender replyTo;
    public static final String NAME = "Console";
    
    private Console() {
    }
    
    public static CommandSender getCommandSender(final Server server) throws Exception {
        if (!(server instanceof CraftServer)) {
            throw new Exception(Util.i18n("invalidServer"));
        }
        return (CommandSender)((CraftServer)server).getServer().console;
    }
    
    @Override
    public void setReplyTo(final CommandSender user) {
        this.replyTo = user;
    }
    
    @Override
    public CommandSender getReplyTo() {
        return this.replyTo;
    }
    
    public static Console getConsoleReplyTo() {
        return Console.instance;
    }
    
    static {
        Console.instance = new Console();
    }
}
