package com.earth2me.essentials;

import org.bukkit.plugin.java.*;
import com.earth2me.essentials.register.payment.*;
import com.earth2me.essentials.perm.*;
import java.io.*;
import com.earth2me.essentials.api.*;
import org.bukkit.event.*;
import com.earth2me.essentials.signs.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import java.util.regex.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.commands.*;
import org.bukkit.command.*;
import java.util.logging.*;
import org.bukkit.scheduler.*;
import java.util.*;
import java.math.*;

public class Essentials extends JavaPlugin implements IEssentials
{
    public static final int BUKKIT_VERSION = 1060;
    private static final Logger LOGGER;
    private transient ISettings settings;
    private final transient TNTExplodeListener tntListener;
    private transient Spawn spawn;
    private transient Jail jail;
    private transient Warps warps;
    private transient Worth worth;
    private transient List<IConf> confList;
    private transient Backup backup;
    private transient BanWorkaround bans;
    private transient ItemDb itemDb;
    private transient EssentialsUpdateTimer updateTimer;
    private final transient Methods paymentMethod;
    private static final transient boolean enableErrorLogging = false;
    private final transient EssentialsErrorHandler errorHandler;
    private transient PermissionsHandler permissionsHandler;
    private transient UserMap userMap;
    
    public Essentials() {
        this.tntListener = new TNTExplodeListener(this);
        this.paymentMethod = new Methods();
        this.errorHandler = new EssentialsErrorHandler();
    }
    
    public ISettings getSettings() {
        return this.settings;
    }
    
    public void setupForTesting(final Server server) throws IOException, InvalidDescriptionException {
        final File dataFolder = File.createTempFile("essentialstest", "");
        if (!dataFolder.delete()) {
            throw new IOException();
        }
        if (!dataFolder.mkdir()) {
            throw new IOException();
        }
        Essentials.LOGGER.log(Level.INFO, Util.i18n("usingTempFolderForTesting"));
        Essentials.LOGGER.log(Level.INFO, dataFolder.toString());
        this.initialize((PluginLoader)null, server, new PluginDescriptionFile((Reader)new FileReader(new File("src" + File.separator + "plugin.yml"))), dataFolder, (File)null, (ClassLoader)null);
        this.settings = new Settings(this);
        this.userMap = new UserMap(this);
        this.permissionsHandler = new PermissionsHandler((Plugin)this, false);
        Economy.setEss(this);
    }
    
