package com.earth2me.essentials;

import org.bukkit.block.*;
import org.bukkit.*;
import java.util.*;
import java.util.logging.*;

public class NetherPortal
{
    private Block block;
    
    public NetherPortal(final Block b) {
        this.block = b;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public void setBlock(final Block b) {
        this.block = b;
    }
    
    public Location getSpawn() {
        if (this.block.getWorld().getBlockAt(this.block.getX() + 1, this.block.getY(), this.block.getZ()).getType().equals((Object)Material.PORTAL) || this.block.getWorld().getBlockAt(this.block.getX() - 1, this.block.getY(), this.block.getZ()).getType().equals((Object)Material.PORTAL)) {
            return new Location(this.block.getWorld(), (double)(this.block.getX() + 1), (double)this.block.getY(), (double)(this.block.getZ() + 1 - 2L * Math.round(Math.random())));
        }
        return new Location(this.block.getWorld(), (double)(this.block.getX() + 1 - 2L * Math.round(Math.random())), (double)this.block.getY(), (double)(this.block.getZ() + 1));
    }
    
    public static NetherPortal findPortal(final Block dest) {
        final World world = dest.getWorld();
        final ArrayList<Block> columns = new ArrayList<Block>();
        for (int x = dest.getX() - 16; x <= dest.getX() + 16; ++x) {
            for (int z = dest.getZ() - 16; z <= dest.getZ() + 16; ++z) {
                final int dx = dest.getX() - x;
                final int dz = dest.getZ() - z;
                if (dx * dx + dz * dz <= 256) {
                    columns.add(world.getBlockAt(x, 0, z));
                }
            }
        }
        for (final Block col : columns) {
            for (int y = 127; y >= 0; --y) {
                final Block b = world.getBlockAt(col.getX(), y, col.getZ());
                if (b.getType().equals((Object)Material.PORTAL) && Math.abs(dest.getY() - y) <= 16) {
                    return new NetherPortal(b);
                }
            }
        }
        return null;
    }
    
    public static NetherPortal createPortal(Block dest) {
        final World world = dest.getWorld();
        for (Material m = dest.getType(); (m.equals((Object)Material.LAVA) || m.equals((Object)Material.WATER) || m.equals((Object)Material.STATIONARY_LAVA) || m.equals((Object)Material.STATIONARY_WATER) || m.equals((Object)Material.SAND) || m.equals((Object)Material.GRAVEL)) && dest.getY() < 120; dest = world.getBlockAt(dest.getX(), dest.getY() + 4, dest.getZ()), m = dest.getType()) {}
        if (dest.getY() > 120) {
            dest = world.getBlockAt(dest.getX(), 120, dest.getZ());
        }
        else if (dest.getY() < 8) {
            dest = world.getBlockAt(dest.getX(), 8, dest.getZ());
        }
        final int x = dest.getX();
        final int y = dest.getY();
        final int z = dest.getZ();
        Logger.getLogger("Minecraft").log(Level.INFO, Util.format("creatingPortal", x, y, z));
        final ArrayList<Block> columns = new ArrayList<Block>();
        for (int x2 = x - 4; x2 <= x + 4; ++x2) {
            for (int z2 = z - 4; z2 <= z + 4; ++z2) {
                final double dx = x + 0.5f - x2;
                final double dz = z - z2;
                if (dx * dx + dz * dz <= 13.0) {
                    columns.add(world.getBlockAt(x2, 0, z2));
                }
            }
        }
        for (final Block col : columns) {
            world.getBlockAt(col.getX(), y - 1, col.getZ()).setType(Material.STONE);
            for (int yd = 0; yd < 4; ++yd) {
                world.getBlockAt(col.getX(), y + yd, col.getZ()).setType(Material.AIR);
            }
        }
        for (int xd = -1; xd < 3; ++xd) {
            for (int yd2 = -1; yd2 < 4; ++yd2) {
                if (xd == -1 || yd2 == -1 || xd == 2 || yd2 == 3) {
                    world.getBlockAt(x + xd, y + yd2, z).setType(Material.OBSIDIAN);
                }
            }
        }
        dest.setType(Material.FIRE);
        return new NetherPortal(dest);
    }
}
