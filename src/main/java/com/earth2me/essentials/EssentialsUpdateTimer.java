package com.earth2me.essentials;

import java.util.logging.*;
import java.io.*;
import java.util.regex.*;
import java.net.*;
import org.bukkit.entity.*;

class EssentialsUpdateTimer implements Runnable
{
    private transient URL url;
    private final transient IEssentials ess;
    private static final Logger LOGGER;
    private final transient Pattern pattern;
    
    public EssentialsUpdateTimer(final IEssentials ess) {
        this.pattern = Pattern.compile("git-Bukkit-([0-9]+).([0-9]+).([0-9]+)-[0-9]+-[0-9a-z]+-b([0-9]+)jnks.*");
        this.ess = ess;
        try {
            this.url = new URL("http://essentialsupdate.appspot.com/check");
        }
        catch (MalformedURLException ex) {
            EssentialsUpdateTimer.LOGGER.log(Level.SEVERE, "Invalid url!", ex);
        }
    }
    
    @Override
    public void run() {
        try {
            final StringBuilder builder = new StringBuilder();
            String bukkitVersion = this.ess.getServer().getVersion();
            final Matcher versionMatch = this.pattern.matcher(bukkitVersion);
            if (versionMatch.matches()) {
                bukkitVersion = versionMatch.group(4);
            }
            builder.append("v=").append(URLEncoder.encode(this.ess.getDescription().getVersion(), "UTF-8"));
            builder.append("&b=").append(URLEncoder.encode(bukkitVersion, "UTF-8"));
            final URLConnection conn = this.url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            conn.connect();
            final OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(builder.toString());
            writer.flush();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String ret = reader.readLine();
            writer.close();
            reader.close();
            if (!ret.isEmpty() && !ret.equalsIgnoreCase("OK")) {
                EssentialsUpdateTimer.LOGGER.log(Level.INFO, "Essentials Update-Check: " + ret);
                if (ret.startsWith("New Version")) {
                    for (final Player player : this.ess.getServer().getOnlinePlayers()) {
                        final User user = this.ess.getUser(player);
                        if (user.isAuthorized("essentials.admin.notices.update")) {
                            user.sendMessage(ret);
                        }
                    }
                }
            }
        }
        catch (IOException ex) {
            EssentialsUpdateTimer.LOGGER.log(Level.SEVERE, "Failed to open connection", ex);
        }
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
