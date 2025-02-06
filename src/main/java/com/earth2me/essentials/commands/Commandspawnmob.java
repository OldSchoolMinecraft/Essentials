package com.earth2me.essentials.commands;

import com.earth2me.essentials.*;
import org.bukkit.block.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class Commandspawnmob extends EssentialsCommand
{
    public Commandspawnmob() {
        super("spawnmob");
    }
    
    public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final String[] mountparts = args[0].split(",");
        String[] parts = mountparts[0].split(":");
        String mobType = parts[0];
        mobType = (mobType.equalsIgnoreCase("PigZombie") ? "PigZombie" : Util.capitalCase(mobType));
        String mobData = null;
        if (parts.length == 2) {
            mobData = parts[1];
        }
        String mountType = null;
        String mountData = null;
        if (mountparts.length > 1) {
            parts = mountparts[1].split(":");
            mountType = parts[0];
            mountType = (mountType.equalsIgnoreCase("PigZombie") ? "PigZombie" : Util.capitalCase(mountType));
            if (parts.length == 2) {
                mountData = parts[1];
            }
        }
        if (this.ess.getSettings().getProtectPreventSpawn(mobType.toLowerCase()) || (mountType != null && this.ess.getSettings().getProtectPreventSpawn(mountType.toLowerCase()))) {
            throw new Exception(Util.i18n("unableToSpawnMob"));
        }
        Entity spawnedMob = null;
        Mob mob = null;
        Entity spawnedMount = null;
        Mob mobMount = null;
        mob = Mob.fromName(mobType);
        if (mob == null) {
            throw new Exception(Util.i18n("invalidMob"));
        }
        final int[] ignore = { 8, 9 };
        final Block block = new TargetBlock((Player)user, 300, 0.2, ignore).getTargetBlock();
        if (block == null) {
            user.sendMessage(Util.i18n("unableToSpawnMob"));
            return;
        }
        final Location loc = block.getLocation();
        final Location sloc = Util.getSafeDestination(loc);
        try {
            spawnedMob = (Entity)mob.spawn((Player)user, server, sloc);
        }
        catch (Mob.MobException e3) {
            user.sendMessage(Util.i18n("unableToSpawnMob"));
            return;
        }
        if (mountType != null) {
            mobMount = Mob.fromName(mountType);
            if (mobMount == null) {
                user.sendMessage(Util.i18n("invalidMob"));
                return;
            }
            try {
                spawnedMount = (Entity)mobMount.spawn((Player)user, server, loc);
            }
            catch (Mob.MobException e3) {
                user.sendMessage(Util.i18n("unableToSpawnMob"));
                return;
            }
            spawnedMob.setPassenger(spawnedMount);
        }
        if (mobData != null) {
            this.changeMobData(mob.name, spawnedMob, mobData, user);
        }
        if (spawnedMount != null && mountData != null) {
            this.changeMobData(mobMount.name, spawnedMount, mountData, user);
        }
        if (args.length == 2) {
            int mobCount = Integer.parseInt(args[1]);
            final int serverLimit = this.ess.getSettings().getSpawnMobLimit();
            if (mobCount > serverLimit) {
                mobCount = serverLimit;
                user.sendMessage(Util.i18n("mobSpawnLimit"));
            }
            try {
                for (int i = 1; i < mobCount; ++i) {
                    spawnedMob = (Entity)mob.spawn((Player)user, server, loc);
                    if (mobMount != null) {
                        try {
                            spawnedMount = (Entity)mobMount.spawn((Player)user, server, loc);
                        }
                        catch (Mob.MobException e4) {
                            user.sendMessage(Util.i18n("unableToSpawnMob"));
                            return;
                        }
                        spawnedMob.setPassenger(spawnedMount);
                    }
                    if (mobData != null) {
                        this.changeMobData(mob.name, spawnedMob, mobData, user);
                    }
                    if (spawnedMount != null && mountData != null) {
                        this.changeMobData(mobMount.name, spawnedMount, mountData, user);
                    }
                }
                user.sendMessage(args[1] + " " + mob.name.toLowerCase() + mob.suffix + " " + Util.i18n("spawned"));
            }
            catch (Mob.MobException e1) {
                throw new Exception(Util.i18n("unableToSpawnMob"), e1);
            }
            catch (NumberFormatException e2) {
                throw new Exception(Util.i18n("numberRequired"), e2);
            }
            catch (NullPointerException np) {
                throw new Exception(Util.i18n("soloMob"), np);
            }
        }
        else {
            user.sendMessage(mob.name + " " + Util.i18n("spawned"));
        }
    }
    
    private void changeMobData(final String type, final Entity spawned, final String data, final User user) throws Exception {
        if ("Slime".equalsIgnoreCase(type)) {
            try {
                ((Slime)spawned).setSize(Integer.parseInt(data));
            }
            catch (Exception e) {
                throw new Exception(Util.i18n("slimeMalformedSize"), e);
            }
        }
        if ("Sheep".equalsIgnoreCase(type)) {
            try {
                if (data.equalsIgnoreCase("random")) {
                    final Random rand = new Random();
                    ((Sheep)spawned).setColor(DyeColor.values()[rand.nextInt(DyeColor.values().length)]);
                }
                else {
                    ((Sheep)spawned).setColor(DyeColor.valueOf(data.toUpperCase()));
                }
            }
            catch (Exception e) {
                throw new Exception(Util.i18n("sheepMalformedColor"), e);
            }
        }
        if ("Wolf".equalsIgnoreCase(type) && data.equalsIgnoreCase("tamed")) {
            final Wolf wolf = (Wolf)spawned;
            wolf.setTamed(true);
            wolf.setOwner((AnimalTamer)user);
            wolf.setSitting(true);
        }
        if ("Wolf".equalsIgnoreCase(type) && data.equalsIgnoreCase("angry")) {
            ((Wolf)spawned).setAngry(true);
        }
        if ("Creeper".equalsIgnoreCase(type) && data.equalsIgnoreCase("powered")) {
            ((Creeper)spawned).setPowered(true);
        }
    }
}
