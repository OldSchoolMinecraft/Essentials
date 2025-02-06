package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import com.earth2me.essentials.*;

public class Commandmute extends EssentialsCommand
{
    public Commandmute() {
        super("mute");
    }
    
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        final User p = this.getPlayer(server, args, 0, true);
        if (!p.isMuted() && p.isAuthorized("essentials.mute.exempt")) {
            throw new Exception(Util.i18n("muteExempt"));
        }
        long muteTimestamp = 0L;
        if (args.length > 1) {
            final String time = EssentialsCommand.getFinalArg(args, 1);
            muteTimestamp = Util.parseDateDiff(time, true);
        }
        p.setMuteTimeout(muteTimestamp);
        final boolean muted = p.toggleMuted();
        sender.sendMessage(muted ? ((muteTimestamp > 0L) ? Util.format("mutedPlayerFor", p.getDisplayName(), Util.formatDateDiff(muteTimestamp)) : Util.format("mutedPlayer", p.getDisplayName())) : Util.format("unmutedPlayer", p.getDisplayName()));
        p.sendMessage(muted ? ((muteTimestamp > 0L) ? Util.format("playerMutedFor", Util.formatDateDiff(muteTimestamp)) : Util.i18n("playerMuted")) : Util.i18n("playerUnmuted"));
    }
}
