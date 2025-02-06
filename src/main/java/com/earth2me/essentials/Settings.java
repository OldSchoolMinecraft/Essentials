package com.earth2me.essentials;

import java.io.*;
import com.earth2me.essentials.commands.*;
import java.util.*;
import org.bukkit.*;
import java.util.logging.*;
import org.bukkit.inventory.*;

public class Settings implements ISettings
{
    private final transient EssentialsConf config;
    private static final Logger logger;
    private final transient IEssentials ess;
    private static final double MAXMONEY = 1.0E13;
    
    public Settings(final IEssentials ess) {
        this.ess = ess;
        (this.config = new EssentialsConf(new File(ess.getDataFolder(), "config.yml"))).setTemplateName("/config.yml");
        this.reloadConfig();
    }
    
    @Override
    public boolean getRespawnAtHome() {
        return this.config.getBoolean("respawn-at-home", false);
    }
    
    @Override
    public int getMultipleHomes() {
        return this.config.getInt("multiple-homes", 5);
    }
    
    @Override
    public boolean getBedSetsHome() {
        return this.config.getBoolean("bed-sethome", false);
    }
    
    @Override
    public int getChatRadius() {
        return this.config.getInt("chat.radius", this.config.getInt("chat-radius", 0));
    }
    
    @Override
    public double getTeleportDelay() {
        return this.config.getDouble("teleport-delay", 0.0);
    }
    
    @Override
    public int getDefaultStackSize() {
        return this.config.getInt("default-stack-size", 64);
    }
    
    @Override
    public int getStartingBalance() {
        return this.config.getInt("starting-balance", 0);
    }
    
    @Override
    public boolean getNetherPortalsEnabled() {
        return this.isNetherEnabled() && this.config.getBoolean("nether.portals-enabled", false);
    }
    
    @Override
    public boolean isCommandDisabled(final IEssentialsCommand cmd) {
        return this.isCommandDisabled(cmd.getName());
    }
    
    @Override
    public boolean isCommandDisabled(final String label) {
        for (String c : (List<String>)this.config.getStringList("disabled-commands", new ArrayList(0))) {
            if (!c.equalsIgnoreCase(label)) {
                continue;
            }
            return true;
        }
        return this.config.getBoolean("disable-" + label.toLowerCase(), false);
    }
    
    @Override
    public boolean isCommandRestricted(final IEssentialsCommand cmd) {
        return this.isCommandRestricted(cmd.getName());
    }
    
    @Override
    public boolean isCommandRestricted(final String label) {
        for (final String c : (List<String>)this.config.getStringList("restricted-commands", (List)new ArrayList(0))) {
            if (!c.equalsIgnoreCase(label)) {
                continue;
            }
            return true;
        }
        return this.config.getBoolean("restrict-" + label.toLowerCase(), false);
    }
    
