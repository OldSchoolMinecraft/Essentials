package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.inventory.*;

public class Commandworth extends EssentialsCommand
{
    public Commandworth() {
        super("worth");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        ItemStack is = user.getInventory().getItemInHand();
        int amount = is.getAmount();
        if (args.length > 0) {
            is = this.ess.getItemDb().get(args[0]);
        }
        try {
            if (args.length > 1) {
                amount = Integer.parseInt(args[1]);
            }
        }
        catch (NumberFormatException ex) {
            amount = 64;
        }
        is.setAmount(amount);
        final double worth = this.ess.getWorth().getPrice(is);
        if (Double.isNaN(worth)) {
            throw new Exception(Util.i18n("itemCannotBeSold"));
        }
        user.sendMessage((is.getDurability() != 0) ? Util.format("worthMeta", is.getType().toString().toLowerCase().replace("_", ""), is.getDurability(), Util.formatCurrency(worth * amount, this.ess), amount, Util.formatCurrency(worth, this.ess)) : Util.format("worth", is.getType().toString().toLowerCase().replace("_", ""), Util.formatCurrency(worth * amount, this.ess), amount, Util.formatCurrency(worth, this.ess)));
    }
}
