package com.earth2me.essentials;

import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.block.*;
import org.bukkit.*;

public class TargetBlock
{
    private final transient Location location;
    private final transient double viewHeight;
    private final transient int maxDistance;
    private final transient int[] blockToIgnore;
    private final transient double checkDistance;
    private transient double curDistance;
    private transient double targetPositionX;
    private transient double targetPositionY;
    private transient double targetPositionZ;
    private transient int itargetPositionX;
    private transient int itargetPositionY;
    private transient int itargetPositionZ;
    private transient int prevPositionX;
    private transient int prevPositionY;
    private transient int prevPositionZ;
    private final transient double offsetX;
    private final transient double offsetY;
    private final transient double offsetZ;
    
    public TargetBlock(final Player player) {
        this(player.getLocation(), 300, 1.65, 0.2, null);
    }
    
    public TargetBlock(final Location loc) {
        this(loc, 300, 0.0, 0.2, null);
    }
    
    public TargetBlock(final Player player, final int maxDistance, final double checkDistance) {
        this(player.getLocation(), maxDistance, 1.65, checkDistance, null);
    }
    
    public TargetBlock(final Location loc, final int maxDistance, final double checkDistance) {
        this(loc, maxDistance, 0.0, checkDistance, null);
    }
    
    public TargetBlock(final Player player, final int maxDistance, final double checkDistance, final int[] blocksToIgnore) {
        this(player.getLocation(), maxDistance, 1.65, checkDistance, blocksToIgnore);
    }
    
    public TargetBlock(final Location loc, final int maxDistance, final double checkDistance, final int[] blocksToIgnore) {
        this(loc, maxDistance, 0.0, checkDistance, blocksToIgnore);
    }
    
    public TargetBlock(final Player player, final int maxDistance, final double checkDistance, final List<String> blocksToIgnore) {
        this(player.getLocation(), maxDistance, 1.65, checkDistance, convertStringArraytoIntArray(blocksToIgnore));
    }
    
    public TargetBlock(final Location loc, final int maxDistance, final double checkDistance, final List<String> blocksToIgnore) {
        this(loc, maxDistance, 0.0, checkDistance, convertStringArraytoIntArray(blocksToIgnore));
    }
    
    private TargetBlock(final Location loc, final int maxDistance, final double viewHeight, final double checkDistance, final int[] blocksToIgnore) {
        this.location = loc;
        this.maxDistance = maxDistance;
        this.viewHeight = viewHeight;
        this.checkDistance = checkDistance;
        if (blocksToIgnore == null || blocksToIgnore.length == 0) {
            this.blockToIgnore = new int[0];
        }
        else {
            System.arraycopy(blocksToIgnore, 0, this.blockToIgnore = new int[blocksToIgnore.length], 0, this.blockToIgnore.length);
        }
        final double xRotation = (loc.getYaw() + 90.0f) % 360.0f;
        final double yRotation = loc.getPitch() * -1.0f;
        final double hypotenuse = checkDistance * Math.cos(Math.toRadians(yRotation));
        this.offsetX = hypotenuse * Math.cos(Math.toRadians(xRotation));
        this.offsetY = checkDistance * Math.sin(Math.toRadians(yRotation));
        this.offsetZ = hypotenuse * Math.sin(Math.toRadians(xRotation));
        this.reset();
    }
    
    public final void reset() {
        this.targetPositionX = this.location.getX();
        this.targetPositionY = this.location.getY() + this.viewHeight;
        this.targetPositionZ = this.location.getZ();
        this.itargetPositionX = (int)Math.floor(this.targetPositionX);
        this.itargetPositionY = (int)Math.floor(this.targetPositionY);
        this.itargetPositionZ = (int)Math.floor(this.targetPositionZ);
        this.prevPositionX = this.itargetPositionX;
        this.prevPositionY = this.itargetPositionY;
        this.prevPositionZ = this.itargetPositionZ;
        this.curDistance = 0.0;
    }
    
    public double getDistanceToBlock() {
        final double blockUnderPlayerX = Math.floor(this.location.getX() + 0.5);
        final double blockUnderPlayerY = Math.floor(this.location.getY() - 0.5);
        final double blockUnderPlayerZ = Math.floor(this.location.getZ() + 0.5);
        final Block block = this.getTargetBlock();
        final double distX = block.getX() - blockUnderPlayerX;
        final double distY = block.getY() - blockUnderPlayerY;
        final double distZ = block.getZ() - blockUnderPlayerZ;
        return Math.sqrt(distX * distX + distY * distY + distZ * distZ);
    }
    
    public int getDistanceToBlockRounded() {
        return (int)Math.round(this.getDistanceToBlock());
    }
    
    public int getXDistanceToBlock() {
        return (int)Math.floor(this.getTargetBlock().getX() - this.location.getBlockX() + 0.5);
    }
    
