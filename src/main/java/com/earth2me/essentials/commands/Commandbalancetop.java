package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandbalancetop extends EssentialsCommand
{
    public Commandbalancetop() {
        super("balancetop");
    }
    
    @Override
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        int max = 10;
        if (args.length > 0) {
            try {
                if (Integer.parseInt(args[0]) < 10) {
                    max = Integer.parseInt(args[0]);
                }
            }
            catch (NumberFormatException ex) {}
        }
        final Map<User, Double> balances = new HashMap<User, Double>();
        for (final User u : this.ess.getUserMap().getAllUsers()) {
            balances.put(u, u.getMoney());
        }
        final List<Map.Entry<User, Double>> sortedEntries = new ArrayList<Map.Entry<User, Double>>(balances.entrySet());
        Collections.sort(sortedEntries, new Comparator<Map.Entry<User, Double>>() {
            @Override
            public int compare(final Map.Entry<User, Double> entry1, final Map.Entry<User, Double> entry2) {
                return -entry1.getValue().compareTo(entry2.getValue());
            }
        });
        int count = 0;
        sender.sendMessage(Util.format("balanceTop", max));
        for (final Map.Entry<User, Double> entry : sortedEntries) {
            if (count == max) {
                break;
            }
            sender.sendMessage(entry.getKey().getDisplayName() + ", " + Util.formatCurrency(entry.getValue(), this.ess));
            ++count;
        }
    }
}
