package com.earth2me.essentials;

import java.util.logging.*;
import org.bukkit.event.server.*;

public class EssentialsPluginListener extends ServerListener implements IConf
{
    private final transient IEssentials ess;
    private static final Logger LOGGER;
    
    public EssentialsPluginListener(final IEssentials ess) {
        this.ess = ess;
    }
    
    public void onPluginEnable(final PluginEnableEvent event) {
        this.ess.getPermissionsHandler().checkPermissions();
        if (!this.ess.getPaymentMethod().hasMethod() && this.ess.getPaymentMethod().setMethod(event.getPlugin())) {
            EssentialsPluginListener.LOGGER.log(Level.INFO, "[Essentials] Payment method found (" + this.ess.getPaymentMethod().getMethod().getName() + " version: " + this.ess.getPaymentMethod().getMethod().getVersion() + ")");
        }
    }
    
    public void onPluginDisable(final PluginDisableEvent event) {
        this.ess.getPermissionsHandler().checkPermissions();
        if (this.ess.getPaymentMethod() != null && this.ess.getPaymentMethod().hasMethod() && this.ess.getPaymentMethod().checkDisabled(event.getPlugin())) {
            EssentialsPluginListener.LOGGER.log(Level.INFO, "[Essentials] Payment method was disabled. No longer accepting payments.");
        }
    }
    
    public void reloadConfig() {
        this.ess.getPermissionsHandler().setUseSuperperms(this.ess.getSettings().useBukkitPermissions());
        this.ess.getPermissionsHandler().checkPermissions();
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
