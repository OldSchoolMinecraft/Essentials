package com.earth2me.essentials.signs;

import com.earth2me.essentials.*;

public class SignTime extends EssentialsSign
{
    public SignTime() {
        super("Time");
    }
    
    @Override
    protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException {
        this.validateTrade(sign, 2, ess);
        final String timeString = sign.getLine(1);
        if ("Day".equalsIgnoreCase(timeString)) {
            sign.setLine(1, "§2Day");
            return true;
        }
        if ("Night".equalsIgnoreCase(timeString)) {
            sign.setLine(1, "§2Night");
            return true;
        }
        throw new SignException(Util.i18n("onlyDayNight"));
    }
    
    @Override
    protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        final Trade charge = this.getTrade(sign, 2, ess);
        charge.isAffordableFor(player);
        final String timeString = sign.getLine(1);
        long time = player.getWorld().getTime();
        time -= time % 24000L;
        if ("§2Day".equalsIgnoreCase(timeString)) {
            player.getWorld().setTime(time + 24000L);
            charge.charge(player);
            return true;
        }
        if ("§2Night".equalsIgnoreCase(timeString)) {
            player.getWorld().setTime(time + 37700L);
            charge.charge(player);
            return true;
        }
        throw new SignException(Util.i18n("onlyDayNight"));
    }
}
