package com.earth2me.essentials.commands;

import com.earth2me.essentials.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import java.util.*;

public class Commandkit extends EssentialsCommand
{
    private static final Map<User, Map<String, Long>> kitPlayers;
    
    public Commandkit() {
        super("kit");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            try {
                final Map<String, Object> kits = this.ess.getSettings().getKits();
                final StringBuilder list = new StringBuilder();
                for (final String k : kits.keySet()) {
                    if (user.isAuthorized("essentials.kit." + k.toLowerCase())) {
                        list.append(" ").append(k);
                    }
                }
                if (list.length() > 0) {
                    user.sendMessage(Util.format("kits", list.toString()));
                }
                else {
                    user.sendMessage(Util.i18n("noKits"));
                }
            }
            catch (Exception ex3) {
                user.sendMessage(Util.i18n("kitError"));
            }
        }
        else {
            try {
                final String kitName = args[0].toLowerCase();
                final Object kit = this.ess.getSettings().getKit(kitName);
                if (!user.isAuthorized("essentials.kit." + kitName)) {
                    user.sendMessage(Util.format("noKitPermission", "essentials.kit." + kitName));
                    return;
                }
                List<String> items;
                try {
                    final Map<String, Object> els = (Map<String, Object>)kit;
                    items = (List<String>)els.get("items");
                    final double delay = els.containsKey("delay") ? (double)els.get("delay") : 0.0;
                    final Calendar c = new GregorianCalendar();
                    c.add(13, (int)delay);
                    c.add(14, (int)(delay * 1000.0 % 1000.0));
                    final long time = c.getTimeInMillis();
                    final Calendar now = new GregorianCalendar();
                    if (!Commandkit.kitPlayers.containsKey(user)) {
                        final Map<String, Long> kitTimes = new HashMap<String, Long>();
                        kitTimes.put(kitName, time);
                        Commandkit.kitPlayers.put(user, kitTimes);
                    }
                    else {
                        final Map<String, Long> kitTimes = Commandkit.kitPlayers.get(user);
                        if (!kitTimes.containsKey(kitName)) {
                            kitTimes.put(kitName, time);
                        }
                        else {
                            if (kitTimes.get(kitName) >= now.getTimeInMillis()) {
                                user.sendMessage(Util.format("kitTimed", Util.formatDateDiff(kitTimes.get(kitName))));
                                return;
                            }
                            kitTimes.put(kitName, time);
                        }
                    }
                }
                catch (Exception ex4) {
                    items = (List<String>)kit;
                }
                final Trade charge = new Trade("kit-" + kitName, this.ess);
                try {
                    charge.isAffordableFor(user);
                }
                catch (Exception ex) {
                    user.sendMessage(ex.getMessage());
                    return;
                }
                boolean spew = false;
                for (final String d : items) {
                    final String[] parts = d.split("[^0-9]+", 3);
                    final int id = Material.getMaterial(Integer.parseInt(parts[0])).getId();
                    final int amount = (parts.length > 1) ? Integer.parseInt(parts[(parts.length > 2) ? 2 : 1]) : 1;
                    final short data = (short)((parts.length > 2) ? Short.parseShort(parts[1]) : 0);
                    final HashMap<Integer, ItemStack> overfilled = (HashMap<Integer, ItemStack>)user.getInventory().addItem(new ItemStack[] { new ItemStack(id, amount, data) });
                    for (final ItemStack itemStack : overfilled.values()) {
                        user.getWorld().dropItemNaturally(user.getLocation(), itemStack);
                        spew = true;
                    }
                }
                if (spew) {
                    user.sendMessage(Util.i18n("kitInvFull"));
                }
                try {
                    charge.charge(user);
                }
                catch (Exception ex2) {
                    user.sendMessage(ex2.getMessage());
                }
                user.sendMessage(Util.format("kitGive", kitName));
            }
            catch (Exception ex3) {
                user.sendMessage(Util.i18n("kitError2"));
                user.sendMessage(Util.i18n("kitErrorHelp"));
            }
        }
    }
    
    static {
        kitPlayers = new HashMap<User, Map<String, Long>>();
    }
}
