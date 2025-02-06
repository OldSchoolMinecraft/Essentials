package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandhome extends EssentialsCommand
{
    public Commandhome() {
        super("home");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        User u = user;
        String homeName = "";
        if (args.length > 0) {
            final String[] nameParts = args[0].split(":");
            if (nameParts[0].length() == args[0].length() || !user.isAuthorized("essentials.home.others")) {
                homeName = nameParts[0];
            }
            else {
                u = this.getPlayer(server, nameParts[0].split(" "), 0, true);
                if (nameParts.length > 1) {
                    homeName = nameParts[1];
                }
            }
        }
        try {
            user.getTeleport().home(u, homeName.toLowerCase(), charge);
        }
        catch (NotEnoughArgumentsException e) {
            final List<String> homes = u.getHomes();
            if (homes.isEmpty()) {
                throw new Exception((u == user) ? Util.i18n("noHomeSet") : Util.i18n("noHomeSetPlayer"));
            }
            if (homes.size() == 1 && u == user) {
                user.getTeleport().home(u, homes.get(0), charge);
            }
            else {
                user.sendMessage(Util.format("homes", Util.joinList(homes)));
            }
        }
        throw new NoChargeException();
    }
}
