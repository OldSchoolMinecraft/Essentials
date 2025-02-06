package com.earth2me.essentials;

import org.bukkit.inventory.*;
import java.util.logging.*;
import java.io.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.entity.*;

public class EssentialsUpgrade
{
    private static final Logger LOGGER;
    private final transient IEssentials ess;
    private final transient EssentialsConf doneFile;
    
    EssentialsUpgrade(final IEssentials essentials) {
        this.ess = essentials;
        if (!this.ess.getDataFolder().exists()) {
            this.ess.getDataFolder().mkdirs();
        }
        (this.doneFile = new EssentialsConf(new File(this.ess.getDataFolder(), "upgrades-done.yml"))).load();
    }
    
    private void moveWorthValuesToWorthYml() {
        if (this.doneFile.getBoolean("moveWorthValuesToWorthYml", false)) {
            return;
        }
        try {
            final File configFile = new File(this.ess.getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                return;
            }
            final EssentialsConf conf = new EssentialsConf(configFile);
            conf.load();
            final Worth worth = new Worth(this.ess.getDataFolder());
            boolean found = false;
            for (final Material mat : Material.values()) {
                final int id = mat.getId();
                final double value = conf.getDouble("worth-" + id, Double.NaN);
                if (!Double.isNaN(value)) {
                    found = true;
                    worth.setPrice(new ItemStack(mat, 1, (short)0, (byte)0), value);
                }
            }
            if (found) {
                this.removeLinesFromConfig(configFile, "\\s*#?\\s*worth-[0-9]+.*", "# Worth values have been moved to worth.yml");
            }
            this.doneFile.setProperty("moveWorthValuesToWorthYml", (Object)true);
            this.doneFile.save();
        }
        catch (Throwable e) {
            EssentialsUpgrade.LOGGER.log(Level.SEVERE, Util.i18n("upgradingFilesError"), e);
        }
    }
    
    private void removeLinesFromConfig(final File file, final String regex, final String info) throws Exception {
        boolean needUpdate = false;
        final BufferedReader bReader = new BufferedReader(new FileReader(file));
        final File tempFile = File.createTempFile("essentialsupgrade", ".tmp.yml", this.ess.getDataFolder());
        final BufferedWriter bWriter = new BufferedWriter(new FileWriter(tempFile));
        while (true) {
            final String line = bReader.readLine();
            if (line == null) {
                break;
            }
            if (line.matches(regex)) {
                if (!needUpdate && info != null) {
                    bWriter.write(info, 0, info.length());
                    bWriter.newLine();
                }
                needUpdate = true;
            }
            else {
                if (line.endsWith("\r\n")) {
                    bWriter.write(line, 0, line.length() - 2);
                }
                else if (line.endsWith("\r") || line.endsWith("\n")) {
                    bWriter.write(line, 0, line.length() - 1);
                }
                else {
                    bWriter.write(line, 0, line.length());
                }
                bWriter.newLine();
            }
        }
        bReader.close();
        bWriter.close();
        if (needUpdate) {
            if (!file.renameTo(new File(file.getParentFile(), file.getName().concat("." + System.currentTimeMillis() + ".upgradebackup")))) {
                throw new Exception(Util.i18n("configFileMoveError"));
            }
            if (!tempFile.renameTo(file)) {
                throw new Exception(Util.i18n("configFileRenameError"));
            }
        }
        else {
            tempFile.delete();
        }
    }
    
    private void updateUsersToNewDefaultHome() {
        if (this.doneFile.getBoolean("updateUsersToNewDefaultHome", false)) {
            return;
        }
        final File userdataFolder = new File(this.ess.getDataFolder(), "userdata");
        if (!userdataFolder.exists() || !userdataFolder.isDirectory()) {
            return;
        }
        final File[] arr$;
        final File[] userFiles = arr$ = userdataFolder.listFiles();
        for (final File file : arr$) {
            if (file.isFile()) {
                if (file.getName().endsWith(".yml")) {
                    final EssentialsConf config = new EssentialsConf(file);
                    try {
                        config.load();
                        if (config.hasProperty("home") && !config.hasProperty("home.default")) {
                            final List<Object> vals = (List<Object>)config.getProperty("home");
                            if (vals != null) {
                                World world = this.ess.getServer().getWorlds().get(0);
                                if (vals.size() > 5) {
                                    world = this.ess.getServer().getWorld((String)vals.get(5));
                                }
                                if (world != null) {
                                    final Location loc = new Location(world, (double)vals.get(0), (double)vals.get(1), (double)vals.get(2), (float)vals.get(3), (float)vals.get(4));
                                    final String worldName = world.getName().toLowerCase();
                                    if (worldName != null && !worldName.isEmpty()) {
                                        config.removeProperty("home");
                                        config.setProperty("home.default", (Object)worldName);
                                        config.setProperty("home.worlds." + worldName, loc);
                                        config.save();
                                    }
                                }
                            }
                        }
                    }
                    catch (RuntimeException ex) {
                        EssentialsUpgrade.LOGGER.log(Level.INFO, "File: " + file.toString());
                        throw ex;
                    }
                }
            }
        }
        this.doneFile.setProperty("updateUsersToNewDefaultHome", (Object)true);
        this.doneFile.save();
    }
    
