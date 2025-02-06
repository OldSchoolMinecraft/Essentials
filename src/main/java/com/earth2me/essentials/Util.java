package com.earth2me.essentials;

import java.util.regex.*;
import org.bukkit.*;
import org.bukkit.block.*;
import java.util.logging.*;
import java.text.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Util
{
    private static final Logger logger;
    private static DecimalFormat df;
    private static final Locale defaultLocale;
    private static Locale currentLocale;
    private static ResourceBundle bundle;
    private static ResourceBundle defaultBundle;
    
    private Util() {
    }
    
    public static String sanitizeFileName(final String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]", "_");
    }
    
    public static String formatDateDiff(final long date) {
        final Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        final Calendar now = new GregorianCalendar();
        return formatDateDiff(now, c);
    }
    
    public static String formatDateDiff(final Calendar fromDate, final Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return i18n("now");
        }
        if (toDate.after(fromDate)) {
            future = true;
        }
        final StringBuilder sb = new StringBuilder();
        final int[] types = { 1, 2, 5, 11, 12, 13 };
        final String[] names = { i18n("year"), i18n("years"), i18n("month"), i18n("months"), i18n("day"), i18n("days"), i18n("hour"), i18n("hours"), i18n("minute"), i18n("minutes"), i18n("second"), i18n("seconds") };
        for (int i = 0; i < types.length; ++i) {
            final int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                sb.append(" ").append(diff).append(" ").append(names[i * 2 + ((diff > 1) ? 1 : 0)]);
            }
        }
        if (sb.length() == 0) {
            return "now";
        }
        return sb.toString();
    }
    
    private static int dateDiff(final int type, final Calendar fromDate, final Calendar toDate, final boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            ++diff;
        }
        --diff;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }
    
    public static long parseDateDiff(final String time, final boolean future) throws Exception {
        final Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
        final Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find()) {
            if (m.group() != null) {
                if (m.group().isEmpty()) {
                    continue;
                }
                for (int i = 0; i < m.groupCount(); ++i) {
                    if (m.group(i) != null && !m.group(i).isEmpty()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    continue;
                }
                if (m.group(1) != null && !m.group(1).isEmpty()) {
                    years = Integer.parseInt(m.group(1));
                }
                if (m.group(2) != null && !m.group(2).isEmpty()) {
                    months = Integer.parseInt(m.group(2));
                }
                if (m.group(3) != null && !m.group(3).isEmpty()) {
                    weeks = Integer.parseInt(m.group(3));
                }
                if (m.group(4) != null && !m.group(4).isEmpty()) {
                    days = Integer.parseInt(m.group(4));
                }
                if (m.group(5) != null && !m.group(5).isEmpty()) {
                    hours = Integer.parseInt(m.group(5));
                }
                if (m.group(6) != null && !m.group(6).isEmpty()) {
                    minutes = Integer.parseInt(m.group(6));
                }
                if (m.group(7) != null && !m.group(7).isEmpty()) {
                    seconds = Integer.parseInt(m.group(7));
                    break;
                }
                break;
            }
        }
        if (!found) {
            throw new Exception(i18n("illegalDate"));
        }
        final Calendar c = new GregorianCalendar();
        if (years > 0) {
            c.add(1, years * (future ? 1 : -1));
        }
        if (months > 0) {
            c.add(2, months * (future ? 1 : -1));
        }
        if (weeks > 0) {
            c.add(3, weeks * (future ? 1 : -1));
        }
        if (days > 0) {
            c.add(5, days * (future ? 1 : -1));
        }
        if (hours > 0) {
            c.add(11, hours * (future ? 1 : -1));
        }
        if (minutes > 0) {
            c.add(12, minutes * (future ? 1 : -1));
        }
        if (seconds > 0) {
            c.add(13, seconds * (future ? 1 : -1));
        }
        return c.getTimeInMillis();
    }
    
    public static Location getSafeDestination(final Location loc) throws Exception {
        if (loc == null || loc.getWorld() == null) {
            throw new Exception(i18n("destinationNotSet"));
        }
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        while (isBlockAboveAir(world, x, y, z) && --y >= 0) {}
        while (isBlockUnsafe(world, x, y, z)) {
            if (++y >= 127) {
                ++x;
                break;
            }
        }
        while (isBlockUnsafe(world, x, y, z)) {
            if (--y <= 1) {
                y = 127;
                if (++x - 32 > loc.getBlockX()) {
                    throw new Exception(i18n("holeInFloor"));
                }
                continue;
            }
        }
        return new Location(world, x + 0.5, (double)y, z + 0.5, loc.getYaw(), loc.getPitch());
    }
    
    private static boolean isBlockAboveAir(final World world, final int x, final int y, final int z) {
        return world.getBlockAt(x, y - 1, z).getType() == Material.AIR;
    }
    
    public static boolean isBlockUnsafe(final World world, final int x, final int y, final int z) {
        final Block below = world.getBlockAt(x, y - 1, z);
        return below.getType() == Material.LAVA || below.getType() == Material.STATIONARY_LAVA || below.getType() == Material.FIRE || (world.getBlockAt(x, y, z).getType() != Material.AIR || world.getBlockAt(x, y + 1, z).getType() != Material.AIR) || isBlockAboveAir(world, x, y, z);
    }
    
    public static String formatCurrency(final double value, final IEssentials ess) {
        String str = ess.getSettings().getCurrencySymbol() + Util.df.format(value);
        if (str.endsWith(".00")) {
            str = str.substring(0, str.length() - 3);
        }
        return str;
    }
    
    public static double roundDouble(final double d) {
        return Math.round(d * 100.0) / 100.0;
    }
    
    public static Locale getCurrentLocale() {
        return Util.currentLocale;
    }
    
    public static String i18n(final String string) {
        try {
            return Util.bundle.getString(string);
        }
        catch (MissingResourceException ex) {
            Util.logger.log(Level.WARNING, String.format("Missing translation key \"%s\" in translation file %s", ex.getKey(), Util.bundle.getLocale().toString()), ex);
            return Util.defaultBundle.getString(string);
        }
    }
    
    public static String format(final String string, final Object... objects) {
        final MessageFormat mf = new MessageFormat(i18n(string));
        return mf.format(objects);
    }
    
    public static void updateLocale(final String loc, final IEssentials ess) {
        if (loc == null || loc.isEmpty()) {
            return;
        }
        final String[] parts = loc.split("[_\\.]");
        if (parts.length == 1) {
            Util.currentLocale = new Locale(parts[0]);
        }
        if (parts.length == 2) {
            Util.currentLocale = new Locale(parts[0], parts[1]);
        }
        if (parts.length == 3) {
            Util.currentLocale = new Locale(parts[0], parts[1], parts[2]);
        }
        Util.logger.log(Level.INFO, String.format("Using locale %s", Util.currentLocale.toString()));
        Util.bundle = ResourceBundle.getBundle("messages", Util.currentLocale, new ConfigClassLoader(Util.class.getClassLoader(), ess));
        if (!Util.bundle.keySet().containsAll(Util.defaultBundle.keySet())) {
            Util.logger.log(Level.WARNING, String.format("Translation file %s does not contain all translation keys.", Util.currentLocale.toString()));
        }
    }
    
    public static String joinList(final Object... list) {
        return joinList(", ", list);
    }
    
    public static String joinList(final String seperator, final Object... list) {
        final StringBuilder buf = new StringBuilder();
        for (final Object each : list) {
            if (buf.length() > 0) {
                buf.append(seperator);
            }
            if (each instanceof List) {
                buf.append(joinList(seperator, ((List)each).toArray()));
            }
            else {
                try {
                    buf.append(each.toString());
                }
                catch (Exception e) {
                    buf.append(each.toString());
                }
            }
        }
        return buf.toString();
    }
    
    public static String capitalCase(final String s) {
        return s.toUpperCase().charAt(0) + s.toLowerCase().substring(1);
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
        Util.df = new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale.US));
        defaultLocale = Locale.getDefault();
        Util.currentLocale = Util.defaultLocale;
        Util.bundle = ResourceBundle.getBundle("messages", Util.defaultLocale);
        Util.defaultBundle = ResourceBundle.getBundle("messages", Locale.US);
    }
    
    private static class ConfigClassLoader extends ClassLoader
    {
        private final transient File dataFolder;
        private final transient ClassLoader cl;
        private final transient IEssentials ess;
        
        public ConfigClassLoader(final ClassLoader cl, final IEssentials ess) {
            this.ess = ess;
            this.dataFolder = ess.getDataFolder();
            this.cl = cl;
        }
        
        @Override
        public URL getResource(final String string) {
            final File file = new File(this.dataFolder, string);
            if (file.exists()) {
                try {
                    return file.toURI().toURL();
                }
                catch (MalformedURLException ex) {
                    return this.cl.getResource(string);
                }
            }
            return this.cl.getResource(string);
        }
        
        @Override
        public synchronized void clearAssertionStatus() {
            this.cl.clearAssertionStatus();
        }
        
        @Override
        public InputStream getResourceAsStream(final String string) {
            final File file = new File(this.dataFolder, string);
            if (file.exists()) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                    final String version = br.readLine();
                    if (version == null || !version.equals("#version: " + this.ess.getDescription().getVersion())) {
                        Util.logger.log(Level.WARNING, String.format("Translation file %s is not updated for Essentials version. Will use default.", file));
                        return this.cl.getResourceAsStream(string);
                    }
                    return new FileInputStream(file);
                }
                catch (IOException ex) {
                    return this.cl.getResourceAsStream(string);
                }
                finally {
                    if (br != null) {
                        try {
                            br.close();
                        }
                        catch (IOException ex2) {}
                    }
                }
            }
            return this.cl.getResourceAsStream(string);
        }
        
        @Override
        public Enumeration<URL> getResources(final String string) throws IOException {
            return this.cl.getResources(string);
        }
        
        @Override
        public Class<?> loadClass(final String string) throws ClassNotFoundException {
            return this.cl.loadClass(string);
        }
        
        @Override
        public synchronized void setClassAssertionStatus(final String string, final boolean bln) {
            this.cl.setClassAssertionStatus(string, bln);
        }
        
        @Override
        public synchronized void setDefaultAssertionStatus(final boolean bln) {
            this.cl.setDefaultAssertionStatus(bln);
        }
        
        @Override
        public synchronized void setPackageAssertionStatus(final String string, final boolean bln) {
            this.cl.setPackageAssertionStatus(string, bln);
        }
    }
}
