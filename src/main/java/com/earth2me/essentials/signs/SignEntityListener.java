package com.earth2me.essentials.signs;

import com.earth2me.essentials.*;
import org.bukkit.event.entity.*;
import org.bukkit.block.*;
import org.bukkit.*;
import java.util.*;

public class SignEntityListener extends EntityListener
{
    private final transient IEssentials ess;
    
    public SignEntityListener(final IEssentials ess) {
        this.ess = ess;
    }
    
    public void onEntityExplode(final EntityExplodeEvent event) {
        for (final Block block : event.blockList()) {
            if (((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block))) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
                event.setCancelled(true);
                return;
            }
            for (final Signs signs : Signs.values()) {
                final EssentialsSign sign = signs.getSign();
                if (sign.getBlocks().contains(block.getType())) {
                    event.setCancelled(!sign.onBlockExplode(block, this.ess));
                    return;
                }
            }
        }
    }
}
