package com.earth2me.essentials.commands;

import java.util.logging.*;

import com.earth2me.essentials.OfflinePlayer;
import org.bukkit.*;
import org.bukkit.entity.*;
import java.util.*;
import com.earth2me.essentials.*;
import org.bukkit.command.*;

public abstract class EssentialsCommand implements IEssentialsCommand
{
    private final transient String name;
    protected transient IEssentials ess;
    protected static final Logger logger;
    
    protected EssentialsCommand(final String name) {
        this.name = name;
    }
    
    @Override
    public void setEssentials(final IEssentials ess) {
        this.ess = ess;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    protected User getPlayer(final Server server, final String[] args, final int pos) throws NoSuchFieldException, NotEnoughArgumentsException {
        return this.getPlayer(server, args, pos, false);
    }
    
    protected User getPlayer(final Server server, final String[] args, final int pos, final boolean getOffline) throws NoSuchFieldException, NotEnoughArgumentsException {
        if (args.length <= pos) {
            throw new NotEnoughArgumentsException();
        }
        final User user = this.ess.getUser(args[pos]);
        if (user == null) {
            final List<Player> matches = server.matchPlayer(args[pos]);
            if (!matches.isEmpty()) {
                for (final Player player : matches) {
                    final User userMatch = this.ess.getUser(player);
                    if (userMatch.getDisplayName().startsWith(args[pos]) && (getOffline || !userMatch.isHidden())) {
                        return userMatch;
                    }
                }
                final User userMatch2 = this.ess.getUser(matches.get(0));
                if (getOffline || !userMatch2.isHidden()) {
                    return userMatch2;
                }
            }
            throw new NoSuchFieldException(Util.i18n("playerNotFound"));
        }
        if (!getOffline && (user.getBase() instanceof OfflinePlayer || user.isHidden())) {
            throw new NoSuchFieldException(Util.i18n("playerNotFound"));
        }
        return user;
    }
    
    @Override
    public final void run(final Server server, final User user, final String commandLabel, final Command cmd, final String[] args) throws Exception {
        final Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        this.run(server, user, commandLabel, args);
        charge.charge(user);
    }
    
    protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        this.run(server, (CommandSender)user.getBase(), commandLabel, args);
    }
    
    @Override
    public final void run(final Server server, final CommandSender sender, final String commandLabel, final Command cmd, final String[] args) throws Exception {
        this.run(server, sender, commandLabel, args);
    }
    
    protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        throw new Exception(Util.format("onlyPlayers", commandLabel));
    }
    
    public static String getFinalArg(final String[] args, final int start) {
        final StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; ++i) {
            if (i != start) {
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
    }
}
