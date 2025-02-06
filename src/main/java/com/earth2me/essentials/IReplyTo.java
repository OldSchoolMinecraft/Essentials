package com.earth2me.essentials;

import org.bukkit.command.*;

public interface IReplyTo
{
    void setReplyTo(final CommandSender p0);
    
    CommandSender getReplyTo();
}
