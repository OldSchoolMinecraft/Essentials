package com.earth2me.essentials.commands;

import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandrepair extends EssentialsCommand
{
    public Commandrepair() {
        super("repair");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        if (args[0].equalsIgnoreCase("hand")) {
            final ItemStack item = user.getItemInHand();
            final String itemName = item.getType().toString().toLowerCase();
            final Trade charge = new Trade("repair-" + itemName.replace('_', '-'), this.ess);
            charge.isAffordableFor(user);
            this.repairItem(item);
            charge.charge(user);
            user.sendMessage(Util.format("repair", itemName.replace('_', ' ')));
        }
        else {
            if (!args[0].equalsIgnoreCase("all")) {
                throw new NotEnoughArgumentsException();
            }
            final List<String> repaired = new ArrayList<String>();
            this.repairItems(user.getInventory().getContents(), user, repaired);
            this.repairItems(user.getInventory().getArmorContents(), user, repaired);
            if (repaired.isEmpty()) {
                throw new Exception(Util.format("repairNone", new Object[0]));
            }
            user.sendMessage(Util.format("repair", Util.joinList(repaired)));
        }
    }
    
    private void repairItem(final ItemStack item) throws Exception {
        final Material material = Material.getMaterial(item.getTypeId());
        if (material.isBlock() || material.getMaxDurability() < 0) {
            throw new Exception(Util.i18n("repairInvalidType"));
        }
        if (item.getDurability() == 0) {
            throw new Exception(Util.i18n("repairAlreadyFixed"));
        }
        item.setDurability((short)0);
    }
    
    private void repairItems(final ItemStack[] items, final IUser user, final List<String> repaired) {
        for (final ItemStack item : items) {
            final String itemName = item.getType().toString().toLowerCase();
            final Trade charge = new Trade("repair-" + itemName.replace('_', '-'), this.ess);
            Label_0155: {
                try {
                    charge.isAffordableFor(user);
                }
                catch (ChargeException ex) {
                    user.sendMessage(ex.getMessage());
                    break Label_0155;
                }
                try {
                    this.repairItem(item);
                }
                catch (Exception e) {
                    break Label_0155;
                }
                try {
                    charge.charge(user);
                }
                catch (ChargeException ex) {
                    user.sendMessage(ex.getMessage());
                }
                repaired.add(itemName.replace('_', ' '));
            }
        }
    }
}
