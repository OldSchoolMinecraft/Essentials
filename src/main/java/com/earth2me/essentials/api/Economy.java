package com.earth2me.essentials.api;

import java.io.*;
import java.util.logging.*;
import com.earth2me.essentials.*;
import org.bukkit.entity.*;

public final class Economy
{
    private static final Logger logger;
    private static IEssentials ess;
    
    private Economy() {
    }
    
    public static void setEss(final IEssentials aEss) {
        Economy.ess = aEss;
    }
    
    private static void createNPCFile(final String name) {
        final File folder = new File(Economy.ess.getDataFolder(), "userdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        final EssentialsConf npcConfig = new EssentialsConf(new File(folder, Util.sanitizeFileName(name) + ".yml"));
        npcConfig.load();
        npcConfig.setProperty("npc", (Object)true);
        npcConfig.setProperty("money", (Object)Economy.ess.getSettings().getStartingBalance());
        npcConfig.save();
    }
    
    private static void deleteNPC(final String name) {
        final File folder = new File(Economy.ess.getDataFolder(), "userdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        final File config = new File(folder, Util.sanitizeFileName(name) + ".yml");
        final EssentialsConf npcConfig = new EssentialsConf(config);
        npcConfig.load();
        if (npcConfig.hasProperty("npc") && npcConfig.getBoolean("npc", false)) {
            if (!config.delete()) {
                Economy.logger.log(Level.WARNING, Util.format("deleteFileError", config));
            }
            Economy.ess.getUserMap().removeUser(name);
        }
    }
    
    private static User getUserByName(final String name) {
        final Player player = Economy.ess.getServer().getPlayer(name);
        User user;
        if (player != null) {
            user = Economy.ess.getUser(player);
        }
        else {
            user = Economy.ess.getOfflineUser(name);
        }
        return user;
    }
    
    public static double getMoney(final String name) throws UserDoesNotExistException {
        final User user = getUserByName(name);
        if (user == null) {
            throw new UserDoesNotExistException(name);
        }
        return user.getMoney();
    }
    
    public static void setMoney(final String name, final double balance) throws UserDoesNotExistException, NoLoanPermittedException {
        final User user = getUserByName(name);
        if (user == null) {
            throw new UserDoesNotExistException(name);
        }
        if (balance < 0.0 && !user.isAuthorized("essentials.eco.loan")) {
            throw new NoLoanPermittedException();
        }
        user.setMoney(balance);
    }
    
    public static void add(final String name, final double amount) throws UserDoesNotExistException, NoLoanPermittedException {
        final double result = getMoney(name) + amount;
        setMoney(name, result);
    }
    
    public static void subtract(final String name, final double amount) throws UserDoesNotExistException, NoLoanPermittedException {
        final double result = getMoney(name) - amount;
        setMoney(name, result);
    }
    
    public static void divide(final String name, final double value) throws UserDoesNotExistException, NoLoanPermittedException {
        final double result = getMoney(name) / value;
        setMoney(name, result);
    }
    
    public static void multiply(final String name, final double value) throws UserDoesNotExistException, NoLoanPermittedException {
        final double result = getMoney(name) * value;
        setMoney(name, result);
    }
    
    public static void resetBalance(final String name) throws UserDoesNotExistException, NoLoanPermittedException {
        setMoney(name, Economy.ess.getSettings().getStartingBalance());
    }
    
    public static boolean hasEnough(final String name, final double amount) throws UserDoesNotExistException {
        return amount <= getMoney(name);
    }
    
    public static boolean hasMore(final String name, final double amount) throws UserDoesNotExistException {
        return amount < getMoney(name);
    }
    
    public static boolean hasLess(final String name, final double amount) throws UserDoesNotExistException {
        return amount > getMoney(name);
    }
    
    public static boolean isNegative(final String name) throws UserDoesNotExistException {
        return getMoney(name) < 0.0;
    }
    
    public static String format(final double amount) {
        return Util.formatCurrency(amount, Economy.ess);
    }
    
    public static boolean playerExists(final String name) {
        return getUserByName(name) != null;
    }
    
    public static boolean isNPC(final String name) throws UserDoesNotExistException {
        final User user = getUserByName(name);
        if (user == null) {
            throw new UserDoesNotExistException(name);
        }
        return user.isNPC();
    }
    
    public static boolean createNPC(final String name) {
        final User user = getUserByName(name);
        if (user == null) {
            createNPCFile(name);
            return true;
        }
        return false;
    }
    
    public static void removeNPC(final String name) throws UserDoesNotExistException {
        final User user = getUserByName(name);
        if (user == null) {
            throw new UserDoesNotExistException(name);
        }
        deleteNPC(name);
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
    }
}
