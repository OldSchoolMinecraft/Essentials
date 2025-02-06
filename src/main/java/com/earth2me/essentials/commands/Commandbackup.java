package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandbackup extends EssentialsCommand
{
    public Commandbackup() {
        super("backup");
    }
    
    @Override
    protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        final Backup backup = this.ess.getBackup();
        if (backup == null) {
            throw new Exception();
        }
        backup.run();
        sender.sendMessage(Util.i18n("backupStarted"));
    }
}
