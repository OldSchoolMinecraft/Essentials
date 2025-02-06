package com.earth2me.essentials.perm;

import ru.tehkode.permissions.bukkit.*;
import org.bukkit.entity.*;
import ru.tehkode.permissions.*;
import java.util.*;

public class PermissionsExHandler implements IPermissionsHandler
{
    private final transient PermissionManager manager;
    
    public PermissionsExHandler() {
        this.manager = PermissionsEx.getPermissionManager();
    }
    
    @Override
    public String getGroup(final Player base) {
        final PermissionUser user = this.manager.getUser(base.getName());
        if (user == null) {
            return null;
        }
        return user.getGroupsNames()[0];
    }
    
    @Override
    public List<String> getGroups(final Player base) {
        final PermissionUser user = this.manager.getUser(base.getName());
        if (user == null) {
            return null;
        }
        return Arrays.asList(user.getGroupsNames());
    }
    
    @Override
    public boolean canBuild(final Player base, final String group) {
        final PermissionUser user = this.manager.getUser(base.getName());
        return user == null || user.getOptionBoolean("build", base.getWorld().getName(), true);
    }
    
    @Override
    public boolean inGroup(final Player base, final String group) {
        final PermissionUser user = this.manager.getUser(base.getName());
        return user != null && user.inGroup(group);
    }
    
    @Override
    public boolean hasPermission(final Player base, final String node) {
        return this.manager.has(base.getName(), node, base.getWorld().getName());
    }
    
    @Override
    public String getPrefix(final Player base) {
        final PermissionUser user = this.manager.getUser(base.getName());
        if (user == null) {
            return null;
        }
        return user.getPrefix();
    }
    
    @Override
    public String getSuffix(final Player base) {
        final PermissionUser user = this.manager.getUser(base.getName());
        if (user == null) {
            return null;
        }
        return user.getSuffix();
    }
}