    private void updateUsersPowerToolsFormat() {
        if (this.doneFile.getBoolean("updateUsersPowerToolsFormat", false)) {
            return;
        }
        final File userdataFolder = new File(this.ess.getDataFolder(), "userdata");
        if (!userdataFolder.exists() || !userdataFolder.isDirectory()) {
            return;
        }
        final File[] arr$;
        final File[] userFiles = arr$ = userdataFolder.listFiles();
        for (final File file : arr$) {
            if (file.isFile()) {
                if (file.getName().endsWith(".yml")) {
                    final EssentialsConf config = new EssentialsConf(file);
                    try {
                        config.load();
                        if (config.hasProperty("powertools")) {
                            final Map<Integer, Object> powertools = (Map<Integer, Object>)config.getProperty("powertools");
                            if (powertools != null) {
                                for (final Map.Entry<Integer, Object> entry : powertools.entrySet()) {
                                    if (entry.getValue() instanceof String) {
                                        final List<String> temp = new ArrayList<String>();
                                        temp.add((String)entry.getValue());
                                        powertools.put(entry.getKey(), temp);
                                    }
                                }
                                config.save();
                            }
                        }
                    }
                    catch (RuntimeException ex) {
                        EssentialsUpgrade.LOGGER.log(Level.INFO, "File: " + file.toString());
                        throw ex;
                    }
                }
            }
        }
        this.doneFile.setProperty("updateUsersPowerToolsFormat", (Object)true);
        this.doneFile.save();
    }
    
    private void updateUsersHomesFormat() {
        if (this.doneFile.getBoolean("updateUsersHomesFormat", false)) {
            return;
        }
        final File userdataFolder = new File(this.ess.getDataFolder(), "userdata");
        if (!userdataFolder.exists() || !userdataFolder.isDirectory()) {
            return;
        }
        final File[] arr$;
        final File[] userFiles = arr$ = userdataFolder.listFiles();
        for (final File file : arr$) {
            if (file.isFile()) {
                if (file.getName().endsWith(".yml")) {
                    final EssentialsConf config = new EssentialsConf(file);
                    try {
                        config.load();
                        if (config.hasProperty("home") && config.hasProperty("home.default")) {
                            final String defworld = (String)config.getProperty("home.default");
                            final Location defloc = this.getFakeLocation(config, "home.worlds." + defworld);
                            if (defloc != null) {
                                config.setProperty("homes.home", defloc);
                            }
                            final List<String> worlds = (List<String>)config.getKeys("home.worlds");
                            if (worlds != null) {
                                for (final String world : worlds) {
                                    if (defworld.equalsIgnoreCase(world)) {
                                        continue;
                                    }
                                    final Location loc = this.getFakeLocation(config, "home.worlds." + world);
                                    if (loc == null) {
                                        continue;
                                    }
                                    final String worldName = loc.getWorld().getName().toLowerCase();
                                    if (worldName == null || worldName.isEmpty()) {
                                        continue;
                                    }
                                    config.setProperty("homes." + worldName, loc);
                                }
                                config.removeProperty("home");
                                config.save();
                            }
                        }
                    }
                    catch (RuntimeException ex) {
                        EssentialsUpgrade.LOGGER.log(Level.INFO, "File: " + file.toString());
                        throw ex;
                    }
                }
            }
        }
        this.doneFile.setProperty("updateUsersHomesFormat", (Object)true);
        this.doneFile.save();
    }
    
