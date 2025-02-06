package com.earth2me.essentials.perm;

import de.bananaco.permissions.worlds.*;
import de.bananaco.permissions.*;
import org.bukkit.entity.*;
import java.util.*;
import de.bananaco.permissions.interfaces.*;

public class BPermissionsHandler extends SuperpermsHandler
{
    private final transient WorldPermissionsManager wpm;
    
    public BPermissionsHandler() {
        this.wpm = Permissions.getWorldPermissionsManager();
    }
    
    @Override
    public String getGroup(final Player base) {
        final List<String> groups = this.getGroups(base);
        if (groups == null || groups.isEmpty()) {
            return null;
        }
        return groups.get(0);
    }
    
    @Override
    public List<String> getGroups(final Player base) {
        final PermissionSet pset = this.wpm.getPermissionSet(base.getWorld());
        if (pset == null) {
            return null;
        }
        return (List<String>)pset.getGroups(base);
    }
    
    @Override
    public boolean inGroup(final Player base, final String group) {
        final List<String> groups = this.getGroups(base);
        return groups != null && !groups.isEmpty() && groups.contains(group);
    }
}
