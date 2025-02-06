package com.earth2me.essentials.signs;

import com.earth2me.essentials.*;
import java.util.*;

public class SignMail extends EssentialsSign
{
    public SignMail() {
        super("Mail");
    }
    
    @Override
    protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException {
        final List<String> mail = player.getMails();
        if (mail.isEmpty()) {
            player.sendMessage(Util.i18n("noNewMail"));
            return false;
        }
        for (final String s : mail) {
            player.sendMessage(s);
        }
        player.sendMessage(Util.i18n("markMailAsRead"));
        return true;
    }
}
