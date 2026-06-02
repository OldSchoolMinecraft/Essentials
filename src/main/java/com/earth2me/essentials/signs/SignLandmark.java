package com.earth2me.essentials.signs;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import net.oldschoolminecraft.lmk.LandmarkData;
import net.oldschoolminecraft.lmk.Landmarks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SignLandmark extends EssentialsSign
{
    public SignLandmark() {
        super("Landmark");
    }
    
    @Override
    protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException {
        this.validateTrade(sign, 3, ess);
        final String warpName = sign.getLine(1);
        if (warpName.isEmpty()) {
            sign.setLine(1, "§Landmark name!");
            return false;
        }
        try {
            ess.getWarps().getWarp(warpName);
        }
        catch (Exception ex) {
            throw new SignException(ex.getMessage(), ex);
        }
        final String group = sign.getLine(2);
        if ("Everyone".equalsIgnoreCase(group)) {
            sign.setLine(2, "§2Everyone");
        }
        return true;
    }
    
    @Override
    protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        final String lmkName = sign.getLine(1);
        final String group = sign.getLine(2);
        if ((!group.isEmpty() && ("§2Everyone".equals(group) || player.inGroup(group))) || (group.isEmpty() && (!ess.getSettings().getPerWarpPermission() || player.isAuthorized("essentials.warp." + lmkName)))) {
            try {
                LandmarkData lmkData = getLmkPlugin().getLmkManager().findLandmark(lmkName);
                player.teleport(new Location(Bukkit.getWorld(lmkData.worldName), lmkData.x, lmkData.y, lmkData.z, lmkData.yaw, lmkData.pitch));
                lmkData.registerVisit(username);
                asyncSave();
            }
            catch (Exception ex) {
                throw new SignException(ex.getMessage(), ex);
            }
            return true;
        }
        return false;
    }

    private static Landmarks lmkPlugin = null;

    private static Landmarks getLmkPlugin()
    {
        if (lmkPlugin == null)
            lmkPlugin = (Landmarks) Bukkit.getServer().getPluginManager().getPlugin("Landmarks");
        return lmkPlugin;
    }

    private static void asyncSave()
    {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(getLmkPlugin(), () -> getLmkPlugin().getLmkManager().save(), 0L);
    }
}
