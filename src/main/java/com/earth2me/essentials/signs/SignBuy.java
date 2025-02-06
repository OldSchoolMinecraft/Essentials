package com.earth2me.essentials.signs;

import com.earth2me.essentials.*;

public class SignBuy extends EssentialsSign
{
    public SignBuy() {
        super("Buy");
    }
    
    @Override
    protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException {
        this.validateTrade(sign, 1, 2, player, ess);
        this.validateTrade(sign, 3, ess);
        return true;
    }
    
    @Override
    protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        final Trade items = this.getTrade(sign, 1, 2, player, ess);
        final Trade charge = this.getTrade(sign, 3, ess);
        charge.isAffordableFor(player);
        items.pay(player);
        charge.charge(player);
        Trade.log("Sign", "Buy", "Interact", username, charge, username, items, sign.getBlock().getLocation(), ess);
        return true;
    }
}
