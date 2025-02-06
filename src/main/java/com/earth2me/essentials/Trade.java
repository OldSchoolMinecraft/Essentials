package com.earth2me.essentials;

import org.bukkit.inventory.*;
import org.bukkit.*;
import java.util.logging.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class Trade
{
    private final transient String command;
    private final transient Double money;
    private final transient ItemStack itemStack;
    private final transient IEssentials ess;
    private static FileWriter fw;
    
    public Trade(final String command, final IEssentials ess) {
        this(command, null, null, ess);
    }
    
    public Trade(final double money, final IEssentials ess) {
        this(null, money, null, ess);
    }
    
    public Trade(final ItemStack items, final IEssentials ess) {
        this(null, null, items, ess);
    }
    
    private Trade(final String command, final Double money, final ItemStack item, final IEssentials ess) {
        this.command = command;
        this.money = money;
        this.itemStack = item;
        this.ess = ess;
    }
    
    public void isAffordableFor(final IUser user) throws ChargeException {
        final double mon = user.getMoney();
        if (this.getMoney() != null && mon < this.getMoney() && this.getMoney() > 0.0 && !user.isAuthorized("essentials.eco.loan")) {
            throw new ChargeException(Util.i18n("notEnoughMoney"));
        }
        if (this.getItemStack() != null && !InventoryWorkaround.containsItem((Inventory)user.getInventory(), true, this.itemStack)) {
            throw new ChargeException(Util.format("missingItems", this.getItemStack().getAmount(), this.getItemStack().getType().toString().toLowerCase().replace("_", " ")));
        }
        if (this.command != null && !this.command.isEmpty() && !user.isAuthorized("essentials.nocommandcost.all") && !user.isAuthorized("essentials.nocommandcost." + this.command) && mon < this.ess.getSettings().getCommandCost((this.command.charAt(0) == '/') ? this.command.substring(1) : this.command) && 0.0 < this.ess.getSettings().getCommandCost((this.command.charAt(0) == '/') ? this.command.substring(1) : this.command) && !user.isAuthorized("essentials.eco.loan")) {
            throw new ChargeException(Util.i18n("notEnoughMoney"));
        }
    }
    
    public void pay(final IUser user) {
        if (this.getMoney() != null && this.getMoney() > 0.0) {
            user.giveMoney(this.getMoney());
        }
        if (this.getItemStack() != null) {
            final Map<Integer, ItemStack> leftOver = InventoryWorkaround.addItem((Inventory)user.getInventory(), true, this.getItemStack());
            for (final ItemStack itemStack : leftOver.values()) {
                InventoryWorkaround.dropItem(user.getLocation(), itemStack);
            }
            user.updateInventory();
        }
    }
    
    public void charge(final IUser user) throws ChargeException {
        if (this.getMoney() != null) {
            final double mon = user.getMoney();
            if (mon < this.getMoney() && this.getMoney() > 0.0 && !user.isAuthorized("essentials.eco.loan")) {
                throw new ChargeException(Util.i18n("notEnoughMoney"));
            }
            user.takeMoney(this.getMoney());
        }
        if (this.getItemStack() != null) {
            if (!InventoryWorkaround.containsItem((Inventory)user.getInventory(), true, this.itemStack)) {
                throw new ChargeException(Util.format("missingItems", this.getItemStack().getAmount(), this.getItemStack().getType().toString().toLowerCase().replace("_", " ")));
            }
            InventoryWorkaround.removeItem((Inventory)user.getInventory(), true, this.getItemStack());
            user.updateInventory();
        }
        if (this.command != null && !this.command.isEmpty() && !user.isAuthorized("essentials.nocommandcost.all") && !user.isAuthorized("essentials.nocommandcost." + this.command)) {
            final double mon = user.getMoney();
            final double cost = this.ess.getSettings().getCommandCost((this.command.charAt(0) == '/') ? this.command.substring(1) : this.command);
            if (mon < cost && cost > 0.0 && !user.isAuthorized("essentials.eco.loan")) {
                throw new ChargeException(Util.i18n("notEnoughMoney"));
            }
            user.takeMoney(cost);
        }
    }
    
    public Double getMoney() {
        return this.money;
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    public static void log(final String type, final String subtype, final String event, final String sender, final Trade charge, final String receiver, final Trade pay, final Location loc, final IEssentials ess) {
        if (!ess.getSettings().isEcoLogEnabled()) {
            return;
        }
        if (Trade.fw == null) {
            try {
                Trade.fw = new FileWriter(new File(ess.getDataFolder(), "trade.log"), true);
            }
            catch (IOException ex) {
                Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(type).append(",").append(subtype).append(",").append(event).append(",\"");
        sb.append(DateFormat.getDateTimeInstance(0, 0).format(new Date()));
        sb.append("\",\"");
        if (sender != null) {
            sb.append(sender);
        }
        sb.append("\",");
        if (charge == null) {
            sb.append("\"\",\"\",\"\"");
        }
        else {
            if (charge.getItemStack() != null) {
                sb.append(charge.getItemStack().getAmount()).append(",");
                sb.append(charge.getItemStack().getType().toString()).append(",");
                sb.append(charge.getItemStack().getDurability());
            }
            if (charge.getMoney() != null) {
                sb.append(charge.getMoney()).append(",");
                sb.append("money").append(",");
                sb.append(ess.getSettings().getCurrencySymbol());
            }
        }
        sb.append(",\"");
        if (receiver != null) {
            sb.append(receiver);
        }
        sb.append("\",");
        if (pay == null) {
            sb.append("\"\",\"\",\"\"");
        }
        else {
            if (pay.getItemStack() != null) {
                sb.append(pay.getItemStack().getAmount()).append(",");
                sb.append(pay.getItemStack().getType().toString()).append(",");
                sb.append(pay.getItemStack().getDurability());
            }
            if (pay.getMoney() != null) {
                sb.append(pay.getMoney()).append(",");
                sb.append("money").append(",");
                sb.append(ess.getSettings().getCurrencySymbol());
            }
        }
        if (loc == null) {
            sb.append(",\"\",\"\",\"\",\"\"");
        }
        else {
            sb.append(",\"");
            sb.append(loc.getWorld().getName()).append("\",");
            sb.append(loc.getBlockX()).append(",");
            sb.append(loc.getBlockY()).append(",");
            sb.append(loc.getBlockZ()).append(",");
        }
        sb.append("\n");
        try {
            Trade.fw.write(sb.toString());
            Trade.fw.flush();
        }
        catch (IOException ex2) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex2);
        }
    }
    
    public static void closeLog() {
        if (Trade.fw != null) {
            try {
                Trade.fw.close();
            }
            catch (IOException ex) {
                Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
            }
            Trade.fw = null;
        }
    }
    
    static {
        Trade.fw = null;
    }
}
