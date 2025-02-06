package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.inventory.*;
import com.earth2me.essentials.*;
import java.util.logging.*;

public class Commandsell extends EssentialsCommand
{
    public Commandsell() {
        super("sell");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        ItemStack is = null;
        if (args[0].equalsIgnoreCase("hand")) {
            is = user.getItemInHand();
        }
        else {
            if (args[0].equalsIgnoreCase("inventory")) {
                for (final ItemStack stack : user.getInventory().getContents()) {
                    if (stack != null) {
                        if (stack.getType() != Material.AIR) {
                            try {
                                this.sellItem(user, stack, args, true);
                            }
                            catch (Exception ex) {}
                        }
                    }
                }
                return;
            }
            if (args[0].equalsIgnoreCase("blocks")) {
                for (final ItemStack stack : user.getInventory().getContents()) {
                    if (stack != null && stack.getTypeId() <= 255) {
                        if (stack.getType() != Material.AIR) {
                            try {
                                this.sellItem(user, stack, args, true);
                            }
                            catch (Exception ex2) {}
                        }
                    }
                }
                return;
            }
        }
        if (is == null) {
            is = this.ess.getItemDb().get(args[0]);
        }
        this.sellItem(user, is, args, false);
    }
    
    private void sellItem(final User user, final ItemStack is, final String[] args, final boolean isBulkSell) throws Exception {
        if (is == null || is.getType() == Material.AIR) {
            throw new Exception(Util.i18n("itemSellAir"));
        }
        final int id = is.getTypeId();
        int amount = 0;
        if (args.length > 1) {
            amount = Integer.parseInt(args[1].replaceAll("[^0-9]", ""));
            if (args[1].startsWith("-")) {
                amount = -amount;
            }
        }
        final double worth = this.ess.getWorth().getPrice(is);
        final boolean stack = args.length > 1 && args[1].endsWith("s");
        final boolean requireStack = this.ess.getSettings().isTradeInStacks(id);
        if (Double.isNaN(worth)) {
            throw new Exception(Util.i18n("itemCannotBeSold"));
        }
        if (requireStack && !stack) {
            throw new Exception(Util.i18n("itemMustBeStacked"));
        }
        int max = 0;
        if (!isBulkSell) {
            for (final ItemStack s : user.getInventory().getContents()) {
                if (s != null) {
                    if (s.getTypeId() == is.getTypeId()) {
                        if (s.getDurability() == is.getDurability()) {
                            max += s.getAmount();
                        }
                    }
                }
            }
        }
        else {
            max += is.getAmount();
        }
        if (stack) {
            amount *= 64;
        }
        if (amount < 1) {
            amount += max;
        }
        if (requireStack) {
            amount -= amount % 64;
        }
        if (amount > max || amount < 1) {
            user.sendMessage(Util.i18n("itemNotEnough1"));
            user.sendMessage(Util.i18n("itemNotEnough2"));
            throw new Exception(Util.i18n("itemNotEnough3"));
        }
        final ItemStack ris = new ItemStack(is.getType(), amount, is.getDurability());
        InventoryWorkaround.removeItem((Inventory)user.getInventory(), true, ris);
        user.updateInventory();
        Trade.log("Command", "Sell", "Item", user.getName(), new Trade(ris, this.ess), user.getName(), new Trade(worth * amount, this.ess), user.getLocation(), this.ess);
        user.giveMoney(worth * amount);
        user.sendMessage(Util.format("itemSold", Util.formatCurrency(worth * amount, this.ess), amount, Util.formatCurrency(worth, this.ess)));
        Commandsell.logger.log(Level.INFO, Util.format("itemSoldConsole", user.getDisplayName(), is.getType().toString().toLowerCase(), Util.formatCurrency(worth * amount, this.ess), amount, Util.formatCurrency(worth, this.ess)));
    }
}
