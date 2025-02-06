package com.earth2me.essentials;

import org.bukkit.*;
import org.bukkit.entity.*;
import java.util.logging.*;
import java.util.*;

public enum Mob
{
    CHICKEN("Chicken", Enemies.FRIENDLY, CreatureType.CHICKEN), 
    COW("Cow", Enemies.FRIENDLY, CreatureType.COW), 
    CREEPER("Creeper", Enemies.ENEMY, CreatureType.CREEPER), 
    GHAST("Ghast", Enemies.ENEMY, CreatureType.GHAST), 
    GIANT("Giant", Enemies.ENEMY, CreatureType.GIANT), 
    PIG("Pig", Enemies.FRIENDLY, CreatureType.PIG), 
    PIGZOMB("PigZombie", Enemies.NEUTRAL, CreatureType.PIG_ZOMBIE), 
    SHEEP("Sheep", Enemies.FRIENDLY, "", CreatureType.SHEEP), 
    SKELETON("Skeleton", Enemies.ENEMY, CreatureType.SKELETON), 
    SLIME("Slime", Enemies.ENEMY, CreatureType.SLIME), 
    SPIDER("Spider", Enemies.ENEMY, CreatureType.SPIDER), 
    SQUID("Squid", Enemies.FRIENDLY, CreatureType.SQUID), 
    ZOMBIE("Zombie", Enemies.ENEMY, CreatureType.ZOMBIE), 
    MONSTER("Monster", Enemies.ENEMY, CreatureType.MONSTER), 
    WOLF("Wolf", Enemies.NEUTRAL, CreatureType.WOLF);
    
    public static final Logger logger;
    public String suffix;
    public final String name;
    public final Enemies type;
    private final CreatureType bukkitType;
    private static final Map<String, Mob> hashMap;
    
    private Mob(final String n, final Enemies en, final String s, final CreatureType type) {
        this.suffix = "s";
        this.suffix = s;
        this.name = n;
        this.type = en;
        this.bukkitType = type;
    }
    
    private Mob(final String n, final Enemies en, final CreatureType type) {
        this.suffix = "s";
        this.name = n;
        this.type = en;
        this.bukkitType = type;
    }
    
    public LivingEntity spawn(final Player player, final Server server, final Location loc) throws MobException {
        final LivingEntity entity = player.getWorld().spawnCreature(loc, this.bukkitType);
        if (entity == null) {
            Mob.logger.log(Level.WARNING, Util.i18n("unableToSpawnMob"));
            throw new MobException();
        }
        return entity;
    }
    
    public CreatureType getType() {
        return this.bukkitType;
    }
    
    public static Mob fromName(final String n) {
        return Mob.hashMap.get(n);
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
        hashMap = new HashMap<String, Mob>();
        for (final Mob mob : values()) {
            Mob.hashMap.put(mob.name, mob);
        }
    }
    
    public enum Enemies
    {
        FRIENDLY("friendly"), 
        NEUTRAL("neutral"), 
        ENEMY("enemy");
        
        protected final String type;
        
        private Enemies(final String t) {
            this.type = t;
        }
    }
    
    public static class MobException extends Exception
    {
        private static final long serialVersionUID = 1L;
    }
}