    public void onEnable() {
        final String[] javaversion = System.getProperty("java.version").split("\\.", 3);
        if (javaversion == null || javaversion.length < 2 || Integer.parseInt(javaversion[1]) < 6) {
            Essentials.LOGGER.log(Level.SEVERE, "Java version not supported! Please install Java 1.6. You have " + System.getProperty("java.version"));
        }
        final EssentialsUpgrade upgrade = new EssentialsUpgrade(this);
        upgrade.beforeSettings();
        this.confList = new ArrayList<IConf>();
        this.settings = new Settings(this);
        this.confList.add(this.settings);
        upgrade.afterSettings();
        Util.updateLocale(this.settings.getLocale(), this);
        this.userMap = new UserMap(this);
        this.confList.add(this.userMap);
        this.spawn = new Spawn(this.getServer(), this.getDataFolder());
        this.confList.add(this.spawn);
        this.warps = new Warps(this.getServer(), this.getDataFolder());
        this.confList.add(this.warps);
        this.worth = new Worth(this.getDataFolder());
        this.confList.add(this.worth);
        this.bans = new BanWorkaround(this);
        this.confList.add(this.bans);
        this.itemDb = new ItemDb(this);
        this.confList.add(this.itemDb);
        this.reload();
        this.backup = new Backup(this);
        final PluginManager pm = this.getServer().getPluginManager();
        for (final Plugin plugin : pm.getPlugins()) {
            if (plugin.getDescription().getName().startsWith("Essentials") && !plugin.getDescription().getVersion().equals(this.getDescription().getVersion())) {
                Essentials.LOGGER.log(Level.WARNING, Util.format("versionMismatch", plugin.getDescription().getName()));
            }
        }
        final Matcher versionMatch = Pattern.compile("git-Bukkit-([0-9]+).([0-9]+).([0-9]+)-[0-9]+-[0-9a-z]+-b([0-9]+)jnks.*").matcher(this.getServer().getVersion());
        if (versionMatch.matches()) {
            final int versionNumber = Integer.parseInt(versionMatch.group(4));
            if (versionNumber < 1060) {
                Essentials.LOGGER.log(Level.WARNING, Util.i18n("notRecommendedBukkit"));
            }
        }
        else {
            Essentials.LOGGER.log(Level.INFO, Util.i18n("bukkitFormatChanged"));
        }
        this.permissionsHandler = new PermissionsHandler((Plugin)this, this.settings.useBukkitPermissions());
        final EssentialsPluginListener serverListener = new EssentialsPluginListener(this);
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, (Listener)serverListener, Event.Priority.Low, (Plugin)this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, (Listener)serverListener, Event.Priority.Low, (Plugin)this);
        this.confList.add(serverListener);
        final EssentialsPlayerListener playerListener = new EssentialsPlayerListener(this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, (Listener)playerListener, Event.Priority.Monitor, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, (Listener)playerListener, Event.Priority.Monitor, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_CHAT, (Listener)playerListener, Event.Priority.Lowest, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, (Listener)playerListener, Event.Priority.Lowest, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, (Listener)playerListener, Event.Priority.High, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_LOGIN, (Listener)playerListener, Event.Priority.High, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_TELEPORT, (Listener)playerListener, Event.Priority.High, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, (Listener)playerListener, Event.Priority.High, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_EGG_THROW, (Listener)playerListener, Event.Priority.High, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, (Listener)playerListener, Event.Priority.High, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_ANIMATION, (Listener)playerListener, Event.Priority.High, (Plugin)this);
        final EssentialsBlockListener blockListener = new EssentialsBlockListener(this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, (Listener)blockListener, Event.Priority.Lowest, (Plugin)this);
        final SignBlockListener signBlockListener = new SignBlockListener(this);
        pm.registerEvent(Event.Type.SIGN_CHANGE, (Listener)signBlockListener, Event.Priority.Highest, (Plugin)this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, (Listener)signBlockListener, Event.Priority.Low, (Plugin)this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, (Listener)signBlockListener, Event.Priority.Highest, (Plugin)this);
        pm.registerEvent(Event.Type.BLOCK_IGNITE, (Listener)signBlockListener, Event.Priority.Low, (Plugin)this);
        pm.registerEvent(Event.Type.BLOCK_BURN, (Listener)signBlockListener, Event.Priority.Low, (Plugin)this);
        pm.registerEvent(Event.Type.BLOCK_PISTON_EXTEND, (Listener)signBlockListener, Event.Priority.Low, (Plugin)this);
        pm.registerEvent(Event.Type.BLOCK_PISTON_RETRACT, (Listener)signBlockListener, Event.Priority.Low, (Plugin)this);
        final SignPlayerListener signPlayerListener = new SignPlayerListener(this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, (Listener)signPlayerListener, Event.Priority.Low, (Plugin)this);
        final SignEntityListener signEntityListener = new SignEntityListener(this);
        pm.registerEvent(Event.Type.ENTITY_EXPLODE, (Listener)signEntityListener, Event.Priority.Low, (Plugin)this);
        final EssentialsEntityListener entityListener = new EssentialsEntityListener(this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, (Listener)entityListener, Event.Priority.Lowest, (Plugin)this);
        pm.registerEvent(Event.Type.ENTITY_COMBUST, (Listener)entityListener, Event.Priority.Lowest, (Plugin)this);
        pm.registerEvent(Event.Type.ENTITY_DEATH, (Listener)entityListener, Event.Priority.Lowest, (Plugin)this);
        this.jail = new Jail(this);
        final JailPlayerListener jailPlayerListener = new JailPlayerListener(this);
        this.confList.add(this.jail);
        pm.registerEvent(Event.Type.BLOCK_BREAK, (Listener)this.jail, Event.Priority.Low, (Plugin)this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGE, (Listener)this.jail, Event.Priority.Low, (Plugin)this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, (Listener)this.jail, Event.Priority.Low, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, (Listener)jailPlayerListener, Event.Priority.Low, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, (Listener)jailPlayerListener, Event.Priority.High, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_TELEPORT, (Listener)jailPlayerListener, Event.Priority.High, (Plugin)this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, (Listener)jailPlayerListener, Event.Priority.High, (Plugin)this);
        if (this.settings.isNetherEnabled() && this.getServer().getWorlds().size() < 2) {
            this.getServer().createWorld(this.settings.getNetherName(), World.Environment.NETHER);
        }
        pm.registerEvent(Event.Type.ENTITY_EXPLODE, (Listener)this.tntListener, Event.Priority.High, (Plugin)this);
        final EssentialsTimer timer = new EssentialsTimer(this);
        this.getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)timer, 1L, 100L);
        Economy.setEss(this);
        if (this.getSettings().isUpdateEnabled()) {
            this.updateTimer = new EssentialsUpdateTimer(this);
            this.getScheduler().scheduleAsyncRepeatingTask((Plugin)this, (Runnable)this.updateTimer, 12000L, 432000L);
        }
        Essentials.LOGGER.info(Util.format("loadinfo", this.getDescription().getName(), this.getDescription().getVersion(), Util.joinList(this.getDescription().getAuthors())));
    }
    
    public void onDisable() {
        Trade.closeLog();
        Essentials.LOGGER.removeHandler(this.errorHandler);
    }
    
    public void reload() {
        Trade.closeLog();
        for (final IConf iConf : this.confList) {
            iConf.reloadConfig();
        }
        Util.updateLocale(this.settings.getLocale(), this);
        this.getConfiguration().load();
    }
    
    public String[] getMotd(final CommandSender sender, final String def) {
        return this.getLines(sender, "motd", def);
    }
    
    public String[] getLines(final CommandSender sender, final String node, final String def) {
        List<String> lines = (List<String>)this.getConfiguration().getProperty(node);
        if (lines == null) {
            return new String[0];
        }
        String[] retval = new String[lines.size()];
        Label_0143: {
            if (!lines.isEmpty()) {
                if (lines.get(0) != null) {
                    break Label_0143;
                }
            }
            try {
                lines = new ArrayList<String>();
                if (!this.getConfiguration().getString(node, def).equals("[]")) {
                    lines.add(this.getConfiguration().getString(node, def));
                    retval = new String[lines.size()];
                }
            }
            catch (Throwable ex2) {
                Essentials.LOGGER.log(Level.WARNING, Util.format("corruptNodeInConfig", node));
                return new String[0];
            }
        }
        if (lines == null || lines.isEmpty() || lines.get(0) == null) {
            return new String[0];
        }
        for (int i = 0; i < lines.size(); ++i) {
            String m = lines.get(i);
            if (m != null) {
                m = m.replace('&', '\u00A7');
                if (sender instanceof User || sender instanceof Player) {
                    final User user = this.getUser((Object)sender);
                    m = m.replace("{PLAYER}", user.getDisplayName());
                    m = m.replace("{IP}", user.getAddress().toString());
                    m = m.replace("{BALANCE}", Double.toString(user.getMoney()));
                    m = m.replace("{MAILS}", Integer.toString(user.getMails().size()));
                    m = m.replace("{WORLD}", user.getLocation().getWorld().getName());
                }
                int playerHidden = 0;
                for (final Player p : this.getServer().getOnlinePlayers()) {
                    if (this.getUser(p).isHidden()) {
                        ++playerHidden;
                    }
                }
                m = m.replace("{ONLINE}", Integer.toString(this.getServer().getOnlinePlayers().length - playerHidden));
                m = m.replace("{UNIQUE}", Integer.toString(this.userMap.getUniqueUsers()));
                if (m.matches(".*\\{PLAYERLIST\\}.*")) {
                    final StringBuilder online = new StringBuilder();
                    for (final Player p2 : this.getServer().getOnlinePlayers()) {
                        if (!this.getUser(p2).isHidden()) {
                            if (online.length() > 0) {
                                online.append(", ");
                            }
                            online.append(p2.getDisplayName());
                        }
                    }
                    m = m.replace("{PLAYERLIST}", online.toString());
                }
                if (sender instanceof Player) {
                    try {
                        final Class User = this.getClassLoader().loadClass("bukkit.Vandolis.User");
                        final Object vuser = User.getConstructor(User.class).newInstance((Player)sender);
                        m = m.replace("{RED:BALANCE}", User.getMethod("getMoney", (Class[])new Class[0]).invoke(vuser, new Object[0]).toString());
                        m = m.replace("{RED:BUYS}", User.getMethod("getNumTransactionsBuy", (Class[])new Class[0]).invoke(vuser, new Object[0]).toString());
                        m = m.replace("{RED:SELLS}", User.getMethod("getNumTransactionsSell", (Class[])new Class[0]).invoke(vuser, new Object[0]).toString());
                    }
                    catch (Throwable ex3) {
                        m = m.replace("{RED:BALANCE}", "N/A");
                        m = m.replace("{RED:BUYS}", "N/A");
                        m = m.replace("{RED:SELLS}", "N/A");
                    }
                }
                retval[i] = m + " ";
            }
        }
        return retval;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        return this.onCommandEssentials(sender, command, commandLabel, args, Essentials.class.getClassLoader(), "com.earth2me.essentials.commands.Command", "essentials.");
    }
    
    public boolean onCommandEssentials(final CommandSender sender, final Command command, final String commandLabel, final String[] args, final ClassLoader classLoader, final String commandPath, final String permissionPrefix) {
        if (!this.getSettings().isCommandOverridden(command.getName()) && !commandLabel.startsWith("e")) {
            for (final Plugin p : this.getServer().getPluginManager().getPlugins()) {
                if (!p.getDescription().getMain().contains("com.earth2me.essentials")) {
                    final PluginDescriptionFile desc = p.getDescription();
                    if (desc != null) {
                        if (desc.getName() != null) {
                            final PluginCommand pc = this.getServer().getPluginCommand(desc.getName() + ":" + commandLabel);
                            if (pc != null) {
                                return pc.execute(sender, commandLabel, args);
                            }
                        }
                    }
                }
            }
        }
        try {
            User user = null;
            if (sender instanceof Player) {
                user = this.getUser((Object)sender);
                Essentials.LOGGER.log(Level.INFO, String.format("[PLAYER_COMMAND] %s: /%s %s ", ((Player)sender).getName(), commandLabel, EssentialsCommand.getFinalArg(args, 0)));
            }
            if (user != null && !this.getSettings().isCommandDisabled("mail") && !commandLabel.equals("mail") && user.isAuthorized("essentials.mail")) {
                final List<String> mail = user.getMails();
                if (mail != null && !mail.isEmpty()) {
                    user.sendMessage(Util.format("youHaveNewMail", mail.size()));
                }
            }
            if (this.getSettings().isCommandDisabled(commandLabel)) {
                return true;
            }
            IEssentialsCommand cmd;
            try {
                cmd = (IEssentialsCommand)classLoader.loadClass(commandPath + command.getName()).newInstance();
                cmd.setEssentials(this);
            }
            catch (Exception ex) {
                sender.sendMessage(Util.format("commandNotLoaded", commandLabel));
                Essentials.LOGGER.log(Level.SEVERE, Util.format("commandNotLoaded", commandLabel), ex);
                return true;
            }
            if (user != null && !user.isAuthorized(cmd, permissionPrefix)) {
                Essentials.LOGGER.log(Level.WARNING, Util.format("deniedAccessCommand", user.getName()));
                user.sendMessage(Util.i18n("noAccessCommand"));
                return true;
            }
            try {
                if (user == null) {
                    cmd.run(this.getServer(), sender, commandLabel, command, args);
                }
                else {
                    cmd.run(this.getServer(), user, commandLabel, command, args);
                }
                return true;
            }
            catch (NoChargeException ex4) {
                return true;
            }
            catch (NotEnoughArgumentsException ex5) {
                sender.sendMessage(command.getDescription());
                sender.sendMessage(command.getUsage().replaceAll("<command>", commandLabel));
                return true;
            }
            catch (Throwable ex2) {
                this.showError(sender, ex2, commandLabel);
                return true;
            }
        }
        catch (Throwable ex3) {
            Essentials.LOGGER.log(Level.SEVERE, Util.format("commandFailed", commandLabel), ex3);
            return true;
        }
    }
    
    public void showError(final CommandSender sender, final Throwable exception, final String commandLabel) {
        sender.sendMessage(Util.format("errorWithMessage", exception.getMessage()));
        final LogRecord logRecord = new LogRecord(Level.WARNING, Util.format("errorCallingCommand", commandLabel));
        logRecord.setThrown(exception);
        if (this.getSettings().isDebug()) {
            Essentials.LOGGER.log(logRecord);
        }
    }
    
    public BukkitScheduler getScheduler() {
        return this.getServer().getScheduler();
    }
    
    public Jail getJail() {
        return this.jail;
    }
    
    public Warps getWarps() {
        return this.warps;
    }
    
    public Worth getWorth() {
        return this.worth;
    }
    
    public Backup getBackup() {
        return this.backup;
    }
    
    public Spawn getSpawn() {
        return this.spawn;
    }
    
    public User getUser(final Object base) {
        if (base instanceof Player) {
            return this.getUser((Player)base);
        }
        if (base instanceof String) {
            try {
                return this.userMap.getUser((String)base);
            }
            catch (NullPointerException ex) {
                return null;
            }
        }
        return null;
    }
    
    private <T extends Player> User getUser(final T base) {
        if (base == null) {
            return null;
        }
        if (base instanceof User) {
            return (User)base;
        }
        try {
            return this.userMap.getUser(base.getName()).update(base);
        }
        catch (NullPointerException ex) {
            return new User(base, this);
        }
    }
    
    public User getOfflineUser(final String name) {
        try {
            return this.userMap.getUser(name);
        }
        catch (NullPointerException ex) {
            return null;
        }
    }
    
    public World getWorld(final String name) {
        if (name.matches("[0-9]+")) {
            final int worldId = Integer.parseInt(name);
            if (worldId < this.getServer().getWorlds().size()) {
                return this.getServer().getWorlds().get(worldId);
            }
        }
        return this.getServer().getWorld(name);
    }
    
    public void addReloadListener(final IConf listener) {
        this.confList.add(listener);
    }
    
    public Methods getPaymentMethod() {
        return this.paymentMethod;
    }
    
    public int broadcastMessage(final IUser sender, final String message) {
        if (sender == null) {
            return this.getServer().broadcastMessage(message);
        }
        if (sender.isHidden()) {
            return 0;
        }
        final Player[] arr$;
        final Player[] players = arr$ = this.getServer().getOnlinePlayers();
        for (final Player player : arr$) {
            final User user = this.getUser(player);
            if (!user.isIgnoredPlayer(sender.getName())) {
                player.sendMessage(message);
            }
        }
        return players.length;
    }
    
    public Map<BigInteger, String> getErrors() {
        return this.errorHandler.getErrors();
    }
    
    public int scheduleAsyncDelayedTask(final Runnable run) {
        return this.getScheduler().scheduleAsyncDelayedTask((Plugin)this, run);
    }
    
    public int scheduleSyncDelayedTask(final Runnable run) {
        return this.getScheduler().scheduleSyncDelayedTask((Plugin)this, run);
    }
    
    public int scheduleSyncDelayedTask(final Runnable run, final long delay) {
        return this.getScheduler().scheduleSyncDelayedTask((Plugin)this, run, delay);
    }
    
    public int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period) {
        return this.getScheduler().scheduleSyncRepeatingTask((Plugin)this, run, delay, period);
    }
    
    public TNTExplodeListener getTNTListener() {
        return this.tntListener;
    }
    
    public PermissionsHandler getPermissionsHandler() {
        return this.permissionsHandler;
    }
    
    public BanWorkaround getBans() {
        return this.bans;
    }
    
    public ItemDb getItemDb() {
        return this.itemDb;
    }
    
    public UserMap getUserMap() {
        return this.userMap;
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
