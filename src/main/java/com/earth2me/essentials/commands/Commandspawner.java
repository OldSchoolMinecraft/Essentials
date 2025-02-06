package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.block.*;

public class Commandspawner extends EssentialsCommand
{
    public Commandspawner() {
        super("spawner");
    }
    
    @Override
    protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1 || args[0].length() < 2) {
            throw new NotEnoughArgumentsException();
        }
        final Block target = user.getTarget().getTargetBlock();
        if (target.getType() != Material.MOB_SPAWNER) {
            throw new Exception(Util.i18n("mobSpawnTarget"));
        }
        try {
            String name = args[0];
            name = (name.equalsIgnoreCase("PigZombie") ? "PigZombie" : Util.capitalCase(name));
            Mob mob = null;
            mob = Mob.fromName(name);
            if (mob == null) {
                user.sendMessage(Util.i18n("invalidMob"));
                return;
            }
            ((CreatureSpawner)target.getState()).setCreatureType(mob.getType());
            user.sendMessage(Util.format("setSpawner", mob.name));
        }
        catch (Throwable ex) {
            throw new Exception(Util.i18n("mobSpawnError"), ex);
        }
    }
}
