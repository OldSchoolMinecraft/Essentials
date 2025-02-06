package com.earth2me.essentials;

import org.bukkit.entity.*;
import org.bukkit.craftbukkit.inventory.*;
import net.minecraft.server.*;
import org.bukkit.craftbukkit.entity.*;

public class PlayerExtension extends PlayerWrapper
{
    protected final IEssentials ess;
    
    public PlayerExtension(final Player base, final IEssentials ess) {
        super(base);
        this.ess = ess;
    }
    
    public boolean isBanned() {
        return this.ess.getBans().isNameBanned(this.getName());
    }
    
    public boolean isIpBanned() {
        return this.ess.getBans().isIpBanned(this.getAddress().getAddress().getHostAddress());
    }
    
    public float getCorrectedYaw() {
        float angle = (this.getLocation().getYaw() - 90.0f) % 360.0f;
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        return angle;
    }
    
    public void showInventory(final IInventory inventory) {
        this.getHandle().a(inventory);
    }
    
    public void showInventory(final CraftInventoryPlayer inventory) {
        this.showInventory((IInventory)inventory.getInventory());
    }
    
    public TargetBlock getTarget() {
        return new TargetBlock(this.getBase());
    }
    
    public String getGroup() {
        return this.ess.getPermissionsHandler().getGroup(this.base);
    }
    
    public boolean inGroup(final String group) {
        return this.ess.getPermissionsHandler().inGroup(this.base, group);
    }
    
    public boolean canBuild() {
        return this.ess.getPermissionsHandler().canBuild(this.base, this.getGroup());
    }
    
    public EntityPlayer getHandle() {
        return this.getCraftPlayer().getHandle();
    }
    
    public CraftPlayer getCraftPlayer() {
        return (CraftPlayer)this.base;
    }
}
