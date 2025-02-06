package com.earth2me.essentials;

import com.earth2me.essentials.commands.*;
import org.bukkit.*;
import java.util.*;

public interface ISettings extends IConf
{
    boolean areSignsDisabled();
    
    String format(final String p0, final IUser p1);
    
    String getAnnounceNewPlayerFormat(final IUser p0);
    
    boolean getAnnounceNewPlayers();
    
    String getBackupCommand();
    
    long getBackupInterval();
    
    boolean getBedSetsHome();
    
    String getChatFormat(final String p0);
    
    int getChatRadius();
    
    double getCommandCost(final IEssentialsCommand p0);
    
    double getCommandCost(final String p0);
    
    String getCurrencySymbol();
    
    int getDefaultStackSize();
    
    boolean getGenerateExitPortals();
    
    double getHealCooldown();
    
    Object getKit(final String p0);
    
    Map<String, Object> getKits();
    
    String getLocale();
    
    String getNetherName();
    
    boolean getNetherPortalsEnabled();
    
    double getNetherRatio();
    
    String getNewbieSpawn();
    
    String getNicknamePrefix();
    
    ChatColor getOperatorColor() throws Exception;
    
    boolean getPerWarpPermission();
    
    boolean getProtectBoolean(final String p0, final boolean p1);
    
    int getProtectCreeperMaxHeight();
    
    List<Integer> getProtectList(final String p0);
    
    boolean getProtectPreventSpawn(final String p0);
    
    String getProtectString(final String p0);
    
    boolean getReclaimSetting();
    
    boolean getRespawnAtHome();
    
    int getMultipleHomes();
    
    boolean getSortListByGroups();
    
    int getSpawnMobLimit();
    
    int getStartingBalance();
    
    double getTeleportCooldown();
    
    double getTeleportDelay();
    
    boolean hidePermissionlessHelp();
    
    boolean isCommandDisabled(final IEssentialsCommand p0);
    
    boolean isCommandDisabled(final String p0);
    
    boolean isCommandOverridden(final String p0);
    
    boolean isCommandRestricted(final IEssentialsCommand p0);
    
    boolean isCommandRestricted(final String p0);
    
    boolean isDebug();
    
    boolean isEcoDisabled();
    
    boolean isNetherEnabled();
    
    boolean isTradeInStacks(final int p0);
    
    List<Integer> itemSpawnBlacklist();
    
    boolean permissionBasedItemSpawn();
    
    boolean showNonEssCommandsInHelp();
    
    boolean spawnIfNoHome();
    
    boolean use1to1RatioInNether();
    
    boolean warnOnBuildDisallow();
    
    boolean warnOnSmite();
    
    double getMaxMoney();
    
    boolean isEcoLogEnabled();
    
    boolean removeGodOnDisconnect();
    
    boolean changeDisplayName();
    
    boolean isPlayerCommand(final String p0);
    
    boolean useBukkitPermissions();
    
    boolean addPrefixSuffix();
    
    boolean isUpdateEnabled();
    
    long getAutoAfk();
    
    long getAutoAfkKick();
    
    boolean getFreezeAfkPlayers();
}