    @Override
    public boolean isPlayerCommand(final String label) {
        for (final String c : (List<String>)this.config.getStringList("player-commands", (List)new ArrayList(0))) {
            if (!c.equalsIgnoreCase(label)) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isCommandOverridden(final String name) {
        final List<String> defaultList = new ArrayList<String>(1);
        defaultList.add("god");
        for (final String c : (List<String>)this.config.getStringList("overridden-commands", (List)defaultList)) {
            if (!c.equalsIgnoreCase(name)) {
                continue;
            }
            return true;
        }
        return this.config.getBoolean("override-" + name.toLowerCase(), false);
    }
    
    @Override
    public double getCommandCost(final IEssentialsCommand cmd) {
        return this.getCommandCost(cmd.getName());
    }
    
    @Override
    public double getCommandCost(final String label) {
        double cost = this.config.getDouble("command-costs." + label, 0.0);
        if (cost == 0.0) {
            cost = this.config.getDouble("cost-" + label, 0.0);
        }
        return cost;
    }
    
    @Override
    public String getNicknamePrefix() {
        return this.config.getString("nickname-prefix", "~");
    }
    
    @Override
    public double getTeleportCooldown() {
        return this.config.getDouble("teleport-cooldown", 60.0);
    }
    
    @Override
    public double getHealCooldown() {
        return this.config.getDouble("heal-cooldown", 60.0);
    }
    
    @Override
    public Object getKit(final String name) {
        final Map<String, Object> kits = (Map<String, Object>)this.config.getProperty("kits");
        for (final Map.Entry<String, Object> entry : kits.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name.replace('.', '_').replace('/', '_'))) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    @Override
    public Map<String, Object> getKits() {
        return (Map<String, Object>)this.config.getProperty("kits");
    }
    
    @Override
    public ChatColor getOperatorColor() throws Exception {
        final String colorName = this.config.getString("ops-name-color", (String)null);
        if (colorName == null) {
            return ChatColor.RED;
        }
        if ("none".equalsIgnoreCase(colorName) || colorName.isEmpty()) {
            throw new Exception();
        }
        try {
            return ChatColor.valueOf(colorName.toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            return ChatColor.getByCode(Integer.parseInt(colorName, 16));
        }
    }
    
    @Override
    public boolean getReclaimSetting() {
        return this.config.getBoolean("reclaim-onlogout", true);
    }
    
    @Override
    public String getNetherName() {
        return this.config.getString("nether.folder", "nether");
    }
    
    @Override
    public boolean isNetherEnabled() {
        return this.config.getBoolean("nether.enabled", true);
    }
    
    @Override
    public int getSpawnMobLimit() {
        return this.config.getInt("spawnmob-limit", 10);
    }
    
    @Override
    public boolean showNonEssCommandsInHelp() {
        return this.config.getBoolean("non-ess-in-help", true);
    }
    
    @Override
    public boolean hidePermissionlessHelp() {
        return this.config.getBoolean("hide-permissionless-help", true);
    }
    
    @Override
    public int getProtectCreeperMaxHeight() {
        return this.config.getInt("protect.creeper.max-height", -1);
    }
    
    @Override
    public boolean areSignsDisabled() {
        return this.config.getBoolean("signs-disabled", false);
    }
    
    @Override
    public long getBackupInterval() {
        return this.config.getInt("backup.interval", 1440);
    }
    
    @Override
    public String getBackupCommand() {
        return this.config.getString("backup.command", (String)null);
    }
    
    @Override
    public String getChatFormat(final String group) {
        return this.config.getString("chat.group-formats." + ((group == null) ? "Default" : group), this.config.getString("chat.format", "&7[{GROUP}]&f {DISPLAYNAME}&7:&f {MESSAGE}"));
    }
    
    @Override
    public boolean getGenerateExitPortals() {
        return this.config.getBoolean("nether.generate-exit-portals", true);
    }
    
    @Override
    public boolean getAnnounceNewPlayers() {
        return !this.config.getString("newbies.announce-format", "-").isEmpty();
    }
    
    @Override
    public String getAnnounceNewPlayerFormat(final IUser user) {
        return this.format(this.config.getString("newbies.announce-format", "&dWelcome {DISPLAYNAME} to the server!"), user);
    }
    
    @Override
    public String format(final String format, final IUser user) {
        return format.replace('&', '§').replace("§§", "&").replace("{PLAYER}", user.getDisplayName()).replace("{DISPLAYNAME}", user.getDisplayName()).replace("{GROUP}", user.getGroup()).replace("{USERNAME}", user.getName()).replace("{ADDRESS}", user.getAddress().toString());
    }
    
    @Override
    public String getNewbieSpawn() {
        return this.config.getString("newbies.spawnpoint", "default");
    }
    
    @Override
    public boolean getPerWarpPermission() {
        return this.config.getBoolean("per-warp-permission", false);
    }
    
    @Override
    public boolean getSortListByGroups() {
        return this.config.getBoolean("sort-list-by-groups", true);
    }
    
    @Override
    public void reloadConfig() {
        this.config.load();
    }
    
    @Override
    public List<Integer> itemSpawnBlacklist() {
        final List<Integer> epItemSpwn = new ArrayList<Integer>();
        for (String itemName : this.config.getString("item-spawn-blacklist", "").split(",")) {
            itemName = itemName.trim();
            if (!itemName.isEmpty()) {
                try {
                    final ItemStack is = this.ess.getItemDb().get(itemName);
                    epItemSpwn.add(is.getTypeId());
                }
                catch (Exception ex) {
                    Settings.logger.log(Level.SEVERE, Util.format("unknownItemInList", itemName, "item-spawn-blacklist"));
                }
            }
        }
        return epItemSpwn;
    }
    
    @Override
    public boolean spawnIfNoHome() {
        return this.config.getBoolean("spawn-if-no-home", false);
    }
    
    @Override
    public boolean warnOnBuildDisallow() {
        return this.config.getBoolean("protect.disable.warn-on-build-disallow", false);
    }
    
    @Override
    public boolean use1to1RatioInNether() {
        return this.config.getBoolean("nether.use-1to1-ratio", false);
    }
    
    @Override
    public double getNetherRatio() {
        if (this.config.getBoolean("nether.use-1to1-ratio", false)) {
            return 1.0;
        }
        return this.config.getDouble("nether.ratio", 16.0);
    }
    
    @Override
    public boolean isDebug() {
        return this.config.getBoolean("debug", false);
    }
    
    @Override
    public boolean warnOnSmite() {
        return this.config.getBoolean("warn-on-smite", true);
    }
    
    @Override
    public boolean permissionBasedItemSpawn() {
        return this.config.getBoolean("permission-based-item-spawn", false);
    }
    
    @Override
    public String getLocale() {
        return this.config.getString("locale", "");
    }
    
    @Override
    public String getCurrencySymbol() {
        return this.config.getString("currency-symbol", "$").substring(0, 1).replaceAll("[0-9]", "$");
    }
    
    @Override
    public boolean isTradeInStacks(final int id) {
        return this.config.getBoolean("trade-in-stacks-" + id, false);
    }
    
    @Override
    public boolean isEcoDisabled() {
        return this.config.getBoolean("disable-eco", false);
    }
    
    @Override
    public boolean getProtectPreventSpawn(final String creatureName) {
        return this.config.getBoolean("protect.prevent.spawn." + creatureName, false);
    }
    
    @Override
    public List<Integer> getProtectList(final String configName) {
        final List<Integer> list = new ArrayList<Integer>();
        for (String itemName : this.config.getString(configName, "").split(",")) {
            itemName = itemName.trim();
            if (!itemName.isEmpty()) {
                try {
                    final ItemStack itemStack = this.ess.getItemDb().get(itemName);
                    list.add(itemStack.getTypeId());
                }
                catch (Exception ex) {
                    Settings.logger.log(Level.SEVERE, Util.format("unknownItemInList", itemName, configName));
                }
            }
        }
        return list;
    }
    
    @Override
    public String getProtectString(final String configName) {
        return this.config.getString(configName, (String)null);
    }
    
    @Override
    public boolean getProtectBoolean(final String configName, final boolean def) {
        return this.config.getBoolean(configName, def);
    }
    
    @Override
    public double getMaxMoney() {
        double max = this.config.getDouble("max-money", 1.0E13);
        if (Math.abs(max) > 1.0E13) {
            max = ((max < 0.0) ? -1.0E13 : 1.0E13);
        }
        return max;
    }
    
    @Override
    public boolean isEcoLogEnabled() {
        return this.config.getBoolean("economy-log-enabled", false);
    }
    
    @Override
    public boolean removeGodOnDisconnect() {
        return this.config.getBoolean("remove-god-on-disconnect", false);
    }
    
    @Override
    public boolean changeDisplayName() {
        return this.config.getBoolean("change-displayname", true);
    }
    
    @Override
    public boolean useBukkitPermissions() {
        return this.config.getBoolean("use-bukkit-permissions", false);
    }
    
    @Override
    public boolean addPrefixSuffix() {
        return this.config.getBoolean("add-prefix-suffix", this.ess.getServer().getPluginManager().isPluginEnabled("EssentialsChat"));
    }
    
    @Override
    public boolean isUpdateEnabled() {
        return this.config.getBoolean("update-check", false);
    }
    
    @Override
    public long getAutoAfk() {
        return this.config.getLong("auto-afk", 300L);
    }
    
    @Override
    public long getAutoAfkKick() {
        return this.config.getLong("auto-afk-kick", -1L);
    }
    
    @Override
    public boolean getFreezeAfkPlayers() {
        return this.config.getBoolean("freeze-afk-players", false);
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
    }
}
