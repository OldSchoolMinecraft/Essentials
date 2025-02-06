package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandsethome extends EssentialsCommand
{
    public Commandsethome() {
        super("sethome");
    }

    private boolean isValidHomeName(String name) {
        return name.matches("^[a-zA-Z0-9]+$");
    }
    
    public void run(final Server server, final User user, final String commandLabel, String[] args) throws Exception {
        if (args.length > 0) {
            final String[] nameParts = args[0].split(":");
            if (nameParts[0].length() != args[0].length()) {
                args = nameParts;
            }
            if (!this.isValidHomeName(args[0])) {
                throw new Exception("Invalid home name. Home names must be alphanumeric.");
            }
            if (args.length < 2) {
                if (!user.isAuthorized("essentials.sethome.multiple")) {
                    throw new Exception(Util.format("maxHomes", 1));
                }
                if (!user.isAuthorized("essentials.sethome.multiple.unlimited") && user.getHomes().size() >= this.ess.getSettings().getMultipleHomes() && !user.getHomes().contains(args[0].toLowerCase())) {
                    throw new Exception(Util.format("maxHomes", this.ess.getSettings().getMultipleHomes()));
                }
                user.setHome(args[0].toLowerCase());
            }
            else if (user.isAuthorized("essentials.sethome.others")) {
                User usersHome = this.ess.getUser(this.ess.getServer().getPlayer(args[0]));
                if (usersHome == null) {
                    usersHome = this.ess.getOfflineUser(args[0]);
                }
                if (usersHome == null) {
                    throw new Exception(Util.i18n("playerNotFound"));
                }
                String name = args[1].toLowerCase();
                if (!user.isAuthorized("essentials.sethome.multiple")) {
                    name = "home";
                }
                usersHome.setHome(name, user.getLocation());
            }
        }
        else {
            user.setHome();
        }
        user.sendMessage(Util.i18n("homeSet"));
    }
}
