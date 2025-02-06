package com.earth2me.essentials;

import org.bukkit.craftbukkit.*;
import java.util.logging.*;
import org.bukkit.command.*;
import java.io.*;

public class Backup implements Runnable
{
    private static final Logger LOGGER;
    private final transient CraftServer server;
    private final transient IEssentials ess;
    private transient boolean running;
    private transient int taskId;
    private transient boolean active;
    
    public Backup(final IEssentials ess) {
        this.running = false;
        this.taskId = -1;
        this.active = false;
        this.ess = ess;
        this.server = (CraftServer)ess.getServer();
        if (this.server.getOnlinePlayers().length > 0) {
            this.startTask();
        }
    }
    
    void onPlayerJoin() {
        this.startTask();
    }
    
    private void startTask() {
        if (!this.running) {
            final long interval = this.ess.getSettings().getBackupInterval() * 1200L;
            if (interval < 1200L) {
                return;
            }
            this.taskId = this.ess.scheduleSyncRepeatingTask(this, interval, interval);
            this.running = true;
        }
    }
    
    @Override
    public void run() {
        if (this.active) {
            return;
        }
        this.active = true;
        final String command = this.ess.getSettings().getBackupCommand();
        if (command == null || "".equals(command)) {
            return;
        }
        Backup.LOGGER.log(Level.INFO, Util.i18n("backupStarted"));
        final CommandSender cs = (CommandSender)this.server.getServer().console;
        this.server.dispatchCommand(cs, "save-all");
        this.server.dispatchCommand(cs, "save-off");
        this.ess.scheduleAsyncDelayedTask(new Runnable() {
            @Override
            public void run() {
                try {
                    final ProcessBuilder childBuilder = new ProcessBuilder(new String[] { command });
                    childBuilder.redirectErrorStream(true);
                    childBuilder.directory(Backup.this.ess.getDataFolder().getParentFile().getParentFile());
                    final Process child = childBuilder.start();
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(child.getInputStream()));
                    try {
                        child.waitFor();
                        String line;
                        do {
                            line = reader.readLine();
                            if (line != null) {
                                Backup.LOGGER.log(Level.INFO, line);
                            }
                        } while (line != null);
                    }
                    finally {
                        reader.close();
                    }
                }
                catch (InterruptedException ex) {
                    Backup.LOGGER.log(Level.SEVERE, null, ex);
                }
                catch (IOException ex2) {
                    Backup.LOGGER.log(Level.SEVERE, null, ex2);
                }
                finally {
                    Backup.this.ess.scheduleSyncDelayedTask(new Runnable() {
                        @Override
                        public void run() {
                            Backup.this.server.dispatchCommand(cs, "save-on");
                            if (Backup.this.server.getOnlinePlayers().length == 0) {
                                Backup.this.running = false;
                                if (Backup.this.taskId != -1) {
                                    Backup.this.server.getScheduler().cancelTask(Backup.this.taskId);
                                }
                            }
                            Backup.this.active = false;
                            Backup.LOGGER.log(Level.INFO, Util.i18n("backupFinished"));
                        }
                    });
                }
            }
        });
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
