package com.earth2me.essentials;

import java.io.*;
import java.util.*;
import org.bukkit.*;
import java.util.logging.*;

public class Warps implements IConf
{
    private static final Logger logger;
    private final Map<StringIgnoreCase, EssentialsConf> warpPoints;
    private final File warpsFolder;
    private final Server server;
    
    public Warps(final Server server, final File dataFolder) {
        this.warpPoints = new HashMap<StringIgnoreCase, EssentialsConf>();
        this.server = server;
        this.warpsFolder = new File(dataFolder, "warps");
        if (!this.warpsFolder.exists()) {
            this.warpsFolder.mkdirs();
        }
        this.reloadConfig();
    }
    
    public boolean isEmpty() {
        return this.warpPoints.isEmpty();
    }
    
    public Collection<String> getWarpNames() {
        final List<String> keys = new ArrayList<String>();
        for (final StringIgnoreCase stringIgnoreCase : this.warpPoints.keySet()) {
            keys.add(stringIgnoreCase.getString());
        }
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        return keys;
    }
    
    public Location getWarp(final String warp) throws Exception {
        final EssentialsConf conf = this.warpPoints.get(new StringIgnoreCase(warp));
        if (conf == null) {
            throw new Exception(Util.i18n("warpNotExist"));
        }
        return conf.getLocation(null, this.server);
    }
    
    public void setWarp(final String name, final Location loc) throws Exception {
        final String filename = Util.sanitizeFileName(name);
        EssentialsConf conf = this.warpPoints.get(new StringIgnoreCase(name));
        if (conf == null) {
            final File confFile = new File(this.warpsFolder, filename + ".yml");
            if (confFile.exists()) {
                throw new Exception(Util.i18n("similarWarpExist"));
            }
            conf = new EssentialsConf(confFile);
            this.warpPoints.put(new StringIgnoreCase(name), conf);
        }
        conf.setProperty(null, loc);
        conf.setProperty("name", (Object)name);
        conf.save();
    }
    
    public void delWarp(final String name) throws Exception {
        final EssentialsConf conf = this.warpPoints.get(new StringIgnoreCase(name));
        if (conf == null) {
            throw new Exception(Util.i18n("warpNotExist"));
        }
        if (!conf.getFile().delete()) {
            throw new Exception(Util.i18n("warpDeleteError"));
        }
        this.warpPoints.remove(new StringIgnoreCase(name));
    }
    
    @Override
    public final void reloadConfig() {
        this.warpPoints.clear();
        final File[] listOfFiles = this.warpsFolder.listFiles();
        if (listOfFiles.length >= 1) {
            for (int i = 0; i < listOfFiles.length; ++i) {
                final String filename = listOfFiles[i].getName();
                if (listOfFiles[i].isFile() && filename.endsWith(".yml")) {
                    try {
                        final EssentialsConf conf = new EssentialsConf(listOfFiles[i]);
                        conf.load();
                        final String name = conf.getString("name");
                        if (name != null) {
                            this.warpPoints.put(new StringIgnoreCase(name), conf);
                        }
                    }
                    catch (Exception ex) {
                        Warps.logger.log(Level.WARNING, Util.format("loadWarpError", filename), ex);
                    }
                }
            }
        }
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
    }
    
    private static class StringIgnoreCase
    {
        private final String string;
        
        public StringIgnoreCase(final String string) {
            this.string = string;
        }
        
        @Override
        public int hashCode() {
            return this.getString().toLowerCase().hashCode();
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof StringIgnoreCase && this.getString().equalsIgnoreCase(((StringIgnoreCase)o).getString());
        }
        
        public String getString() {
            return this.string;
        }
    }
}
