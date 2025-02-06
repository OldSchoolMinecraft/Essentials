package com.earth2me.essentials.signs;

import com.earth2me.essentials.*;
import org.bukkit.inventory.*;

public class SignTrade extends EssentialsSign
{
    public SignTrade() {
        super("Trade");
    }
    
    @Override
    protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        this.validateTrade(sign, 1, false, ess);
        this.validateTrade(sign, 2, true, ess);
        final Trade charge = this.getTrade(sign, 2, true, true, ess);
        charge.isAffordableFor(player);
        sign.setLine(3, "§8" + username);
        charge.charge(player);
        Trade.log("Sign", "Trade", "Create", username, charge, username, null, sign.getBlock().getLocation(), ess);
        return true;
    }
    
    @Override
    protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        Label_0187: {
            if (sign.getLine(3).substring(2).equalsIgnoreCase(username)) {
                try {
                    final Trade stored = this.getTrade(sign, 1, true, true, ess);
                    this.substractAmount(sign, 1, stored, ess);
                    stored.pay(player);
                    Trade.log("Sign", "Trade", "OwnerInteract", username, null, username, stored, sign.getBlock().getLocation(), ess);
                    break Label_0187;
                }
                catch (SignException e) {
                    throw new SignException(Util.i18n("tradeSignEmptyOwner"));
                }
            }
            final Trade charge = this.getTrade(sign, 1, false, false, ess);
            final Trade trade = this.getTrade(sign, 2, false, true, ess);
            charge.isAffordableFor(player);
            this.substractAmount(sign, 2, trade, ess);
            trade.pay(player);
            this.addAmount(sign, 1, charge, ess);
            charge.charge(player);
            Trade.log("Sign", "Trade", "Interact", sign.getLine(3), charge, username, trade, sign.getBlock().getLocation(), ess);
        }
        sign.updateSign();
        return true;
    }
    
    @Override
    protected boolean onSignBreak(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException {
        if ((sign.getLine(3).length() > 3 && sign.getLine(3).substring(2).equalsIgnoreCase(username)) || player.isAuthorized("essentials.signs.trade.override")) {
            final Trade stored1 = this.getTrade(sign, 1, true, false, ess);
            final Trade stored2 = this.getTrade(sign, 2, true, false, ess);
            stored1.pay(player);
            stored2.pay(player);
            Trade.log("Sign", "Trade", "Break", username, stored2, username, stored1, sign.getBlock().getLocation(), ess);
            return true;
        }
        return false;
    }
    
    protected final void validateTrade(final ISign sign, final int index, final boolean amountNeeded, final IEssentials ess) throws SignException {
        final String line = sign.getLine(index).trim();
        if (line.isEmpty()) {
            throw new SignException("Empty line");
        }
        final String[] split = line.split("[ :]+");
        if (split.length == 1 && !amountNeeded) {
            final Double money = this.getMoney(split[0]);
            if (money != null) {
                if (Util.formatCurrency(money, ess).length() * 2 > 15) {
                    throw new SignException("Line can be too long!");
                }
                sign.setLine(index, Util.formatCurrency(money, ess) + ":0");
                return;
            }
        }
        if (split.length == 2 && amountNeeded) {
            final Double money = this.getMoney(split[0]);
            Double amount = this.getDoublePositive(split[1]);
            if (money != null && amount != null) {
                amount -= amount % money;
                if (amount < 0.01 || money < 0.01) {
                    throw new SignException(Util.i18n("moreThanZero"));
                }
                sign.setLine(index, Util.formatCurrency(money, ess) + ":" + Util.formatCurrency(amount, ess).substring(1));
                return;
            }
        }
        if (split.length == 2 && !amountNeeded) {
            final int amount2 = this.getIntegerPositive(split[0]);
            final ItemStack item = this.getItemStack(split[1], amount2, ess);
            if (amount2 < 1 || item.getTypeId() == 0) {
                throw new SignException(Util.i18n("moreThanZero"));
            }
            final String newline = amount2 + " " + split[1] + ":0";
            if ((newline + amount2).length() > 16) {
                throw new SignException("Line can be too long!");
            }
            sign.setLine(index, newline);
        }
        else {
            if (split.length != 3 || !amountNeeded) {
                throw new SignException(Util.format("invalidSignLine", index + 1));
            }
            final int stackamount = this.getIntegerPositive(split[0]);
            final ItemStack item = this.getItemStack(split[1], stackamount, ess);
            int amount3 = this.getIntegerPositive(split[2]);
            amount3 -= amount3 % stackamount;
            if (amount3 < 1 || stackamount < 1 || item.getTypeId() == 0) {
                throw new SignException(Util.i18n("moreThanZero"));
            }
            sign.setLine(index, stackamount + " " + split[1] + ":" + amount3);
        }
    }
    
    protected final Trade getTrade(final ISign sign, final int index, final boolean fullAmount, final boolean notEmpty, final IEssentials ess) throws SignException {
        final String line = sign.getLine(index).trim();
        if (line.isEmpty()) {
            throw new SignException("Empty line");
        }
        final String[] split = line.split("[ :]+");
        if (split.length == 2) {
            try {
                final Double money = this.getMoney(split[0]);
                final Double amount = notEmpty ? this.getDoublePositive(split[1]) : this.getDouble(split[1]);
                if (money != null && amount != null) {
                    return new Trade(fullAmount ? amount : money, ess);
                }
            }
            catch (SignException e) {
                throw new SignException(Util.i18n("tradeSignEmpty"));
            }
        }
        if (split.length != 3) {
            throw new SignException(Util.format("invalidSignLine", index + 1));
        }
        final int stackamount = this.getIntegerPositive(split[0]);
        final ItemStack item = this.getItemStack(split[1], stackamount, ess);
        int amount2 = this.getInteger(split[2]);
        amount2 -= amount2 % stackamount;
        if (notEmpty && (amount2 < 1 || stackamount < 1 || item.getTypeId() == 0)) {
            throw new SignException(Util.i18n("tradeSignEmpty"));
        }
        item.setAmount(fullAmount ? amount2 : stackamount);
        return new Trade(item, ess);
    }
    
    protected final void substractAmount(final ISign sign, final int index, final Trade trade, final IEssentials ess) throws SignException {
        final Double money = trade.getMoney();
        if (money != null) {
            this.changeAmount(sign, index, -money, ess);
        }
        final ItemStack item = trade.getItemStack();
        if (item != null) {
            this.changeAmount(sign, index, -item.getAmount(), ess);
        }
    }
    
    protected final void addAmount(final ISign sign, final int index, final Trade trade, final IEssentials ess) throws SignException {
        final Double money = trade.getMoney();
        if (money != null) {
            this.changeAmount(sign, index, money, ess);
        }
        final ItemStack item = trade.getItemStack();
        if (item != null) {
            this.changeAmount(sign, index, item.getAmount(), ess);
        }
    }
    
    private void changeAmount(final ISign sign, final int index, final double value, final IEssentials ess) throws SignException {
        final String line = sign.getLine(index).trim();
        if (line.isEmpty()) {
            throw new SignException("Empty line");
        }
        final String[] split = line.split("[ :]+");
        if (split.length == 2) {
            final Double money = this.getMoney(split[0]);
            final Double amount = this.getDouble(split[1]);
            if (money != null && amount != null) {
                sign.setLine(index, Util.formatCurrency(money, ess) + ":" + Util.formatCurrency(amount + value, ess).substring(1));
                return;
            }
        }
        if (split.length == 3) {
            final int stackamount = this.getIntegerPositive(split[0]);
            final ItemStack item = this.getItemStack(split[1], stackamount, ess);
            final int amount2 = this.getInteger(split[2]);
            sign.setLine(index, stackamount + " " + split[1] + ":" + (amount2 + Math.round(value)));
            return;
        }
        throw new SignException(Util.format("invalidSignLine", index + 1));
    }
}
