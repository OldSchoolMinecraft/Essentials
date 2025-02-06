package com.earth2me.essentials.commands;

import com.earth2me.essentials.*;
import org.bukkit.*;

public class Commandgetpos extends EssentialsCommand
{
    public Commandgetpos() {
        super("getpos");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final Location coords = user.getLocation();
        user.sendMessage("§7X: " + coords.getBlockX() + " (-North <-> +South)");
        user.sendMessage("§7Y: " + coords.getBlockY() + " (+Up <-> -Down)");
        user.sendMessage("§7Z: " + coords.getBlockZ() + " (+East <-> -West)");
        user.sendMessage("§7Yaw: " + user.getCorrectedYaw() + " (Rotation)");
        user.sendMessage("§7Pitch: " + coords.getPitch() + " (Head angle)");
    }
}
