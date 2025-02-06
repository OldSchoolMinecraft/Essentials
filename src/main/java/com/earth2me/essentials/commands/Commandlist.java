package com.earth2me.essentials.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandlist extends EssentialsCommand
{
    public Commandlist() {
        super("list");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        boolean showhidden = false;
        if (sender instanceof Player) {
            if (this.ess.getUser(sender).isAuthorized("essentials.list.hidden")) {
                showhidden = true;
            }
        }
        else {
            showhidden = true;
        }
        int playerHidden = 0;
        for (final Player p : server.getOnlinePlayers()) {
            if (this.ess.getUser(p).isHidden()) {
                ++playerHidden;
            }
        }
        final StringBuilder online = new StringBuilder();
        online.append(ChatColor.BLUE).append("There are ").append(ChatColor.RED).append(server.getOnlinePlayers().length - playerHidden);
        if (showhidden && playerHidden > 0) {
            online.append(ChatColor.GRAY).append("/").append(playerHidden);
        }
        online.append(ChatColor.BLUE).append(" out of a maximum ").append(ChatColor.RED).append(server.getMaxPlayers());
        online.append(ChatColor.BLUE).append(" players online.");
        sender.sendMessage(online.toString());
        if (this.ess.getSettings().getSortListByGroups()) {
            final Map<String, List<User>> sort = new HashMap<String, List<User>>();
            for (final Player p2 : server.getOnlinePlayers()) {
                final User u = this.ess.getUser(p2);
                if (!u.isHidden() || showhidden) {
                    final String group = u.getGroup();
                    List<User> list = sort.get(group);
                    if (list == null) {
                        list = new ArrayList<User>();
                        sort.put(group, list);
                    }
                    list.add(u);
                }
            }
            final String[] groups = sort.keySet().toArray(new String[0]);
            Arrays.sort(groups, String.CASE_INSENSITIVE_ORDER);
            for (final String group2 : groups) {
                final StringBuilder groupString = new StringBuilder();
                groupString.append(group2).append(": ");
                final List<User> users = sort.get(group2);
                Collections.sort(users);
                boolean first = true;
                for (final User user : users) {
                    if (!first) {
                        groupString.append(", ");
                    }
                    else {
                        first = false;
                    }
                    if (user.isAfk()) {
                        groupString.append("§7[AFK]§f");
                    }
                    if (user.isHidden()) {
                        groupString.append("§7[HIDDEN]§f");
                    }
                    groupString.append(user.getDisplayName());
                    groupString.append("§f");
                }
                sender.sendMessage(groupString.toString());
            }
        }
        else {
            final List<User> users2 = new ArrayList<User>();
            for (final Player p2 : server.getOnlinePlayers()) {
                final User u = this.ess.getUser(p2);
                if (!u.isHidden() || showhidden) {
                    users2.add(u);
                }
            }
            Collections.sort(users2);
            final StringBuilder onlineUsers = new StringBuilder();
            onlineUsers.append(Util.i18n("connectedPlayers"));
            boolean first2 = true;
            for (final User user2 : users2) {
                if (!first2) {
                    onlineUsers.append(", ");
                }
                else {
                    first2 = false;
                }
                if (user2.isAfk()) {
                    onlineUsers.append("§7[AFK]§f");
                }
                if (user2.isHidden()) {
                    onlineUsers.append("§7[HIDDEN]§f");
                }
                onlineUsers.append(user2.getDisplayName());
                onlineUsers.append("§f");
            }
            sender.sendMessage(onlineUsers.toString());
        }
    }
}
