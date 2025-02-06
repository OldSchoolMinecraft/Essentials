package com.earth2me.essentials;

import net.minecraft.server.*;
import java.util.*;
import org.bukkit.craftbukkit.*;
import java.util.logging.*;
import java.io.*;

public class BanWorkaround implements IConf
{
    private final transient IEssentials ess;
    private final transient ServerConfigurationManager scm;
    private static final Logger LOGGER;
    private final transient Set<String> bans;
    private final transient Set<String> bannedIps;
    
    public BanWorkaround(final IEssentials ess) {
        this.bans = new HashSet<String>();
        this.bannedIps = new HashSet<String>();
        this.ess = ess;
        this.scm = ((CraftServer)ess.getServer()).getHandle();
    }
    
    public void banByName(final String name) {
        this.scm.a(name);
        this.reloadConfig();
    }
    
    public void unbanByName(final String name) {
        this.scm.b(name);
        this.reloadConfig();
    }
    
    public void banByIp(final String ip) {
        this.scm.c(ip);
        this.reloadConfig();
    }
    
    public void unbanByIp(final String ip) {
        this.scm.d(ip);
        this.reloadConfig();
    }
    
    public boolean isNameBanned(final String name) {
        return this.bans.contains(name.toLowerCase());
    }
    
    public boolean isIpBanned(final String ip) {
        return this.bannedIps.contains(ip.toLowerCase());
    }
    
    @Override
    public void reloadConfig() {
        final File file = new File(this.ess.getDataFolder().getParentFile().getParentFile(), "banned-players.txt");
        try {
            if (!file.exists()) {
                throw new FileNotFoundException(Util.i18n("bannedPlayersFileNotFound"));
            }
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            try {
                this.bans.clear();
                while (bufferedReader.ready()) {
                    final String line = bufferedReader.readLine().trim().toLowerCase();
                    if (line.length() > 0 && line.charAt(0) == '#') {
                        continue;
                    }
                    this.bans.add(line);
                }
            }
            catch (IOException io) {
                BanWorkaround.LOGGER.log(Level.SEVERE, Util.i18n("bannedPlayersFileError"), io);
                try {
                    bufferedReader.close();
                }
                catch (IOException ex) {
                    BanWorkaround.LOGGER.log(Level.SEVERE, Util.i18n("bannedPlayersFileError"), ex);
                }
            }
            finally {
                try {
                    bufferedReader.close();
                }
                catch (IOException ex2) {
                    BanWorkaround.LOGGER.log(Level.SEVERE, Util.i18n("bannedPlayersFileError"), ex2);
                }
            }
        }
        catch (FileNotFoundException ex3) {
            BanWorkaround.LOGGER.log(Level.SEVERE, Util.i18n("bannedPlayersFileError"), ex3);
        }
        final File ipFile = new File(this.ess.getDataFolder().getParentFile().getParentFile(), "banned-ips.txt");
        try {
            if (!ipFile.exists()) {
                throw new FileNotFoundException(Util.i18n("bannedIpsFileNotFound"));
            }
            final BufferedReader bufferedReader2 = new BufferedReader(new FileReader(ipFile));
            try {
                this.bannedIps.clear();
                while (bufferedReader2.ready()) {
                    final String line2 = bufferedReader2.readLine().trim().toLowerCase();
                    if (line2.length() > 0 && line2.charAt(0) == '#') {
                        continue;
                    }
                    this.bannedIps.add(line2);
                }
            }
            catch (IOException io2) {
                BanWorkaround.LOGGER.log(Level.SEVERE, Util.i18n("bannedIpsFileError"), io2);
                try {
                    bufferedReader2.close();
                }
                catch (IOException ex4) {
                    BanWorkaround.LOGGER.log(Level.SEVERE, Util.i18n("bannedIpsFileError"), ex4);
                }
            }
            finally {
                try {
                    bufferedReader2.close();
                }
                catch (IOException ex5) {
                    BanWorkaround.LOGGER.log(Level.SEVERE, Util.i18n("bannedIpsFileError"), ex5);
                }
            }
        }
        catch (FileNotFoundException ex6) {
            BanWorkaround.LOGGER.log(Level.SEVERE, Util.i18n("bannedIpsFileError"), ex6);
        }
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
