package com.earth2me.essentials.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import com.earth2me.essentials.*;

public class Commandgive extends EssentialsCommand
{
    public Commandgive() {
        super("give");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        final ItemStack stack = this.ess.getItemDb().get(args[1]);
        final String itemname = stack.getType().toString().toLowerCase().replace("_", "");
        Label_0249: {
            if (sender instanceof Player) {
                if (this.ess.getSettings().permissionBasedItemSpawn()) {
                    if (this.ess.getUser(sender).isAuthorized("essentials.give.item-all") || this.ess.getUser(sender).isAuthorized("essentials.give.item-" + itemname) || this.ess.getUser(sender).isAuthorized("essentials.give.item-" + stack.getTypeId())) {
                        break Label_0249;
                    }
                }
                else if (this.ess.getUser(sender).isAuthorized("essentials.itemspawn.exempt") || this.ess.getUser(sender).canSpawnItem(stack.getTypeId())) {
                    break Label_0249;
                }
                throw new Exception(ChatColor.RED + "You are not allowed to spawn the item " + itemname);
            }
        }
        if (args.length > 2 && Integer.parseInt(args[2]) > 0) {
            stack.setAmount(Integer.parseInt(args[2]));
        }
        if (stack.getType() == Material.AIR) {
            throw new Exception(ChatColor.RED + "You can't give air.");
        }
        final User giveTo = this.getPlayer(server, args, 0);
        final String itemName = stack.getType().toString().toLowerCase().replace('_', ' ');
        sender.sendMessage(ChatColor.BLUE + "Giving " + stack.getAmount() + " of " + itemName + " to " + giveTo.getDisplayName() + ".");
        giveTo.getInventory().addItem(new ItemStack[] { stack });
        giveTo.updateInventory();
    }
}