    public int getYDistanceToBlock() {
        return (int)Math.floor(this.getTargetBlock().getY() - this.location.getBlockY() + this.viewHeight);
    }
    
    public int getZDistanceToBlock() {
        return (int)Math.floor(this.getTargetBlock().getZ() - this.location.getBlockZ() + 0.5);
    }
    
    public Block getTargetBlock() {
        this.reset();
        Block block;
        do {
            block = this.getNextBlock();
        } while (block != null && (block.getTypeId() == 0 || this.blockIsIgnored(block.getTypeId())));
        return block;
    }
    
    public boolean setTargetBlock(final int typeID) {
        return this.setTargetBlock(Material.getMaterial(typeID));
    }
    
    public boolean setTargetBlock(final Material type) {
        if (type == null) {
            return false;
        }
        final Block block = this.getTargetBlock();
        if (block != null) {
            block.setType(type);
            return true;
        }
        return false;
    }
    
    public boolean setTargetBlock(final String type) {
        return this.setTargetBlock(Material.valueOf(type));
    }
    
    public Block getFaceBlock() {
        final Block block = this.getTargetBlock();
        if (block == null) {
            return null;
        }
        return this.getPreviousBlock();
    }
    
    public boolean setFaceBlock(final int typeID) {
        return this.setFaceBlock(Material.getMaterial(typeID));
    }
    
    public boolean setFaceBlock(final Material type) {
        if (type == null) {
            return false;
        }
        if (this.getCurrentBlock() != null) {
            final Block blk = this.location.getWorld().getBlockAt(this.prevPositionX, this.prevPositionY, this.prevPositionZ);
            blk.setType(type);
            return true;
        }
        return false;
    }
    
    public boolean setFaceBlock(final String type) {
        return this.setFaceBlock(Material.valueOf(type));
    }
    
    public Block getNextBlock() {
        this.prevPositionX = this.itargetPositionX;
        this.prevPositionY = this.itargetPositionY;
        this.prevPositionZ = this.itargetPositionZ;
        do {
            this.curDistance += this.checkDistance;
            this.targetPositionX += this.offsetX;
            this.targetPositionY += this.offsetY;
            this.targetPositionZ += this.offsetZ;
            this.itargetPositionX = (int)Math.floor(this.targetPositionX);
            this.itargetPositionY = (int)Math.floor(this.targetPositionY);
            this.itargetPositionZ = (int)Math.floor(this.targetPositionZ);
        } while (this.curDistance <= this.maxDistance && this.itargetPositionX == this.prevPositionX && this.itargetPositionY == this.prevPositionY && this.itargetPositionZ == this.prevPositionZ);
        if (this.curDistance > this.maxDistance) {
            return null;
        }
        return this.location.getWorld().getBlockAt(this.itargetPositionX, this.itargetPositionY, this.itargetPositionZ);
    }
    
    public Block getCurrentBlock() {
        Block block;
        if (this.curDistance <= this.maxDistance) {
            block = this.location.getWorld().getBlockAt(this.itargetPositionX, this.itargetPositionY, this.itargetPositionZ);
        }
        else {
            block = null;
        }
        return block;
    }
    
    public boolean setCurrentBlock(final int typeID) {
        return this.setCurrentBlock(Material.getMaterial(typeID));
    }
    
    public boolean setCurrentBlock(final Material type) {
        final Block blk = this.getCurrentBlock();
        if (blk != null && type != null) {
            blk.setType(type);
            return true;
        }
        return false;
    }
    
    public boolean setCurrentBlock(final String type) {
        return this.setCurrentBlock(Material.valueOf(type));
    }
    
    public Block getPreviousBlock() {
        return this.location.getWorld().getBlockAt(this.prevPositionX, this.prevPositionY, this.prevPositionZ);
    }
    
    public boolean setPreviousBlock(final int typeID) {
        return this.setPreviousBlock(Material.getMaterial(typeID));
    }
    
    public boolean setPreviousBlock(final Material type) {
        final Block blk = this.getPreviousBlock();
        if (blk != null && type != null) {
            blk.setType(type);
            return true;
        }
        return false;
    }
    
    public boolean setPreviousBlock(final String type) {
        return this.setPreviousBlock(Material.valueOf(type));
    }
    
    private static int[] convertStringArraytoIntArray(final List<String> array) {
        final int[] intarray = new int[(array == null) ? 0 : array.size()];
        for (int i = 0; i < intarray.length; ++i) {
            try {
                intarray[i] = Integer.parseInt(array.get(i));
            }
            catch (NumberFormatException ex) {}
        }
        return intarray;
    }
    
    private boolean blockIsIgnored(final int value) {
        for (final int i : this.blockToIgnore) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }
}
