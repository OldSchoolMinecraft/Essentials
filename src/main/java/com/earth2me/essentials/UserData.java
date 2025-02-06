package com.earth2me.essentials;

import java.util.logging.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import java.io.*;
import java.util.*;
import org.bukkit.*;

public abstract class UserData extends PlayerExtension implements IConf
{
    private final EssentialsConf config;
    private static final Logger logger;
    private double money;
    private Map<String, Object> homes;
    private List<Integer> unlimited;
    private Map<Integer, Object> powertools;
    private Location lastLocation;
    private long lastTeleportTimestamp;
    private long lastHealTimestamp;
    private String jail;
    private List<String> mails;
    private ItemStack[] savedInventory;
    private boolean teleportEnabled;
    private List<String> ignoredPlayers;
    private boolean godmode;
    private boolean muted;
    private long muteTimeout;
    private boolean jailed;
    private long jailTimeout;
    private long lastLogin;
    private long lastLogout;
    private boolean afk;
    private boolean newplayer;
    private String geolocation;
    private boolean isSocialSpyEnabled;
    private boolean isNPC;
    private boolean afkDetectionOn;
    
    protected UserData(final Player base, final IEssentials ess) {
        super(base, ess);
        final File folder = new File(ess.getDataFolder(), "userdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        this.config = new EssentialsConf(new File(folder, Util.sanitizeFileName(base.getName()) + ".yml"));
        this.reloadConfig();
    }
    
    @Override
    public final void reloadConfig() {
        this.config.load();
        this.money = this._getMoney();
        this.unlimited = this._getUnlimited();
        this.powertools = this._getPowertools();
        this.homes = this._getHomes();
        this.lastLocation = this._getLastLocation();
        this.lastTeleportTimestamp = this._getLastTeleportTimestamp();
        this.lastHealTimestamp = this._getLastHealTimestamp();
        this.jail = this._getJail();
        this.mails = this._getMails();
        this.savedInventory = this._getSavedInventory();
        this.teleportEnabled = this.getTeleportEnabled();
        this.ignoredPlayers = this.getIgnoredPlayers();
        this.godmode = this.getGodModeEnabled();
        this.muted = this.getMuted();
        this.muteTimeout = this._getMuteTimeout();
        this.jailed = this.getJailed();
        this.jailTimeout = this._getJailTimeout();
        this.lastLogin = this._getLastLogin();
        this.lastLogout = this._getLastLogout();
        this.afk = this.getAfk();
        this.newplayer = this.getNew();
        this.geolocation = this._getGeoLocation();
        this.isSocialSpyEnabled = this._isSocialSpyEnabled();
        this.isNPC = this._isNPC();
        this.afkDetectionOn = this._isAFKDetectionOn();
    }

    private boolean _isAFKDetectionOn()
    {
        return this.config.getBoolean("afkDetectionOn", true);
    }

    public boolean isAFKDetectionOn()
    {
        return this.afkDetectionOn;
    }

    public void setAfkDetectionOn(boolean set)
    {
        this.afkDetectionOn = set;
        this.config.setProperty("afkDetectionOn", (Object)set);
        this.config.save();
    }

    private double _getMoney() {
        double money = this.ess.getSettings().getStartingBalance();
        if (this.config.hasProperty("money")) {
            money = this.config.getDouble("money", money);
        }
        if (Math.abs(money) > this.ess.getSettings().getMaxMoney()) {
            money = ((money < 0.0) ? (-this.ess.getSettings().getMaxMoney()) : this.ess.getSettings().getMaxMoney());
        }
        return money;
    }
    
    public double getMoney() {
        return this.money;
    }
    
    public void setMoney(final double value) {
        this.money = value;
        if (Math.abs(this.money) > this.ess.getSettings().getMaxMoney()) {
            this.money = ((this.money < 0.0) ? (-this.ess.getSettings().getMaxMoney()) : this.ess.getSettings().getMaxMoney());
        }
        this.config.setProperty("money", (Object)value);
        this.config.save();
    }
    
    private Map<String, Object> _getHomes() {
        final Object o = this.config.getProperty("homes");
        if (o instanceof Map) {
            return (Map<String, Object>)o;
        }
        return new HashMap<String, Object>();
    }
    
    public Location getHome(final String name) throws Exception {
        Location loc = this.config.getLocation("homes." + name, this.getServer());
        if (loc == null) {
            try {
                loc = this.config.getLocation("homes." + this.getHomes().get(Integer.parseInt(name) - 1), this.getServer());
            }
            catch (IndexOutOfBoundsException e) {
                return null;
            }
            catch (NumberFormatException e2) {
                return null;
            }
        }
        return loc;
    }
    
    public Location getHome(final Location world) throws Exception {
        for (final String home : this.getHomes()) {
            final Location loc = this.config.getLocation("homes." + home, this.getServer());
            if (world.getWorld() == loc.getWorld()) {
                return loc;
            }
        }
        final Location loc = this.config.getLocation("homes." + this.getHomes().get(0), this.getServer());
        return loc;
    }
    
    public List<String> getHomes() {
        final List<String> list = new ArrayList<String>(this.homes.keySet());
        return list;
    }
    
    public void setHome(final String name, final Location loc) {
        this.homes.put(name, loc);
        this.config.setProperty("homes." + name, loc);
        this.config.save();
    }
    
    public void delHome(final String name) throws Exception {
        if (this.getHome(name) != null) {
            this.homes.remove(name);
            this.config.removeProperty("homes." + name);
            this.config.save();
            return;
        }
        throw new Exception("Home " + name + " doesn't exist");
    }
    
    public boolean hasHome() {
        return this.config.hasProperty("home");
    }
    
    public String getNickname() {
        return this.config.getString("nickname");
    }
    
    public void setNickname(final String nick) {
        this.config.setProperty("nickname", (Object)nick);
        this.config.save();
    }
    
    private List<Integer> _getUnlimited() {
        return (List<Integer>)this.config.getIntList("unlimited", (List)new ArrayList());
    }
    
    public List<Integer> getUnlimited() {
        return this.unlimited;
    }
    
    public boolean hasUnlimited(final ItemStack stack) {
        return this.unlimited.contains(stack.getTypeId());
    }
    
    public void setUnlimited(final ItemStack stack, final boolean state) {
        if (this.unlimited.contains(stack.getTypeId())) {
            this.unlimited.remove((Object)stack.getTypeId());
        }
        if (state) {
            this.unlimited.add(stack.getTypeId());
        }
        this.config.setProperty("unlimited", (Object)this.unlimited);
        this.config.save();
    }
    
    private Map<Integer, Object> _getPowertools() {
        final Object o = this.config.getProperty("powertools");
        if (o instanceof Map) {
            return (Map<Integer, Object>)o;
        }
        return new HashMap<Integer, Object>();
    }
    
    public List<String> getPowertool(final ItemStack stack) {
        return (List<String>) this.powertools.get(stack.getTypeId());
    }
    
    public void setPowertool(final ItemStack stack, final List<String> commandList) {
        if (commandList == null || commandList.isEmpty()) {
            this.powertools.remove(stack.getTypeId());
        }
        else {
            this.powertools.put(stack.getTypeId(), commandList);
        }
        this.config.setProperty("powertools", this.powertools);
        this.config.save();
    }
    
    private Location _getLastLocation() {
        try {
            return this.config.getLocation("lastlocation", this.getServer());
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public Location getLastLocation() {
        return this.lastLocation;
    }
    
    public void setLastLocation(final Location loc) {
        this.lastLocation = loc;
        this.config.setProperty("lastlocation", loc);
        this.config.save();
    }
    
    private long _getLastTeleportTimestamp() {
        return this.config.getLong("timestamps.lastteleport", 0L);
    }
    
    public long getLastTeleportTimestamp() {
        return this.lastTeleportTimestamp;
    }
    
    public void setLastTeleportTimestamp(final long time) {
        this.lastTeleportTimestamp = time;
        this.config.setProperty("timestamps.lastteleport", (Object)time);
        this.config.save();
    }
    
    private long _getLastHealTimestamp() {
        return this.config.getLong("timestamps.lastheal", 0L);
    }
    
    public long getLastHealTimestamp() {
        return this.lastHealTimestamp;
    }
    
    public void setLastHealTimestamp(final long time) {
        this.lastHealTimestamp = time;
        this.config.setProperty("timestamps.lastheal", (Object)time);
        this.config.save();
    }
    
    private String _getJail() {
        return this.config.getString("jail");
    }
    
    public String getJail() {
        return this.jail;
    }
    
    public void setJail(final String jail) {
        if (jail == null || jail.isEmpty()) {
            this.jail = null;
            this.config.removeProperty("jail");
        }
        else {
            this.jail = jail;
            this.config.setProperty("jail", (Object)jail);
        }
        this.config.save();
    }
    
    private List<String> _getMails() {
        return (List<String>)this.config.getStringList("mail", (List)new ArrayList());
    }
    
    public List<String> getMails() {
        return this.mails;
    }
    
    public void setMails(List<String> mails) {
        if (mails == null) {
            this.config.removeProperty("mail");
            mails = this._getMails();
        }
        else {
            this.config.setProperty("mail", (Object)mails);
        }
        this.mails = mails;
        this.config.save();
    }
    
    public void addMail(final String mail) {
        this.mails.add(mail);
        this.setMails(this.mails);
    }
    
    public ItemStack[] getSavedInventory() {
        return this.savedInventory;
    }
    
    private ItemStack[] _getSavedInventory() {
        final int size = this.config.getInt("inventory.size", 0);
        if (size < 1 || (this.getInventory() != null && size > this.getInventory().getSize())) {
            return null;
        }
        final ItemStack[] is = new ItemStack[size];
        for (int i = 0; i < size; ++i) {
            is[i] = this.config.getItemStack("inventory." + i);
        }
        return is;
    }
    
    public void setSavedInventory(final ItemStack[] is) {
        if (is == null || is.length == 0) {
            this.savedInventory = null;
            this.config.removeProperty("inventory");
        }
        else {
            this.savedInventory = is;
            this.config.setProperty("inventory.size", (Object)is.length);
            for (int i = 0; i < is.length; ++i) {
                if (is[i] != null) {
                    if (is[i].getType() != Material.AIR) {
                        this.config.setProperty("inventory." + i, is[i]);
                    }
                }
            }
        }
        this.config.save();
    }
    
    private boolean getTeleportEnabled() {
        return this.config.getBoolean("teleportenabled", true);
    }
    
    public boolean isTeleportEnabled() {
        return this.teleportEnabled;
    }
    
    public void setTeleportEnabled(final boolean set) {
        this.teleportEnabled = set;
        this.config.setProperty("teleportenabled", (Object)set);
        this.config.save();
    }
    
    public boolean toggleTeleportEnabled() {
        final boolean ret = !this.isTeleportEnabled();
        this.setTeleportEnabled(ret);
        return ret;
    }
    
    public boolean toggleSocialSpy() {
        final boolean ret = !this.isSocialSpyEnabled();
        this.setSocialSpyEnabled(ret);
        return ret;
    }
    
    public List<String> getIgnoredPlayers() {
        return (List<String>)this.config.getStringList("ignore", (List)new ArrayList());
    }
    
    public void setIgnoredPlayers(final List<String> players) {
        if (players == null || players.isEmpty()) {
            this.ignoredPlayers = new ArrayList<String>();
            this.config.removeProperty("ignore");
        }
        else {
            this.ignoredPlayers = players;
            this.config.setProperty("ignore", (Object)players);
        }
        this.config.save();
    }
    
    public boolean isIgnoredPlayer(final String name) {
        return this.ignoredPlayers.contains(name.toLowerCase());
    }
    
    public void setIgnoredPlayer(final String name, final boolean set) {
        if (set) {
            this.ignoredPlayers.add(name.toLowerCase());
        }
        else {
            this.ignoredPlayers.remove(name.toLowerCase());
        }
        this.setIgnoredPlayers(this.ignoredPlayers);
    }
    
    private boolean getGodModeEnabled() {
        return this.config.getBoolean("godmode", false);
    }
    
    public boolean isGodModeEnabled() {
        return this.godmode;
    }
    
    public void setGodModeEnabled(final boolean set) {
        this.godmode = set;
        this.config.setProperty("godmode", (Object)set);
        this.config.save();
    }
    
    public boolean toggleGodModeEnabled() {
        final boolean ret = !this.isGodModeEnabled();
        this.setGodModeEnabled(ret);
        return ret;
    }
    
    private boolean getMuted() {
        return this.config.getBoolean("muted", false);
    }
    
    public boolean isMuted() {
        return this.muted;
    }
    
    public void setMuted(final boolean set) {
        this.muted = set;
        this.config.setProperty("muted", (Object)set);
        this.config.save();
    }
    
    public boolean toggleMuted() {
        final boolean ret = !this.isMuted();
        this.setMuted(ret);
        return ret;
    }
    
    private long _getMuteTimeout() {
        return this.config.getLong("timestamps.mute", 0L);
    }
    
    public long getMuteTimeout() {
        return this.muteTimeout;
    }
    
    public void setMuteTimeout(final long time) {
        this.muteTimeout = time;
        this.config.setProperty("timestamps.mute", (Object)time);
        this.config.save();
    }
    
    private boolean getJailed() {
        return this.config.getBoolean("jailed", false);
    }
    
    public boolean isJailed() {
        return this.jailed;
    }
    
    public void setJailed(final boolean set) {
        this.jailed = set;
        this.config.setProperty("jailed", (Object)set);
        this.config.save();
    }
    
    public boolean toggleJailed() {
        final boolean ret = !this.isJailed();
        this.setJailed(ret);
        return ret;
    }
    
    private long _getJailTimeout() {
        return this.config.getLong("timestamps.jail", 0L);
    }
    
    public long getJailTimeout() {
        return this.jailTimeout;
    }
    
    public void setJailTimeout(final long time) {
        this.jailTimeout = time;
        this.config.setProperty("timestamps.jail", (Object)time);
        this.config.save();
    }
    
    public String getBanReason() {
        return this.config.getString("ban.reason");
    }
    
    public void setBanReason(final String reason) {
        this.config.setProperty("ban.reason", (Object)reason);
        this.config.save();
    }
    
    public long getBanTimeout() {
        return this.config.getLong("ban.timeout", 0L);
    }
    
    public void setBanTimeout(final long time) {
        this.config.setProperty("ban.timeout", (Object)time);
        this.config.save();
    }
    
    private long _getLastLogin() {
        return this.config.getLong("timestamps.login", 0L);
    }
    
    public long getLastLogin() {
        return this.lastLogin;
    }
    
    public void setLastLogin(final long time) {
        this.lastLogin = time;
        this.config.setProperty("timestamps.login", (Object)time);
        this.config.save();
    }
    
    private long _getLastLogout() {
        return this.config.getLong("timestamps.logout", 0L);
    }
    
    public long getLastLogout() {
        return this.lastLogout;
    }
    
    public void setLastLogout(final long time) {
        this.lastLogout = time;
        this.config.setProperty("timestamps.logout", (Object)time);
        this.config.save();
    }
    
    private boolean getAfk() {
        return this.config.getBoolean("afk", false);
    }
    
    public boolean isAfk() {
        return this.afk;
    }
    
    public void setAfk(final boolean set) {
        this.afk = set;
        this.config.setProperty("afk", (Object)set);
        this.config.save();
    }
    
    public boolean toggleAfk() {
        final boolean ret = !this.isAfk();
        this.setAfk(ret);
        return ret;
    }
    
    private boolean getNew() {
        return this.config.getBoolean("newplayer", true);
    }
    
    public boolean isNew() {
        return this.newplayer;
    }
    
    public void setNew(final boolean set) {
        this.newplayer = set;
        this.config.setProperty("newplayer", (Object)set);
        this.config.save();
    }
    
    private String _getGeoLocation() {
        return this.config.getString("geolocation");
    }
    
    public String getGeoLocation() {
        return this.geolocation;
    }
    
    public void setGeoLocation(final String geolocation) {
        if (geolocation == null || geolocation.isEmpty()) {
            this.geolocation = null;
            this.config.removeProperty("geolocation");
        }
        else {
            this.geolocation = geolocation;
            this.config.setProperty("geolocation", (Object)geolocation);
        }
        this.config.save();
    }
    
    private boolean _isSocialSpyEnabled() {
        return this.config.getBoolean("socialspy", false);
    }
    
    public boolean isSocialSpyEnabled() {
        return this.isSocialSpyEnabled;
    }
    
    public void setSocialSpyEnabled(final boolean status) {
        this.isSocialSpyEnabled = status;
        this.config.setProperty("socialspy", (Object)status);
        this.config.save();
    }
    
    private boolean _isNPC() {
        return this.config.getBoolean("npc", false);
    }
    
    public boolean isNPC() {
        return this.isNPC;
    }
    
    public void setNPC(final boolean set) {
        this.isNPC = set;
        this.config.setProperty("npc", (Object)set);
        this.config.save();
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
    }
}
