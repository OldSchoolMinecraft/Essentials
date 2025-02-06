package com.earth2me.essentials;

import java.util.logging.*;
import java.io.*;
import org.bukkit.inventory.*;

public class Worth implements IConf
{
    private static final Logger logger;
    private final EssentialsConf config;
    
    public Worth(final File dataFolder) {
        (this.config = new EssentialsConf(new File(dataFolder, "worth.yml"))).setTemplateName("/worth.yml");
        this.config.load();
    }
    
    public double getPrice(final ItemStack itemStack) {
        final String itemname = itemStack.getType().toString().toLowerCase().replace("_", "");
        double result = this.config.getDouble("worth." + itemname + "." + itemStack.getDurability(), Double.NaN);
        if (Double.isNaN(result)) {
            result = this.config.getDouble("worth." + itemname + ".0", Double.NaN);
        }
        if (Double.isNaN(result)) {
            result = this.config.getDouble("worth." + itemname, Double.NaN);
        }
        if (Double.isNaN(result)) {
            result = this.config.getDouble("worth-" + itemStack.getTypeId(), Double.NaN);
        }
        return result;
    }
    
    public void setPrice(final ItemStack itemStack, final double price) {
        if (itemStack.getType().getData() == null) {
            this.config.setProperty("worth." + itemStack.getType().toString().toLowerCase().replace("_", ""), (Object)price);
        }
        else {
            this.config.setProperty("worth." + itemStack.getType().toString().toLowerCase().replace("_", "") + "." + itemStack.getDurability(), (Object)price);
        }
        this.config.removeProperty("worth-" + itemStack.getTypeId());
        this.config.save();
    }
    
    @Override
    public void reloadConfig() {
        this.config.load();
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
    }
}
