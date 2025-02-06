package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public interface IEssentialsCommand
{
    String getName();
    
    void run(final Server p0, final User p1, final String p2, final Command p3, final String[] p4) throws Exception;
    
    void run(final Server p0, final CommandSender p1, final String p2, final Command p3, final String[] p4) throws Exception;
    
    void setEssentials(final IEssentials p0);
}
