package com.earth2me.essentials.signs;

import com.earth2me.essentials.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.block.*;
import org.bukkit.block.*;

public class SignPlayerListener extends PlayerListener
{
    private final transient IEssentials ess;
    
    public SignPlayerListener(final IEssentials ess) {
        this.ess = ess;
    }
    
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.isCancelled() || this.ess.getSettings().areSignsDisabled()) {
            return;
        }
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        final int mat = block.getTypeId();
        if (mat == Material.SIGN_POST.getId() || mat == Material.WALL_SIGN.getId()) {
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }
            final Sign csign = (Sign)block.getState();
            for (final Signs signs : Signs.values()) {
                final EssentialsSign sign = signs.getSign();
                if (csign.getLine(0).equalsIgnoreCase(sign.getSuccessName())) {
                    sign.onSignInteract(block, event.getPlayer(), this.ess);
                    event.setCancelled(true);
                    return;
                }
            }
        }
        else {
            for (final Signs signs2 : Signs.values()) {
                final EssentialsSign sign2 = signs2.getSign();
                if (sign2.getBlocks().contains(block.getType()) && !sign2.onBlockInteract(block, event.getPlayer(), this.ess)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
