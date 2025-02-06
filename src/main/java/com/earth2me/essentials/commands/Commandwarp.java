package com.earth2me.essentials.commands;

import org.bukkit.*;
import java.util.*;
import com.earth2me.essentials.*;

public class Commandwarp extends EssentialsCommand
{
    private static final int WARPS_PER_PAGE = 20;
    
    public Commandwarp() {
        super("warp");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length == 0 || args[0].matches("[0-9]+")) {
            if (!user.isAuthorized("essentials.warp.list")) {
                throw new Exception(Util.i18n("warpListPermission"));
            }
            final Warps warps = this.ess.getWarps();
            if (warps.isEmpty()) {
                throw new Exception(Util.i18n("noWarpsDefined"));
            }
            final List<String> warpNameList = new ArrayList<String>(warps.getWarpNames());
            final Iterator<String> iterator = warpNameList.iterator();
            while (iterator.hasNext()) {
                final String warpName = iterator.next();
                if (this.ess.getSettings().getPerWarpPermission() && !user.isAuthorized("essentials.warp." + warpName)) {
                    iterator.remove();
                }
            }
            int page = 1;
            if (args.length > 0) {
                page = Integer.parseInt(args[0]);
            }
            if (warpNameList.size() > 20) {
                user.sendMessage(Util.format("warpsCount", warpNameList.size(), page, (int)Math.ceil(warpNameList.size() / 20.0)));
            }
            final int warpPage = (page - 1) * 20;
            user.sendMessage(Util.joinList(warpNameList.subList(warpPage, warpPage + Math.min(warpNameList.size() - warpPage, 20))));
            throw new NoChargeException();
        }
        else {
            if (args.length <= 0) {
                return;
            }
            User otherUser = null;
            if (args.length != 2 || !user.isAuthorized("essentials.warp.otherplayers")) {
                this.warpUser(user, args[0]);
                throw new NoChargeException();
            }
            otherUser = this.ess.getUser(server.getPlayer(args[1]));
            if (otherUser == null) {
                throw new Exception(Util.i18n("playerNotFound"));
            }
            this.warpUser(otherUser, args[0]);
            throw new NoChargeException();
        }
    }
    
    private void warpUser(final User user, final String name) throws Exception {
        final Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        if (!this.ess.getSettings().getPerWarpPermission()) {
            user.getTeleport().warp(name, charge);
            return;
        }
        if (user.isAuthorized("essentials.warp." + name)) {
            user.getTeleport().warp(name, charge);
            return;
        }
        throw new Exception(Util.i18n("warpUsePermission"));
    }
}
