package com.earth2me.essentials.commands;

import org.bukkit.command.*;
import com.earth2me.essentials.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import java.util.*;

public class Commandptime extends EssentialsCommand
{
    public static final Set<String> getAliases;
    
    public Commandptime() {
        super("ptime");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        String userSelector = null;
        if (args.length == 2) {
            userSelector = args[1];
        }
        final Set<User> users = this.getUsers(server, sender, userSelector);
        if (args.length == 0) {
            this.getUsersTime(sender, users);
            return;
        }
        final User user = this.ess.getUser(sender);
        if ((!users.contains(user) || users.size() > 1) && user != null && !user.isAuthorized("essentials.ptime.others")) {
            user.sendMessage(Util.i18n("pTimeOthersPermission"));
            return;
        }
        String timeParam = args[0];
        Boolean relative = true;
        if (timeParam.startsWith("@")) {
            relative = false;
            timeParam = timeParam.substring(1);
        }
        if (Commandptime.getAliases.contains(timeParam)) {
            this.getUsersTime(sender, users);
            return;
        }
        Long ticks;
        if (DescParseTickFormat.meansReset(timeParam)) {
            ticks = null;
        }
        else {
            try {
                ticks = DescParseTickFormat.parse(timeParam);
            }
            catch (NumberFormatException e) {
                throw new NotEnoughArgumentsException();
            }
        }
        this.setUsersTime(sender, users, ticks, relative);
    }
    
    private void getUsersTime(final CommandSender sender, final Collection<User> users) {
        if (users.size() > 1) {
            sender.sendMessage(Util.format("pTimePlayers", new Object[0]));
        }
        for (final User user : users) {
            if (user.getPlayerTimeOffset() == 0L) {
                sender.sendMessage(Util.format("pTimeNormal", user.getName()));
            }
            else {
                final String time = DescParseTickFormat.format(user.getPlayerTime());
                if (!user.isPlayerTimeRelative()) {
                    sender.sendMessage(Util.format("pTimeCurrentFixed", user.getName(), time));
                }
                else {
                    sender.sendMessage(Util.format("pTimeCurrent", user.getName(), time));
                }
            }
        }
    }
    
    private void setUsersTime(final CommandSender sender, final Collection<User> users, final Long ticks, final Boolean relative) {
        if (ticks == null) {
            for (final User user : users) {
                user.resetPlayerTime();
            }
        }
        else {
            for (final User user : users) {
                final World world = user.getWorld();
                long time = user.getPlayerTime();
                time -= time % 24000L;
                time += 24000L + ticks;
                if (relative) {
                    time -= world.getTime();
                }
                user.setPlayerTime(time, relative);
            }
        }
        final StringBuilder msg = new StringBuilder();
        for (final User user2 : users) {
            if (msg.length() > 0) {
                msg.append(", ");
            }
            msg.append(user2.getName());
        }
        if (ticks == null) {
            sender.sendMessage(Util.format("pTimeReset", msg.toString()));
        }
        else {
            final String time2 = DescParseTickFormat.format(ticks);
            if (!relative) {
                sender.sendMessage(Util.format("pTimeSetFixed", time2, msg.toString()));
            }
            else {
                sender.sendMessage(Util.format("pTimeSet", time2, msg.toString()));
            }
        }
    }
    
    private Set<User> getUsers(final Server server, final CommandSender sender, final String selector) throws Exception {
        final Set<User> users = new TreeSet<User>(new UserNameComparator());
        if (selector == null) {
            final User user = this.ess.getUser(sender);
            if (user == null) {
                for (final Player player : server.getOnlinePlayers()) {
                    users.add(this.ess.getUser(player));
                }
            }
            else {
                users.add(user);
            }
            return users;
        }
        User user = null;
        final List<Player> matchedPlayers = (List<Player>)server.matchPlayer(selector);
        if (!matchedPlayers.isEmpty()) {
            user = this.ess.getUser(matchedPlayers.get(0));
        }
        if (user != null) {
            users.add(user);
        }
        else {
            if (!selector.equalsIgnoreCase("*") && !selector.equalsIgnoreCase("all")) {
                throw new Exception(Util.i18n("playerNotFound"));
            }
            for (final Player player2 : server.getOnlinePlayers()) {
                users.add(this.ess.getUser(player2));
            }
        }
        return users;
    }
    
    static {
        (getAliases = new HashSet<String>()).add("get");
        Commandptime.getAliases.add("list");
        Commandptime.getAliases.add("show");
        Commandptime.getAliases.add("display");
    }
}
