package com.earth2me.essentials.commands;

import org.bukkit.command.*;
import org.bukkit.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandtime extends EssentialsCommand
{
    public Commandtime() {
        super("time");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        String worldSelector = null;
        if (args.length == 2) {
            worldSelector = args[1];
        }
        final Set<World> worlds = this.getWorlds(server, sender, worldSelector);
        if (args.length == 0) {
            this.getWorldsTime(sender, worlds);
            return;
        }
        final User user = this.ess.getUser(sender);
        if (user != null && !user.isAuthorized("essentials.time.set")) {
            user.sendMessage(Util.i18n("timeSetPermission"));
            return;
        }
        long ticks;
        try {
            ticks = DescParseTickFormat.parse(args[0]);
        }
        catch (NumberFormatException e) {
            throw new NotEnoughArgumentsException();
        }
        this.setWorldsTime(sender, worlds, ticks);
    }
    
    private void getWorldsTime(final CommandSender sender, final Collection<World> worlds) {
        if (worlds.size() == 1) {
            final Iterator<World> iter = worlds.iterator();
            sender.sendMessage(DescParseTickFormat.format(iter.next().getTime()));
            return;
        }
        for (final World world : worlds) {
            sender.sendMessage(Util.format("timeCurrentWorld", world.getName(), DescParseTickFormat.format(world.getTime())));
        }
    }
    
    private void setWorldsTime(final CommandSender sender, final Collection<World> worlds, final long ticks) {
        for (final World world : worlds) {
            long time = world.getTime();
            time -= time % 24000L;
            world.setTime(time + 24000L + ticks);
        }
        final StringBuilder msg = new StringBuilder();
        final boolean first = true;
        for (final World world2 : worlds) {
            if (msg.length() > 0) {
                msg.append(", ");
            }
            msg.append(world2.getName());
        }
        sender.sendMessage(Util.format("timeWorldSet", DescParseTickFormat.format(ticks), msg.toString()));
    }
    
    private Set<World> getWorlds(final Server server, final CommandSender sender, final String selector) throws Exception {
        final Set<World> worlds = new TreeSet<World>(new WorldNameComparator());
        if (selector == null) {
            final User user = this.ess.getUser(sender);
            if (user == null) {
                worlds.addAll(server.getWorlds());
            }
            else {
                worlds.add(user.getWorld());
            }
            return worlds;
        }
        final World world = server.getWorld(selector);
        if (world != null) {
            worlds.add(world);
        }
        else {
            if (!selector.equalsIgnoreCase("*") && !selector.equalsIgnoreCase("all")) {
                throw new Exception(Util.i18n("invalidWorld"));
            }
            worlds.addAll(server.getWorlds());
        }
        return worlds;
    }
}
