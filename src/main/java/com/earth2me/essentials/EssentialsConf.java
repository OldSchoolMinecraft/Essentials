package com.earth2me.essentials;

import org.bukkit.util.config.*;
import java.util.logging.*;
import java.io.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import java.util.*;

public class EssentialsConf extends Configuration
{
    private static final Logger LOGGER;
    private transient File configFile;
    private transient String templateName;
    private transient Class<?> resourceClass;
    
    public EssentialsConf(final File configFile) {
        super(configFile);
        this.templateName = null;
        this.resourceClass = EssentialsConf.class;
        this.configFile = configFile;
        if (this.root == null) {
            this.root = new HashMap();
        }
    }
    
    public void load() {
        this.configFile = this.configFile.getAbsoluteFile();
        if (!this.configFile.getParentFile().exists() && !this.configFile.getParentFile().mkdirs()) {
            EssentialsConf.LOGGER.log(Level.SEVERE, Util.format("failedToCreateConfig", this.configFile.toString()));
        }
        if (this.configFile.exists() && this.configFile.length() != 0L) {
            try {
                final InputStream input = new FileInputStream(this.configFile);
                try {
                    if (input.read() == 0) {
                        input.close();
                        this.configFile.delete();
                    }
                }
                catch (IOException ex) {
                    EssentialsConf.LOGGER.log(Level.SEVERE, null, ex);
                    try {
                        input.close();
                    }
                    catch (IOException ex2) {
                        EssentialsConf.LOGGER.log(Level.SEVERE, null, ex2);
                    }
                }
                finally {
                    try {
                        input.close();
                    }
                    catch (IOException ex2) {
                        EssentialsConf.LOGGER.log(Level.SEVERE, null, ex2);
                    }
                }
            }
            catch (FileNotFoundException ex3) {
                EssentialsConf.LOGGER.log(Level.SEVERE, null, ex3);
            }
        }
        if (!this.configFile.exists()) {
            if (this.templateName != null) {
                EssentialsConf.LOGGER.log(Level.INFO, Util.format("creatingConfigFromTemplate", this.configFile.toString()));
                this.createFromTemplate();
            }
            else {
                try {
                    EssentialsConf.LOGGER.log(Level.INFO, Util.format("creatingEmptyConfig", this.configFile.toString()));
                    if (!this.configFile.createNewFile()) {
                        EssentialsConf.LOGGER.log(Level.SEVERE, Util.format("failedToCreateConfig", this.configFile.toString()));
                    }
                }
                catch (IOException ex4) {
                    EssentialsConf.LOGGER.log(Level.SEVERE, Util.format("failedToCreateConfig", this.configFile.toString()), ex4);
                }
            }
        }
        try {
            super.load();
        }
        catch (RuntimeException e) {
            EssentialsConf.LOGGER.log(Level.INFO, "File: " + this.configFile.toString());
            throw e;
        }
        if (this.root == null) {
            this.root = new HashMap();
        }
    }
    
    private void createFromTemplate() {
        InputStream istr = null;
        OutputStream ostr = null;
        try {
            istr = this.resourceClass.getResourceAsStream(this.templateName);
            if (istr == null) {
                EssentialsConf.LOGGER.log(Level.SEVERE, Util.format("couldNotFindTemplate", this.templateName));
                return;
            }
            ostr = new FileOutputStream(this.configFile);
            byte[] buffer;
            int length;
            for (buffer = new byte[1024], length = 0, length = istr.read(buffer); length > 0; length = istr.read(buffer)) {
                ostr.write(buffer, 0, length);
            }
        }
        catch (IOException ex) {
            EssentialsConf.LOGGER.log(Level.SEVERE, Util.format("failedToWriteConfig", this.configFile.toString()), ex);
        }
        finally {
            try {
                if (istr != null) {
                    istr.close();
                }
            }
            catch (IOException ex2) {
                Logger.getLogger(EssentialsConf.class.getName()).log(Level.SEVERE, null, ex2);
            }
            try {
                if (ostr != null) {
                    ostr.close();
                }
            }
            catch (IOException ex2) {
                EssentialsConf.LOGGER.log(Level.SEVERE, Util.format("failedToCloseConfig", this.configFile.toString()), ex2);
            }
        }
    }
    
    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }
    
    public File getFile() {
        return this.configFile;
    }
    
    public void setTemplateName(final String templateName, final Class<?> resClass) {
        this.templateName = templateName;
        this.resourceClass = resClass;
    }
    
    public boolean hasProperty(final String path) {
        return this.getProperty(path) != null;
    }
    
    public Location getLocation(final String path, final Server server) throws Exception {
        final String worldName = this.getString(((path == null) ? "" : (path + ".")) + "world");
        if (worldName == null || worldName.isEmpty()) {
            return null;
        }
        final World world = server.getWorld(worldName);
        if (world == null) {
            throw new Exception(Util.i18n("invalidWorld"));
        }
        return new Location(world, this.getDouble(((path == null) ? "" : (path + ".")) + "x", 0.0), this.getDouble(((path == null) ? "" : (path + ".")) + "y", 0.0), this.getDouble(((path == null) ? "" : (path + ".")) + "z", 0.0), (float)this.getDouble(((path == null) ? "" : (path + ".")) + "yaw", 0.0), (float)this.getDouble(((path == null) ? "" : (path + ".")) + "pitch", 0.0));
    }
    
    public void setProperty(final String path, final Location loc) {
        this.setProperty(((path == null) ? "" : (path + ".")) + "world", (Object)loc.getWorld().getName());
        this.setProperty(((path == null) ? "" : (path + ".")) + "x", (Object)loc.getX());
        this.setProperty(((path == null) ? "" : (path + ".")) + "y", (Object)loc.getY());
        this.setProperty(((path == null) ? "" : (path + ".")) + "z", (Object)loc.getZ());
        this.setProperty(((path == null) ? "" : (path + ".")) + "yaw", (Object)loc.getYaw());
        this.setProperty(((path == null) ? "" : (path + ".")) + "pitch", (Object)loc.getPitch());
    }
    
    public ItemStack getItemStack(final String path) {
        return new ItemStack(Material.valueOf(this.getString(path + ".type", "AIR")), this.getInt(path + ".amount", 1), (short)this.getInt(path + ".damage", 0));
    }
    
    public void setProperty(final String path, final ItemStack stack) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", stack.getType().toString());
        map.put("amount", stack.getAmount());
        map.put("damage", stack.getDurability());
        this.setProperty(path, (Object)map);
    }
    
    public long getLong(final String path, final long def) {
        try {
            final Number num = (Number)this.getProperty(path);
            return (num == null) ? def : num.longValue();
        }
        catch (ClassCastException ex) {
            return def;
        }
    }
    
    public double getDouble(final String path, final double def) {
        try {
            final Number num = (Number)this.getProperty(path);
            return (num == null) ? def : num.doubleValue();
        }
        catch (ClassCastException ex) {
            return def;
        }
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
