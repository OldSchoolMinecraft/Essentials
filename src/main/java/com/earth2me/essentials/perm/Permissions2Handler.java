package com.earth2me.essentials.perm;

import com.nijiko.permissions.*;
import org.bukkit.plugin.*;
import com.nijikokun.bukkit.Permissions.*;
import org.bukkit.entity.*;
import java.util.*;

public class Permissions2Handler implements IPermissionsHandler
{
    private final transient PermissionHandler permissionHandler;
    
    public Permissions2Handler(final Plugin permissionsPlugin) {
        this.permissionHandler = ((Permissions)permissionsPlugin).getHandler();
    }
    
    @Override
    public String getGroup(final Player base) {
        return this.permissionHandler.getGroup(base.getWorld().getName(), base.getName());
    }
    
    @Override
    public List<String> getGroups(final Player base) {
        return Arrays.asList(this.permissionHandler.getGroups(base.getWorld().getName(), base.getName()));
    }
    
    @Override
    public boolean canBuild(final Player base, final String group) {
        return this.permissionHandler.canGroupBuild(base.getWorld().getName(), this.getGroup(base));
    }
    
    @Override
    public boolean inGroup(final Player base, final String group) {
        return this.permissionHandler.inGroup(base.getWorld().getName(), base.getName(), group);
    }
    
    @Override
    public boolean hasPermission(final Player base, final String node) {
        return this.permissionHandler.permission(base, node);
    }
    
    @Override
    public String getPrefix(final Player base) {
        return this.permissionHandler.getGroupPrefix(base.getWorld().getName(), this.getGroup(base));
    }
    
    @Override
    public String getSuffix(final Player base) {
        return this.permissionHandler.getGroupSuffix(base.getWorld().getName(), this.getGroup(base));
    }
}
