package com.earth2me.essentials.signs;

import com.earth2me.essentials.commands.*;
import com.earth2me.essentials.*;

public class SignSpawnmob extends EssentialsSign
{
    public SignSpawnmob() {
        super("Spawnmob");
    }
    
    @Override
    protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        this.validateInteger(sign, 1);
        this.validateTrade(sign, 3, ess);
        return true;
    }
    
    @Override
    protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        final Trade charge = this.getTrade(sign, 3, ess);
        charge.isAffordableFor(player);
        final Commandspawnmob command = new Commandspawnmob();
        command.setEssentials(ess);
        final String[] args = { sign.getLine(2), sign.getLine(1) };
        try {
            command.run(ess.getServer(), player, "spawnmob", args);
        }
        catch (Exception ex) {
            throw new SignException(ex.getMessage(), ex);
        }
        charge.charge(player);
        return true;
    }
}
