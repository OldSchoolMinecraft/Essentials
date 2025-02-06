package com.earth2me.essentials.signs;

import net.minecraft.server.*;
import org.bukkit.craftbukkit.inventory.*;
import org.bukkit.inventory.*;
import com.earth2me.essentials.*;
import org.bukkit.inventory.ItemStack;

public class SignFree extends EssentialsSign
{
    public SignFree() {
        super("Free");
    }
    
    @Override
    protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException {
        this.getItemStack(sign.getLine(1), 1, ess);
        return true;
    }
    
    @Override
    protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException {
        final ItemStack item = this.getItemStack(sign.getLine(1), 1, ess);
        item.setAmount(item.getType().getMaxStackSize() * 9 * 4);
        final CraftInventoryPlayer inv = new CraftInventoryPlayer(new InventoryPlayer((EntityHuman)player.getHandle()));
        inv.clear();
        InventoryWorkaround.addItem((Inventory)inv, true, item);
        player.showInventory(inv);
        Trade.log("Sign", "Free", "Interact", username, null, username, new Trade(item, ess), sign.getBlock().getLocation(), ess);
        return true;
    }
}
