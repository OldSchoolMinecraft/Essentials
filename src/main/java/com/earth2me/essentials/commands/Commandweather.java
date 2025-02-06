package com.earth2me.essentials.commands;

import com.earth2me.essentials.*;
import org.bukkit.*;

public class Commandweather extends EssentialsCommand
{
    public Commandweather() {
        super("weather");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final boolean isStorm = args[0].equalsIgnoreCase("storm");
        final World world = user.getWorld();
        if (args.length > 1) {
            world.setStorm(isStorm);
            world.setWeatherDuration(Integer.parseInt(args[1]) * 20);
            user.sendMessage(isStorm ? Util.format("weatherStormFor", args[1]) : Util.format("weatherSunFor", args[1]));
            return;
        }
        world.setStorm(isStorm);
        user.sendMessage(isStorm ? Util.i18n("weatherStorm") : Util.i18n("weatherSun"));
    }
}
