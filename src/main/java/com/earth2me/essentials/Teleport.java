package com.earth2me.essentials;

import java.util.logging.*;
import org.bukkit.command.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.commands.*;

public class Teleport implements Runnable
{
    private static final double MOVE_CONSTANT = 0.3;
    private IUser user;
    private int teleTimer;
    private long started;
    private long delay;
    private int health;
    private long initX;
    private long initY;
    private long initZ;
    private Target teleportTarget;
    private Trade chargeFor;
    private final IEssentials ess;
    private static final Logger logger;
    
    private void initTimer(final long delay, final Target target, final Trade chargeFor) {
        this.started = System.currentTimeMillis();
        this.delay = delay;
        this.health = this.user.getHealth();
        this.initX = Math.round(this.user.getLocation().getX() * 0.3);
        this.initY = Math.round(this.user.getLocation().getY() * 0.3);
        this.initZ = Math.round(this.user.getLocation().getZ() * 0.3);
        this.teleportTarget = target;
        this.chargeFor = chargeFor;
    }
    
    @Override
    public void run() {
        if (this.user == null || !this.user.isOnline() || this.user.getLocation() == null) {
            this.cancel();
            return;
        }
        if (Math.round(this.user.getLocation().getX() * 0.3) != this.initX || Math.round(this.user.getLocation().getY() * 0.3) != this.initY || Math.round(this.user.getLocation().getZ() * 0.3) != this.initZ || this.user.getHealth() < this.health) {
            this.cancel(true);
            return;
        }
        this.health = this.user.getHealth();
        final long now = System.currentTimeMillis();
        if (now > this.started + this.delay) {
            try {
                this.cooldown(false);
                this.user.sendMessage(Util.i18n("teleportationCommencing"));
                try {
                    this.now(this.teleportTarget);
                    if (this.chargeFor != null) {
                        this.chargeFor.charge(this.user);
                    }
                }
                catch (Throwable ex) {
                    this.ess.showError((CommandSender)this.user.getBase(), ex, "teleport");
                }
            }
            catch (Exception ex2) {
                this.user.sendMessage(Util.format("cooldownWithMessage", ex2.getMessage()));
            }
        }
    }
    
    public Teleport(final IUser user, final IEssentials ess) {
        this.teleTimer = -1;
        this.user = user;
        this.ess = ess;
    }
    
    public void respawn(final Spawn spawn, final Trade chargeFor) throws Exception {
        this.teleport(new Target(spawn.getSpawn(this.user.getGroup())), chargeFor);
    }
    
    public void warp(final String warp, final Trade chargeFor) throws Exception {
        final Location loc = this.ess.getWarps().getWarp(warp);
        this.teleport(new Target(loc), chargeFor);
        this.user.sendMessage(Util.format("warpingTo", warp));
    }
    
    public void cooldown(final boolean check) throws Exception {
        final Calendar now = new GregorianCalendar();
        if (this.user.getLastTeleportTimestamp() > 0L) {
            final double cooldown = this.ess.getSettings().getTeleportCooldown();
            final Calendar cooldownTime = new GregorianCalendar();
            cooldownTime.setTimeInMillis(this.user.getLastTeleportTimestamp());
            cooldownTime.add(13, (int)cooldown);
            cooldownTime.add(14, (int)(cooldown * 1000.0 % 1000.0));
            if (cooldownTime.after(now) && !this.user.isAuthorized("essentials.teleport.cooldown.bypass")) {
                throw new Exception(Util.format("timeBeforeTeleport", Util.formatDateDiff(cooldownTime.getTimeInMillis())));
            }
        }
        if (!check) {
            this.user.setLastTeleportTimestamp(now.getTimeInMillis());
        }
    }
    
    public void cancel(final boolean notifyUser) {
        if (this.teleTimer == -1) {
            return;
        }
        try {
            this.ess.getServer().getScheduler().cancelTask(this.teleTimer);
            if (notifyUser) {
                this.user.sendMessage(Util.i18n("pendingTeleportCancelled"));
            }
        }
        finally {
            this.teleTimer = -1;
        }
    }
    
    public void cancel() {
        this.cancel(false);
    }
    
    public void teleport(final Location loc, final Trade chargeFor) throws Exception {
        this.teleport(new Target(loc), chargeFor);
    }
    
    public void teleport(final Entity entity, final Trade chargeFor) throws Exception {
        this.teleport(new Target(entity), chargeFor);
    }
    
    private void teleport(final Target target, final Trade chargeFor) throws Exception {
        final double delay = this.ess.getSettings().getTeleportDelay();
        if (chargeFor != null) {
            chargeFor.isAffordableFor(this.user);
        }
        this.cooldown(true);
        if (delay <= 0.0 || this.user.isAuthorized("essentials.teleport.timer.bypass")) {
            this.cooldown(false);
            this.now(target);
            if (chargeFor != null) {
                chargeFor.charge(this.user);
            }
            return;
        }
        this.cancel();
        final Calendar c = new GregorianCalendar();
        c.add(13, (int)delay);
        c.add(14, (int)(delay * 1000.0 % 1000.0));
        this.user.sendMessage(Util.format("dontMoveMessage", Util.formatDateDiff(c.getTimeInMillis())));
        this.initTimer((long)(delay * 1000.0), target, chargeFor);
        this.teleTimer = this.ess.scheduleSyncRepeatingTask(this, 10L, 10L);
    }
    
    private void now(final Target target) throws Exception {
        this.cancel();
        this.user.setLastLocation();
        this.user.getBase().teleport(Util.getSafeDestination(target.getLocation()));
    }
    
    public void now(final Location loc) throws Exception {
        this.cooldown(false);
        this.now(new Target(loc));
    }
    
    public void now(final Location loc, final Trade chargeFor) throws Exception {
        this.cooldown(false);
        chargeFor.charge(this.user);
        this.now(new Target(loc));
    }
    
    public void now(final Entity entity, final boolean cooldown) throws Exception {
        if (cooldown) {
            this.cooldown(false);
        }
        this.now(new Target(entity));
    }
    
    public void back(final Trade chargeFor) throws Exception {
        this.teleport(new Target(this.user.getLastLocation()), chargeFor);
    }
    
    public void back() throws Exception {
        this.back(null);
    }
    
    public void home(final IUser user, final String home, final Trade chargeFor) throws Exception {
        final Location loc = user.getHome(home);
        if (loc == null) {
            if (!this.ess.getSettings().spawnIfNoHome()) {
                throw new NotEnoughArgumentsException();
            }
            this.respawn(this.ess.getSpawn(), chargeFor);
        }
        this.teleport(new Target(loc), chargeFor);
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
    }
    
    private static class Target
    {
        private final Location location;
        private final Entity entity;
        
        public Target(final Location location) {
            this.location = location;
            this.entity = null;
        }
        
        public Target(final Entity entity) {
            this.entity = entity;
            this.location = null;
        }
        
        public Location getLocation() {
            if (this.entity != null) {
                return this.entity.getLocation();
            }
            return this.location;
        }
    }
}
