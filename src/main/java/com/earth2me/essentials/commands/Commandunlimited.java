package com.earth2me.essentials.commands;

import org.bukkit.*;
import java.util.*;
import com.earth2me.essentials.*;
import org.bukkit.inventory.*;

public class Commandunlimited extends EssentialsCommand
{
    public Commandunlimited() {
        super("unlimited");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        User target = user;
        if (args.length > 1 && user.isAuthorized("essentials.unlimited.others")) {
            target = this.getPlayer(server, args, 1);
        }
        if (args[0].equalsIgnoreCase("list")) {
            final String list = this.getList(target);
            user.sendMessage(list);
        }
        else if (args[0].equalsIgnoreCase("clear")) {
            final List<Integer> itemList = target.getUnlimited();
            for (int index = 0; itemList.size() > index; ++index) {
                final Integer item = itemList.get(index);
                if (!this.toggleUnlimited(user, target, item.toString())) {}
            }
        }
        else {
            this.toggleUnlimited(user, target, args[0]);
        }
    }
    
    private String getList(final User target) {
        final StringBuilder sb = new StringBuilder();
        sb.append(Util.i18n("unlimitedItems")).append(" ");
        boolean first = true;
        final List<Integer> items = target.getUnlimited();
        if (items.isEmpty()) {
            sb.append(Util.i18n("none"));
        }
        for (final Integer integer : items) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            final String matname = Material.getMaterial((int)integer).toString().toLowerCase().replace("_", "");
            sb.append(matname);
        }
        return sb.toString();
    }
    
    private Boolean toggleUnlimited(final User user, final User target, final String item) throws Exception {
        final ItemStack stack = this.ess.getItemDb().get(item, 1);
        stack.setAmount(Math.min(stack.getType().getMaxStackSize(), 2));
        final String itemname = stack.getType().toString().toLowerCase().replace("_", "");
        if (this.ess.getSettings().permissionBasedItemSpawn() && !user.isAuthorized("essentials.unlimited.item-all") && !user.isAuthorized("essentials.unlimited.item-" + itemname) && !user.isAuthorized("essentials.unlimited.item-" + stack.getTypeId()) && ((stack.getType() != Material.WATER_BUCKET && stack.getType() != Material.LAVA_BUCKET) || !user.isAuthorized("essentials.unlimited.item-bucket"))) {
            throw new Exception(Util.format("unlimitedItemPermission", itemname));
        }
        String message = "disableUnlimited";
        Boolean enableUnlimited = false;
        if (!target.hasUnlimited(stack)) {
            message = "enableUnlimited";
            enableUnlimited = true;
            if (!InventoryWorkaround.containsItem((Inventory)target.getInventory(), true, stack)) {
                target.getInventory().addItem(new ItemStack[] { stack });
            }
        }
        if (user != target) {
            user.sendMessage(Util.format(message, itemname, target.getDisplayName()));
        }
        target.sendMessage(Util.format(message, itemname, target.getDisplayName()));
        target.setUnlimited(stack, enableUnlimited);
        return true;
    }
}
