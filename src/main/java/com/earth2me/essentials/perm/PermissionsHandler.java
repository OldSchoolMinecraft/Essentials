package com.earth2me.essentials.perm;

import org.bukkit.entity.*;
import java.util.*;
import java.util.logging.*;
import org.bukkit.plugin.*;

public class PermissionsHandler implements IPermissionsHandler
{
    private transient IPermissionsHandler handler;
    private transient String defaultGroup;
    private final transient Plugin plugin;
    private static final Logger LOGGER;
    private transient boolean useSuperperms;
    
    public PermissionsHandler(final Plugin plugin) {
        this.handler = new NullPermissionsHandler();
        this.defaultGroup = "default";
        this.useSuperperms = false;
        this.plugin = plugin;
    }
    
    public PermissionsHandler(final Plugin plugin, final boolean useSuperperms) {
        this.handler = new NullPermissionsHandler();
        this.defaultGroup = "default";
        this.useSuperperms = false;
        this.plugin = plugin;
        this.useSuperperms = useSuperperms;
    }
    
    public PermissionsHandler(final Plugin plugin, final String defaultGroup) {
        this.handler = new NullPermissionsHandler();
        this.defaultGroup = "default";
        this.useSuperperms = false;
        this.plugin = plugin;
        this.defaultGroup = defaultGroup;
    }
    
    @Override
    public String getGroup(final Player base) {
        String group = this.handler.getGroup(base);
        if (group == null) {
            group = this.defaultGroup;
        }
        return group;
    }
    
    @Override
    public List<String> getGroups(final Player base) {
        List<String> groups = this.handler.getGroups(base);
        if (groups == null || groups.isEmpty()) {
            groups = Collections.singletonList(this.defaultGroup);
        }
        return Collections.unmodifiableList((List<? extends String>)groups);
    }
    
    @Override
    public boolean canBuild(final Player base, final String group) {
        return this.handler.canBuild(base, group);
    }
    
    @Override
    public boolean inGroup(final Player base, final String group) {
        return this.handler.inGroup(base, group);
    }
    
    @Override
    public boolean hasPermission(final Player base, final String node) {
        return this.handler.hasPermission(base, node);
    }
    
    @Override
    public String getPrefix(final Player base) {
        String prefix = this.handler.getPrefix(base);
        if (prefix == null) {
            prefix = "";
        }
        return prefix;
    }
    
    @Override
    public String getSuffix(final Player base) {
        String suffix = this.handler.getSuffix(base);
        if (suffix == null) {
            suffix = "";
        }
        return suffix;
    }
    
    public void checkPermissions() {
        final PluginManager pluginManager = this.plugin.getServer().getPluginManager();
        final Plugin permExPlugin = pluginManager.getPlugin("PermissionsEx");
        if (permExPlugin != null && permExPlugin.isEnabled()) {
            if (!(this.handler instanceof PermissionsExHandler)) {
                PermissionsHandler.LOGGER.log(Level.INFO, "Essentials: Using PermissionsEx based permissions.");
                this.handler = new PermissionsExHandler();
            }
            return;
        }
        final Plugin permBukkitPlugin = pluginManager.getPlugin("PermissionsBukkit");
        if (permBukkitPlugin != null && permBukkitPlugin.isEnabled()) {
            if (!(this.handler instanceof PermissionsBukkitHandler)) {
                PermissionsHandler.LOGGER.log(Level.INFO, "Essentials: Using PermissionsBukkit based permissions.");
                this.handler = new PermissionsBukkitHandler(permBukkitPlugin);
            }
            return;
        }
        final Plugin bPermPlugin = pluginManager.getPlugin("bPermissions");
        if (bPermPlugin != null && bPermPlugin.isEnabled()) {
            if (!(this.handler instanceof BPermissionsHandler)) {
                PermissionsHandler.LOGGER.log(Level.INFO, "Essentials: Using bPermissions based permissions.");
                this.handler = new BPermissionsHandler();
            }
            return;
        }
        final Plugin permPlugin = pluginManager.getPlugin("Permissions");
        if (permPlugin != null && permPlugin.isEnabled()) {
            if (permPlugin.getDescription().getVersion().charAt(0) == '3') {
                /*if (!(this.handler instanceof Permissions3Handler)) {
                    PermissionsHandler.LOGGER.log(Level.INFO, "Essentials: Using Permissions 3 based permissions.");
                    this.handler = new Permissions3Handler(permPlugin);
                }*/ // REMOVED V3 HANDLER
                throw new UnsupportedOperationException("Permissions V3 handler is removed");
            }
            else if (!(this.handler instanceof Permissions2Handler)) {
                PermissionsHandler.LOGGER.log(Level.INFO, "Essentials: Using Permissions 2 based permissions.");
                this.handler = new Permissions2Handler(permPlugin);
            }
            return;
        }
        if (this.useSuperperms) {
            if (!(this.handler instanceof SuperpermsHandler)) {
                PermissionsHandler.LOGGER.log(Level.INFO, "Essentials: Using superperms based permissions.");
                this.handler = new SuperpermsHandler();
            }
        }
        else if (!(this.handler instanceof ConfigPermissionsHandler)) {
            PermissionsHandler.LOGGER.log(Level.INFO, "Essentials: Using config based permissions. Enable superperms in config.");
            this.handler = new ConfigPermissionsHandler(this.plugin);
        }
    }
    
    public void setUseSuperperms(final boolean useSuperperms) {
        this.useSuperperms = useSuperperms;
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
