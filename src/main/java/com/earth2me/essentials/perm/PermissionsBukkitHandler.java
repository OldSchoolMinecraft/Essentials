package com.earth2me.essentials.perm;

import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import java.util.*;
import com.platymuus.bukkit.permissions.*;

public class PermissionsBukkitHandler extends SuperpermsHandler
{
    private final transient PermissionsPlugin plugin;
    
    public PermissionsBukkitHandler(final Plugin plugin) {
        this.plugin = (PermissionsPlugin)plugin;
    }
    
    @Override
    public String getGroup(final Player base) {
        final List<Group> groups = this.getPBGroups(base);
        if (groups == null || groups.isEmpty()) {
            return null;
        }
        return groups.get(0).getName();
    }
    
    @Override
    public List<String> getGroups(final Player base) {
        final List<Group> groups = this.getPBGroups(base);
        if (groups.size() == 1) {
            return Collections.singletonList(groups.get(0).getName());
        }
        final List<String> groupNames = new ArrayList<String>(groups.size());
        for (final Group group : groups) {
            groupNames.add(group.getName());
        }
        return groupNames;
    }
    
    private List<Group> getPBGroups(final Player base) {
        final PermissionInfo info = this.plugin.getPlayerInfo(base.getName());
        if (info == null) {
            return Collections.emptyList();
        }
        final List<Group> groups = (List<Group>)info.getGroups();
        if (groups == null || groups.isEmpty()) {
            return Collections.emptyList();
        }
        return groups;
    }
    
    @Override
    public boolean inGroup(final Player base, final String group) {
        final List<Group> groups = this.getPBGroups(base);
        for (final Group group2 : groups) {
            if (group2.getName().equalsIgnoreCase(group)) {
                return true;
            }
        }
        return false;
    }
}
