package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;

public class Commandcompass extends EssentialsCommand
{
    public Commandcompass() {
        super("compass");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final int r = (int)user.getCorrectedYaw();
        String dir;
        if (r < 23) {
            dir = "N";
        }
        else if (r < 68) {
            dir = "NE";
        }
        else if (r < 113) {
            dir = "E";
        }
        else if (r < 158) {
            dir = "SE";
        }
        else if (r < 203) {
            dir = "S";
        }
        else if (r < 248) {
            dir = "SW";
        }
        else if (r < 293) {
            dir = "W";
        }
        else if (r < 338) {
            dir = "NW";
        }
        else {
            dir = "N";
        }
        user.sendMessage(Util.format("compassBearing", dir, r));
    }
}
