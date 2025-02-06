package com.earth2me.essentials;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.inventory.*;
import java.util.logging.*;
import org.bukkit.command.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import java.util.*;

public class EssentialsPlayerListener extends PlayerListener
{
    private static final Logger LOGGER;
    private final transient Server server;
    private final transient IEssentials ess;
    
    public EssentialsPlayerListener(final IEssentials parent) {
        this.ess = parent;
        this.server = parent.getServer();
    }
    
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        user.setDisplayName(user.getNick());
        this.updateCompass(user);
        if (this.ess.getSettings().changeDisplayName()) {
            user.setDisplayName(user.getNick());
        }
    }
    
    public void onPlayerChat(final PlayerChatEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        if (user.isMuted()) {
            event.setCancelled(true);
            user.sendMessage(Util.i18n("playerMuted"));
            EssentialsPlayerListener.LOGGER.info(Util.format("mutedUserSpeaks", user.getName()));
        }
        final Iterator<Player> it = event.getRecipients().iterator();
        while (it.hasNext()) {
            final User u = this.ess.getUser(it.next());
            if (u.isIgnoredPlayer(user.getName())) {
                it.remove();
            }
        }
        user.updateActivity(true);
        if (this.ess.getSettings().changeDisplayName()) {
            user.setDisplayName(user.getNick());
        }
    }
    
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final User user = this.ess.getUser(event.getPlayer());
        if (user.isAfk() && this.ess.getSettings().getFreezeAfkPlayers()) {
            final Location from = event.getFrom();
            final Location to = event.getTo().clone();
            to.setX(from.getX());
            to.setY(from.getY());
            to.setZ(from.getZ());
            try {
                event.setTo(Util.getSafeDestination(to));
            }
            catch (Exception ex2) {
                event.setTo(to);
            }
            return;
        }
        final Location afk = user.getAfkPosition();
        if (afk == null || !event.getTo().getWorld().equals(afk.getWorld()) || afk.distanceSquared(event.getTo()) > 9.0) {
            user.updateActivity(true);
        }
        if (!this.ess.getSettings().getNetherPortalsEnabled()) {
            return;
        }
        final Block block = event.getPlayer().getWorld().getBlockAt(event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ());
        final List<World> worlds = (List<World>)this.server.getWorlds();
        if (block.getType() != Material.PORTAL || worlds.size() <= 1 || !user.isAuthorized("essentials.portal")) {
            user.setJustPortaled(false);
            return;
        }
        if (user.getJustPortaled()) {
            return;
        }
        World nether = this.server.getWorld(this.ess.getSettings().getNetherName());
        if (nether == null) {
            for (final World world : worlds) {
                if (world.getEnvironment() == World.Environment.NETHER) {
                    nether = world;
                    break;
                }
            }
            if (nether == null) {
                return;
            }
        }
        final World world2 = (user.getWorld() == nether) ? worlds.get(0) : nether;
        double factor;
        if (user.getWorld().getEnvironment() == World.Environment.NETHER && world2.getEnvironment() == World.Environment.NORMAL) {
            factor = this.ess.getSettings().getNetherRatio();
        }
        else if (user.getWorld().getEnvironment() == World.Environment.NORMAL && world2.getEnvironment() == World.Environment.NETHER) {
            factor = 1.0 / this.ess.getSettings().getNetherRatio();
        }
        else {
            factor = 1.0;
        }
        Location loc = event.getTo();
        int x = loc.getBlockX();
        final int y = loc.getBlockY();
        int z = loc.getBlockZ();
        if (user.getWorld().getBlockAt(x, y, z - 1).getType() == Material.PORTAL) {
            --z;
        }
        if (user.getWorld().getBlockAt(x - 1, y, z).getType() == Material.PORTAL) {
            --x;
        }
        x *= (int)factor;
        z *= (int)factor;
        loc = new Location(world2, x + 0.5, (double)y, z + 0.5);
        final Block dest = world2.getBlockAt(x, y, z);
        NetherPortal portal = NetherPortal.findPortal(dest);
        if (portal == null) {
            if (world2.getEnvironment() == World.Environment.NETHER || this.ess.getSettings().getGenerateExitPortals()) {
                portal = NetherPortal.createPortal(dest);
                EssentialsPlayerListener.LOGGER.info(Util.format("userCreatedPortal", event.getPlayer().getName()));
                user.sendMessage(Util.i18n("generatingPortal"));
                loc = portal.getSpawn();
            }
        }
        else {
            EssentialsPlayerListener.LOGGER.info(Util.format("userUsedPortal", event.getPlayer().getName()));
            user.sendMessage(Util.i18n("usingPortal"));
            loc = portal.getSpawn();
        }
        event.setFrom(loc);
        event.setTo(loc);
        try {
            user.getTeleport().now(loc, new Trade("portal", this.ess));
        }
        catch (Exception ex) {
            user.sendMessage(ex.getMessage());
        }
        user.setJustPortaled(true);
        user.sendMessage(Util.i18n("teleportingPortal"));
        event.setCancelled(true);
    }
    
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        if (this.ess.getSettings().removeGodOnDisconnect() && user.isGodModeEnabled()) {
            user.toggleGodModeEnabled();
        }
        if (user.getSavedInventory() != null) {
            user.getInventory().setContents(user.getSavedInventory());
            user.setSavedInventory(null);
        }
        user.updateActivity(false);
        user.dispose();
        if (!this.ess.getSettings().getReclaimSetting()) {
            return;
        }
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                    final Runtime rt = Runtime.getRuntime();
                    double mem = (double)rt.freeMemory();
                    rt.runFinalization();
                    rt.gc();
                    mem = rt.freeMemory() - mem;
                    mem /= 1048576.0;
                    EssentialsPlayerListener.LOGGER.log(Level.INFO, Util.format("freedMemory", mem));
                }
                catch (InterruptedException ex) {}
            }
        });
        thread.setPriority(1);
        thread.start();
    }
    
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.ess.getBackup().onPlayerJoin();
        final User user = this.ess.getUser(event.getPlayer());
        if (user.isIpBanned()) {
            final String banReason = user.getBanReason();
            user.kickPlayer((banReason != null && !banReason.isEmpty()) ? banReason : Util.i18n("defaultBanReason"));
            return;
        }
        if (this.ess.getSettings().changeDisplayName()) {
            user.setDisplayName(user.getNick());
        }
        user.updateActivity(false);
        if (user.isAuthorized("essentials.sleepingignored")) {
            user.setSleepingIgnored(true);
        }
        if (!this.ess.getSettings().isCommandDisabled("motd") && user.isAuthorized("essentials.motd")) {
            for (final String m : this.ess.getMotd((CommandSender)user, null)) {
                if (m != null) {
                    user.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
                }
            }
        }
        if (!this.ess.getSettings().isCommandDisabled("mail") && user.isAuthorized("essentials.mail")) {
            final List<String> mail = user.getMails();
            if (mail.isEmpty()) {
                user.sendMessage(Util.i18n("noNewMail"));
            }
            else {
                user.sendMessage(Util.format("youHaveNewMail", mail.size()));
            }
        }
    }
    
    public void onPlayerLogin(final PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED && event.getResult() != PlayerLoginEvent.Result.KICK_FULL && event.getResult() != PlayerLoginEvent.Result.KICK_BANNED) {
            return;
        }
        final User user = this.ess.getUser(event.getPlayer());
        user.setNPC(false);
        final long currentTime = System.currentTimeMillis();
        user.checkBanTimeout(currentTime);
        user.checkMuteTimeout(currentTime);
        user.checkJailTimeout(currentTime);
        if (user.isBanned()) {
            final String banReason = user.getBanReason();
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, (banReason != null && !banReason.isEmpty()) ? banReason : Util.i18n("defaultBanReason"));
            return;
        }
        if (this.server.getOnlinePlayers().length >= this.server.getMaxPlayers() && !user.isAuthorized("essentials.joinfullserver")) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, Util.i18n("serverFull"));
            return;
        }
        event.allow();
        user.setLastLogin(System.currentTimeMillis());
        this.updateCompass(user);
    }
    
    private void updateCompass(final User user) {
        try {
            user.setCompassTarget(user.getHome(user.getLocation()));
        }
        catch (Exception ex) {}
    }
    
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final User user = this.ess.getUser(event.getPlayer());
        if (this.ess.getSettings().changeDisplayName()) {
            user.setDisplayName(user.getNick());
        }
        this.updateCompass(user);
    }
    
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (this.ess.getSettings().getBedSetsHome() && event.getClickedBlock().getType() == Material.BED_BLOCK) {
            try {
                final User user = this.ess.getUser(event.getPlayer());
                user.setHome();
                user.sendMessage(Util.i18n("homeSetToBed"));
            }
            catch (Throwable t) {}
        }
    }
    
    public void onPlayerEggThrow(final PlayerEggThrowEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        final ItemStack is = new ItemStack(Material.EGG, 1);
        if (user.hasUnlimited(is)) {
            user.getInventory().addItem(new ItemStack[] { is });
            user.updateInventory();
        }
    }
    
    public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        if (user.hasUnlimited(new ItemStack(event.getBucket()))) {
            event.getItemStack().setType(event.getBucket());
            this.ess.scheduleSyncDelayedTask(new Runnable() {
                @Override
                public void run() {
                    user.updateInventory();
                }
            });
        }
    }
    
    public void onPlayerAnimation(final PlayerAnimationEvent event) {
        this.usePowertools(event);
    }
    
    private void usePowertools(final PlayerAnimationEvent event) {
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) {
            return;
        }
        final User user = this.ess.getUser(event.getPlayer());
        final ItemStack is = user.getItemInHand();
        if (is == null || is.getType() == Material.AIR) {
            return;
        }
        final List<String> commandList = user.getPowertool(is);
        if (commandList == null || commandList.isEmpty()) {
            return;
        }
        for (final String command : commandList) {
            if (command.matches(".*\\{player\\}.*")) {
                continue;
            }
            if (command.startsWith("c:")) {
                for (final Player p : this.server.getOnlinePlayers()) {
                    p.sendMessage(user.getDisplayName() + ":" + command.substring(2));
                }
            }
            else {
                user.getServer().dispatchCommand((CommandSender)user, command);
            }
        }
    }
    
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final User user = this.ess.getUser(event.getPlayer());
        final String cmd = event.getMessage().toLowerCase().split(" ")[0].replace("/", "").toLowerCase();
        final List<String> commands = Arrays.asList("msg", "r", "mail", "m", "t", "emsg", "tell", "er", "reply", "ereply", "email");
        if (commands.contains(cmd)) {
            for (final Player player : this.ess.getServer().getOnlinePlayers()) {
                if (this.ess.getUser(player).isSocialSpyEnabled()) {
                    player.sendMessage(user.getDisplayName() + " : " + event.getMessage());
                }
            }
        }
        if (!cmd.equalsIgnoreCase("afk")) {
            user.updateActivity(true);
        }
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
