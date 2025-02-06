package com.earth2me.essentials.commands;

import com.earth2me.essentials.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class Commanditem extends EssentialsCommand
{
    public Commanditem() {
        super("item");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final ItemStack stack = this.ess.getItemDb().get(args[0]);
        final String itemname = stack.getType().toString().toLowerCase().replace("_", "");
        Label_0185: {
            if (this.ess.getSettings().permissionBasedItemSpawn()) {
                if (user.isAuthorized("essentials.itemspawn.item-all") || user.isAuthorized("essentials.itemspawn.item-" + itemname) || user.isAuthorized("essentials.itemspawn.item-" + stack.getTypeId())) {
                    break Label_0185;
                }
            }
            else if (user.isAuthorized("essentials.itemspawn.exempt") || user.canSpawnItem(stack.getTypeId())) {
                break Label_0185;
            }
            throw new Exception(Util.format("cantSpawnItem", itemname));
        }
        if (args.length > 1 && Integer.parseInt(args[1]) > 0) {
            stack.setAmount(Integer.parseInt(args[1]));
        }
        if (stack.getType() == Material.AIR) {
            throw new Exception(Util.format("cantSpawnItem", "Air"));
        }
        final String itemName = stack.getType().toString().toLowerCase().replace('_', ' ');
        user.sendMessage(Util.format("itemSpawn", stack.getAmount(), itemName));
        user.getInventory().addItem(new ItemStack[] { stack });
        user.updateInventory();
    }
}
