package com.earth2me.essentials.perm;

import org.bukkit.entity.*;
import java.util.*;

public interface IPermissionsHandler
{
    String getGroup(final Player p0);
    
    List<String> getGroups(final Player p0);
    
    boolean canBuild(final Player p0, final String p1);
    
    boolean inGroup(final Player p0, final String p1);
    
    boolean hasPermission(final Player p0, final String p1);
    
    String getPrefix(final Player p0);
    
    String getSuffix(final Player p0);
}