    private void moveUsersDataToUserdataFolder() {
        final File usersFile = new File(this.ess.getDataFolder(), "users.yml");
        if (!usersFile.exists()) {
            return;
        }
        final EssentialsConf usersConfig = new EssentialsConf(usersFile);
        usersConfig.load();
        for (final String username : usersConfig.getKeys((String)null)) {
            final User user = new User((Player)new OfflinePlayer(username, this.ess), this.ess);
            final String nickname = usersConfig.getString(username + ".nickname");
            if (nickname != null && !nickname.isEmpty() && !nickname.equals(username)) {
                user.setNickname(nickname);
            }
            final List<String> mails = (List<String>)usersConfig.getStringList(username + ".mail", (List)null);
            if (mails != null && !mails.isEmpty()) {
                user.setMails(mails);
            }
            if (!user.hasHome()) {
                final List<Object> vals = (List<Object>)usersConfig.getProperty(username + ".home");
                if (vals == null) {
                    continue;
                }
                World world = this.ess.getServer().getWorlds().get(0);
                if (vals.size() > 5) {
                    world = this.getFakeWorld((String)vals.get(5));
                }
                if (world == null) {
                    continue;
                }
                user.setHome("home", new Location(world, (double)vals.get(0), (double)vals.get(1), (double)vals.get(2), (float)vals.get(3), (float)vals.get(4)));
            }
        }
        usersFile.renameTo(new File(usersFile.getAbsolutePath() + ".old"));
    }
    
