package com.earth2me.essentials.signs;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.block.*;
import java.util.logging.*;
import com.earth2me.essentials.*;
import java.util.*;
import org.bukkit.event.block.*;

public class SignBlockListener extends BlockListener
{
    private final transient IEssentials ess;
    private static final Logger LOGGER;
    
    public SignBlockListener(final IEssentials ess) {
        this.ess = ess;
    }
    
    public void onBlockBreak(final BlockBreakEvent event) {
        if (event.isCancelled() || this.ess.getSettings().areSignsDisabled()) {
            return;
        }
        if (this.protectSignsAndBlocks(event.getBlock(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }
    
    public boolean protectSignsAndBlocks(final Block block, final Player player) {
        final int mat = block.getTypeId();
        if (mat == Material.SIGN_POST.getId() || mat == Material.WALL_SIGN.getId()) {
            final Sign csign = (Sign)block.getState();
            for (final Signs signs : Signs.values()) {
                final EssentialsSign sign = signs.getSign();
                if (csign.getLine(0).equalsIgnoreCase(sign.getSuccessName()) && !sign.onSignBreak(block, player, this.ess)) {
                    return true;
                }
            }
        }
        else {
            if (EssentialsSign.checkIfBlockBreaksSigns(block)) {
                SignBlockListener.LOGGER.log(Level.INFO, "Prevented that a block was broken next to a sign.");
                return true;
            }
            for (final Signs signs2 : Signs.values()) {
                final EssentialsSign sign2 = signs2.getSign();
                if (sign2.getBlocks().contains(block.getType()) && !sign2.onBlockBreak(block, player, this.ess)) {
                    SignBlockListener.LOGGER.log(Level.INFO, "A block was protected by a sign.");
                    return true;
                }
            }
        }
        return false;
    }
    
    public void onSignChange(final SignChangeEvent event) {
        if (event.isCancelled() || this.ess.getSettings().areSignsDisabled()) {
            return;
        }
        final User user = this.ess.getUser(event.getPlayer());
        if (user.isAuthorized("essentials.signs.color")) {
            for (int i = 0; i < 4; ++i) {
                event.setLine(i, event.getLine(i).replaceAll("&([0-9a-f])", "§$1"));
            }
        }
        for (final Signs signs : Signs.values()) {
            final EssentialsSign sign = signs.getSign();
            if (event.getLine(0).equalsIgnoreCase(sign.getSuccessName())) {
                event.setCancelled(true);
                return;
            }
            if (event.getLine(0).equalsIgnoreCase(sign.getTemplateName()) && !sign.onSignCreate(event, this.ess)) {
                event.setCancelled(true);
                return;
            }
        }
    }
    
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled() || this.ess.getSettings().areSignsDisabled()) {
            return;
        }
        final Block against = event.getBlockAgainst();
        if ((against.getType() == Material.WALL_SIGN || against.getType() == Material.SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(against))) {
            event.setCancelled(true);
            return;
        }
        final Block block = event.getBlock();
        if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
            return;
        }
        for (final Signs signs : Signs.values()) {
            final EssentialsSign sign = signs.getSign();
            if (sign.getBlocks().contains(block.getType()) && !sign.onBlockPlace(block, event.getPlayer(), this.ess)) {
                event.setCancelled(true);
                return;
            }
        }
    }
    
    public void onBlockBurn(final BlockBurnEvent event) {
        if (event.isCancelled() || this.ess.getSettings().areSignsDisabled()) {
            return;
        }
        final Block block = event.getBlock();
        if (((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block))) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
            event.setCancelled(true);
            return;
        }
        for (final Signs signs : Signs.values()) {
            final EssentialsSign sign = signs.getSign();
            if (sign.getBlocks().contains(block.getType()) && !sign.onBlockBurn(block, this.ess)) {
                event.setCancelled(true);
                return;
            }
        }
    }
    
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if (event.isCancelled() || this.ess.getSettings().areSignsDisabled()) {
            return;
        }
        final Block block = event.getBlock();
        if (((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block))) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
            event.setCancelled(true);
            return;
        }
        for (final Signs signs : Signs.values()) {
            final EssentialsSign sign = signs.getSign();
            if (sign.getBlocks().contains(block.getType()) && !sign.onBlockIgnite(block, this.ess)) {
                event.setCancelled(true);
                return;
            }
        }
    }
    
    public void onBlockPistonExtend(final BlockPistonExtendEvent event) {
        for (final Block block : event.getBlocks()) {
            if (((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block))) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
                event.setCancelled(true);
                return;
            }
            for (final Signs signs : Signs.values()) {
                final EssentialsSign sign = signs.getSign();
                if (sign.getBlocks().contains(block.getType()) && !sign.onBlockPush(block, this.ess)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
    
    public void onBlockPistonRetract(final BlockPistonRetractEvent event) {
        if (event.isSticky()) {
            final Block block = event.getBlock();
            if (((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block))) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
                event.setCancelled(true);
                return;
            }
            for (final Signs signs : Signs.values()) {
                final EssentialsSign sign = signs.getSign();
                if (sign.getBlocks().contains(block.getType()) && !sign.onBlockPush(block, this.ess)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
    
    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
