package com.earth2me.essentials;

import org.bukkit.block.Block;
import org.bukkit.event.entity.*;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.*;
import net.minecraft.server.*;
import java.util.logging.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;

public class TNTExplodeListener extends EntityListener implements Runnable
{
    private final IEssentials ess;
    private boolean enabled;
    private int timer;
    
    public TNTExplodeListener(final IEssentials ess) {
        this.enabled = false;
        this.timer = -1;
        this.ess = ess;
    }
    
    public void enable() {
        if (!this.enabled) {
            this.enabled = true;
            this.timer = this.ess.scheduleSyncDelayedTask(this, 1000L);
            return;
        }
        if (this.timer != -1) {
            this.ess.getScheduler().cancelTask(this.timer);
            this.timer = this.ess.scheduleSyncDelayedTask(this, 1000L);
        }
    }
    
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (!this.enabled) {
            return;
        }
        if (event.getEntity() instanceof LivingEntity) {
            return;
        }
        try {
            final Set<ChunkPosition> set = new HashSet<ChunkPosition>(event.blockList().size());
            final Player[] players = this.ess.getServer().getOnlinePlayers();
            final List<ChunkPosition> blocksUnderPlayers = new ArrayList<ChunkPosition>(players.length);
            final Location loc = event.getLocation();
            for (final Player player : players) {
                if (player.getWorld().equals(loc.getWorld())) {
                    blocksUnderPlayers.add(new ChunkPosition(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()));
                }
            }
            for (final Block block : event.blockList()) {
                final ChunkPosition cp = new ChunkPosition(block.getX(), block.getY(), block.getZ());
                if (!blocksUnderPlayers.contains(cp)) {
                    set.add(cp);
                }
            }
            ((CraftServer)this.ess.getServer()).getHandle().sendPacketNearby(loc.getX(), loc.getY(), loc.getZ(), 64.0, ((CraftWorld)loc.getWorld()).getHandle().worldProvider.dimension, (Packet)new Packet60Explosion(loc.getX(), loc.getY(), loc.getZ(), 3.0f, (Set)set));
        }
        catch (Throwable ex) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        }
        event.setCancelled(true);
    }
    
    public void run() {
        this.enabled = false;
    }
}