    private void convertWarps() {
        final File warpsFolder = new File(this.ess.getDataFolder(), "warps");
        if (!warpsFolder.exists()) {
            warpsFolder.mkdirs();
        }
        final File[] listOfFiles = warpsFolder.listFiles();
        if (listOfFiles.length >= 1) {
            for (int i = 0; i < listOfFiles.length; ++i) {
                final String filename = listOfFiles[i].getName();
                if (listOfFiles[i].isFile() && filename.endsWith(".dat")) {
                    try {
                        final BufferedReader rx = new BufferedReader(new FileReader(listOfFiles[i]));
                        double x;
                        double y;
                        double z;
                        float yaw;
                        float pitch;
                        String worldName;
                        try {
                            if (!rx.ready()) {
                                continue;
                            }
                            x = Double.parseDouble(rx.readLine().trim());
                            if (!rx.ready()) {
                                continue;
                            }
                            y = Double.parseDouble(rx.readLine().trim());
                            if (!rx.ready()) {
                                continue;
                            }
                            z = Double.parseDouble(rx.readLine().trim());
                            if (!rx.ready()) {
                                continue;
                            }
                            yaw = Float.parseFloat(rx.readLine().trim());
                            if (!rx.ready()) {
                                continue;
                            }
                            pitch = Float.parseFloat(rx.readLine().trim());
                            worldName = rx.readLine();
                        }
                        finally {
                            rx.close();
                        }
                        World w = null;
                        for (final World world : this.ess.getServer().getWorlds()) {
                            if (world.getEnvironment() != World.Environment.NETHER) {
                                w = world;
                                break;
                            }
                        }
                        if (worldName != null) {
                            worldName = worldName.trim();
                            World w2 = null;
                            w2 = this.getFakeWorld(worldName);
                            if (w2 != null) {
                                w = w2;
                            }
                        }
                        final Location loc = new Location(w, x, y, z, yaw, pitch);
                        this.ess.getWarps().setWarp(filename.substring(0, filename.length() - 4), loc);
                        if (!listOfFiles[i].renameTo(new File(warpsFolder, filename + ".old"))) {
                            throw new Exception(Util.format("fileRenameError", filename));
                        }
                    }
                    catch (Exception ex) {
                        EssentialsUpgrade.LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        final File warpFile = new File(this.ess.getDataFolder(), "warps.txt");
        if (warpFile.exists()) {
            try {
                final BufferedReader rx2 = new BufferedReader(new FileReader(warpFile));
                try {
                    String[] parts = new String[0];
                    while (rx2.ready()) {
                        if (parts.length >= 6) {
                            final String name = parts[0];
                            final double x2 = Double.parseDouble(parts[1].trim());
                            final double y2 = Double.parseDouble(parts[2].trim());
                            final double z2 = Double.parseDouble(parts[3].trim());
                            final float yaw2 = Float.parseFloat(parts[4].trim());
                            final float pitch2 = Float.parseFloat(parts[5].trim());
                            if (!name.isEmpty()) {
                                World w = null;
                                for (final World world : this.ess.getServer().getWorlds()) {
                                    if (world.getEnvironment() != World.Environment.NETHER) {
                                        w = world;
                                        break;
                                    }
                                }
                                final Location loc = new Location(w, x2, y2, z2, yaw2, pitch2);
                                this.ess.getWarps().setWarp(name, loc);
                                if (!warpFile.renameTo(new File(this.ess.getDataFolder(), "warps.txt.old"))) {
                                    throw new Exception(Util.format("fileRenameError", "warps.txt"));
                                }
                            }
                        }
                        parts = rx2.readLine().split(":");
                    }
                }
                finally {
                    rx2.close();
                }
            }
            catch (Exception ex2) {
                EssentialsUpgrade.LOGGER.log(Level.SEVERE, null, ex2);
            }
        }
    }
    
    private void sanitizeAllUserFilenames() {
        if (this.doneFile.getBoolean("sanitizeAllUserFilenames", false)) {
            return;
        }
        final File usersFolder = new File(this.ess.getDataFolder(), "userdata");
        if (!usersFolder.exists()) {
            return;
        }
        final File[] listOfFiles = usersFolder.listFiles();
        for (int i = 0; i < listOfFiles.length; ++i) {
            final String filename = listOfFiles[i].getName();
            if (listOfFiles[i].isFile()) {
                if (filename.endsWith(".yml")) {
                    final String sanitizedFilename = Util.sanitizeFileName(filename.substring(0, filename.length() - 4)) + ".yml";
                    if (!sanitizedFilename.equals(filename)) {
                        final File tmpFile = new File(listOfFiles[i].getParentFile(), sanitizedFilename + ".tmp");
                        final File newFile = new File(listOfFiles[i].getParentFile(), sanitizedFilename);
                        if (!listOfFiles[i].renameTo(tmpFile)) {
                            EssentialsUpgrade.LOGGER.log(Level.WARNING, Util.format("userdataMoveError", filename, sanitizedFilename));
                        }
                        else if (newFile.exists()) {
                            EssentialsUpgrade.LOGGER.log(Level.WARNING, Util.format("duplicatedUserdata", filename, sanitizedFilename));
                        }
                        else if (!tmpFile.renameTo(newFile)) {
                            EssentialsUpgrade.LOGGER.log(Level.WARNING, Util.format("userdataMoveBackError", sanitizedFilename, sanitizedFilename));
                        }
                    }
                }
            }
        }
        this.doneFile.setProperty("sanitizeAllUserFilenames", (Object)true);
        this.doneFile.save();
    }
    
    private World getFakeWorld(final String name) {
        final File bukkitDirectory = this.ess.getDataFolder().getParentFile().getParentFile();
        final File worldDirectory = new File(bukkitDirectory, name);
        if (worldDirectory.exists() && worldDirectory.isDirectory()) {
            return (World)new FakeWorld(worldDirectory.getName(), World.Environment.NORMAL);
        }
        return null;
    }
    
    public Location getFakeLocation(final EssentialsConf config, final String path) {
        final String worldName = config.getString(((path != null) ? (path + ".") : "") + "world");
        if (worldName == null || worldName.isEmpty()) {
            return null;
        }
        final World world = this.getFakeWorld(worldName);
        if (world == null) {
            return null;
        }
        return new Location(world, config.getDouble(((path != null) ? (path + ".") : "") + "x", 0.0), config.getDouble(((path != null) ? (path + ".") : "") + "y", 0.0), config.getDouble(((path != null) ? (path + ".") : "") + "z", 0.0), (float)config.getDouble(((path != null) ? (path + ".") : "") + "yaw", 0.0), (float)config.getDouble(((path != null) ? (path + ".") : "") + "pitch", 0.0));
    }
    
    public void beforeSettings() {
        if (!this.ess.getDataFolder().exists()) {
            this.ess.getDataFolder().mkdirs();
        }
        this.moveWorthValuesToWorthYml();
    }
    
    public void afterSettings() {
        this.sanitizeAllUserFilenames();
        this.updateUsersToNewDefaultHome();
        this.moveUsersDataToUserdataFolder();
        this.convertWarps();
        this.updateUsersPowerToolsFormat();
        this.updateUsersHomesFormat();
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
