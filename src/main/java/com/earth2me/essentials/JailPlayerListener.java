package com.earth2me.essentials;

import java.util.logging.*;
import org.bukkit.event.player.*;

public class JailPlayerListener extends PlayerListener
{
    private static final Logger LOGGER;
    private final IEssentials ess;
    
    public JailPlayerListener(final IEssentials parent) {
        this.ess = parent;
    }
    
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        if (user.isJailed()) {
            event.setCancelled(true);
        }
    }
    
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        if (user.isJailed() && user.getJail() != null && !user.getJail().isEmpty()) {
            try {
                event.setRespawnLocation(this.ess.getJail().getJail(user.getJail()));
            }
            catch (Exception ex) {}
        }
    }
    
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        if (!user.isJailed() || user.getJail() == null || user.getJail().isEmpty()) {
            return;
        }
        try {
            event.setTo(this.ess.getJail().getJail(user.getJail()));
        }
        catch (Exception ex) {
            JailPlayerListener.LOGGER.log(Level.WARNING, Util.i18n("returnPlayerToJailError"), ex);
        }
        user.sendMessage(Util.i18n("jailMessage"));
    }
    
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final User u = this.ess.getUser(event.getPlayer());
        if (u.isJailed()) {
            try {
                this.ess.getJail().sendToJail(u, u.getJail());
            }
            catch (Exception ex) {
                JailPlayerListener.LOGGER.log(Level.WARNING, Util.i18n("returnPlayerToJailError"), ex);
            }
            u.sendMessage(Util.i18n("jailMessage"));
        }
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
