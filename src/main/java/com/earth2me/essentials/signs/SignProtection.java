package com.earth2me.essentials.signs;

import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import com.earth2me.essentials.*;

public class SignProtection extends EssentialsSign
{
    private final transient Set<Material> protectedBlocks;
    
    public SignProtection() {
        super("Protection");
        (this.protectedBlocks = EnumSet.noneOf(Material.class)).add(Material.CHEST);
        this.protectedBlocks.add(Material.BURNING_FURNACE);
        this.protectedBlocks.add(Material.FURNACE);
        this.protectedBlocks.add(Material.DISPENSER);
    }
    
    @Override
    protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        sign.setLine(3, "§4" + username);
        if (this.hasAdjacentBlock(sign.getBlock(), new Block[0]) && this.isBlockProtected(sign.getBlock(), player, username, true) != SignProtectionState.NOT_ALLOWED) {
            sign.setLine(3, "§1" + username);
            return true;
        }
        player.sendMessage("§4You are not allowed to create sign here.");
        return false;
    }
    
    @Override
    protected boolean onSignBreak(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException {
        final SignProtectionState state = this.checkProtectionSign(sign, player, username);
        return state == SignProtectionState.OWNER;
    }
    
    public boolean hasAdjacentBlock(final Block block, final Block... ignoredBlocks) {
        final Block[] arr$;
        final Block[] faces = arr$ = this.getAdjacentBlocks(block);
        for (final Block b : arr$) {
            for (final Block ignoredBlock : ignoredBlocks) {
                if (b.getLocation().equals((Object)ignoredBlock.getLocation())) {}
            }
            if (this.protectedBlocks.contains(b.getType())) {
                return true;
            }
        }
        return false;
    }
    
    private void checkIfSignsAreBroken(final Block block, final User player, final String username, final IEssentials ess) {
        final Map<Location, SignProtectionState> signs = this.getConnectedSigns(block, player, username, false);
        for (final Map.Entry<Location, SignProtectionState> entry : signs.entrySet()) {
            if (entry.getValue() != SignProtectionState.NOSIGN) {
                final Block sign = entry.getKey().getBlock();
                if (this.hasAdjacentBlock(sign, block)) {
                    continue;
                }
                block.setType(Material.AIR);
                final Trade trade = new Trade(new ItemStack(Material.SIGN, 1), ess);
                trade.pay(player);
            }
        }
    }
    
    private Map<Location, SignProtectionState> getConnectedSigns(final Block block, final User user, final String username, final boolean secure) {
        final Map<Location, SignProtectionState> signs = new HashMap<Location, SignProtectionState>();
        this.getConnectedSigns(block, signs, user, username, secure ? 4 : 2);
        return signs;
    }
    
    private void getConnectedSigns(final Block block, final Map<Location, SignProtectionState> signs, final User user, final String username, final int depth) {
        final Block[] arr$;
        final Block[] faces = arr$ = this.getAdjacentBlocks(block);
        for (final Block b : arr$) {
            final Location loc = b.getLocation();
            if (!signs.containsKey(loc)) {
                final SignProtectionState check = this.checkProtectionSign(b, user, username);
                signs.put(loc, check);
                if (this.protectedBlocks.contains(b.getType()) && depth > 0) {
                    this.getConnectedSigns(b, signs, user, username, depth - 1);
                }
            }
        }
    }
    
    private SignProtectionState checkProtectionSign(final Block block, final User user, final String username) {
        if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
            final BlockSign sign = new BlockSign(block);
            if (sign.getLine(0).equalsIgnoreCase(this.getSuccessName())) {
                return this.checkProtectionSign(sign, user, username);
            }
        }
        return SignProtectionState.NOSIGN;
    }
    
    private SignProtectionState checkProtectionSign(final ISign sign, final User user, final String username) {
        if (user == null || username == null) {
            return SignProtectionState.NOT_ALLOWED;
        }
        if (user.isAuthorized("essentials.signs.protection.override")) {
            return SignProtectionState.OWNER;
        }
        if (ChatColor.stripColor(sign.getLine(3)).equalsIgnoreCase(username)) {
            return SignProtectionState.OWNER;
        }
        for (int i = 1; i <= 2; ++i) {
            final String line = sign.getLine(i);
            if (line.startsWith("(") && line.endsWith(")") && user.inGroup(line.substring(1, line.length() - 1))) {
                return SignProtectionState.ALLOWED;
            }
            if (line.equalsIgnoreCase(username)) {
                return SignProtectionState.ALLOWED;
            }
        }
        return SignProtectionState.NOT_ALLOWED;
    }
    
    private Block[] getAdjacentBlocks(final Block block) {
        return new Block[] { block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.EAST), block.getRelative(BlockFace.WEST), block.getRelative(BlockFace.DOWN), block.getRelative(BlockFace.UP) };
    }
    
    public SignProtectionState isBlockProtected(final Block block, final User user, final String username, final boolean secure) {
        final Map<Location, SignProtectionState> signs = this.getConnectedSigns(block, user, username, secure);
        SignProtectionState retstate = SignProtectionState.NOSIGN;
        for (final SignProtectionState state : signs.values()) {
            if (state == SignProtectionState.OWNER || state == SignProtectionState.ALLOWED) {
                return state;
            }
            if (state != SignProtectionState.NOT_ALLOWED) {
                continue;
            }
            retstate = state;
        }
        return retstate;
    }
    
    public boolean isBlockProtected(final Block block) {
        final Block[] arr$;
        final Block[] faces = arr$ = this.getAdjacentBlocks(block);
        for (final Block b : arr$) {
            if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
                final Sign sign = (Sign)b.getState();
                if (sign.getLine(0).equalsIgnoreCase("§1[Protection]")) {
                    return true;
                }
            }
            if (this.protectedBlocks.contains(b.getType())) {
                final Block[] arr$2;
                final Block[] faceChest = arr$2 = this.getAdjacentBlocks(b);
                for (final Block a : arr$2) {
                    if (a.getType() == Material.SIGN_POST || a.getType() == Material.WALL_SIGN) {
                        final Sign sign2 = (Sign)a.getState();
                        if (sign2.getLine(0).equalsIgnoreCase("§1[Protection]")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public Set<Material> getBlocks() {
        return this.protectedBlocks;
    }
    
    @Override
    protected boolean onBlockPlace(final Block block, final User player, final String username, final IEssentials ess) throws SignException {
        for (final Block adjBlock : this.getAdjacentBlocks(block)) {
            final SignProtectionState state = this.isBlockProtected(adjBlock, player, username, true);
            if ((state == SignProtectionState.ALLOWED || state == SignProtectionState.NOT_ALLOWED) && !player.isAuthorized("essentials.signs.protection.override")) {
                player.sendMessage(Util.format("noPlacePermission", block.getType().toString().toLowerCase()));
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected boolean onBlockInteract(final Block block, final User player, final String username, final IEssentials ess) throws SignException {
        final SignProtectionState state = this.isBlockProtected(block, player, username, false);
        if (state == SignProtectionState.OWNER || state == SignProtectionState.NOSIGN || state == SignProtectionState.ALLOWED) {
            return true;
        }
        if (state == SignProtectionState.NOT_ALLOWED && player.isAuthorized("essentials.signs.protection.override")) {
            return true;
        }
        player.sendMessage(Util.format("noAccessPermission", block.getType().toString().toLowerCase()));
        return false;
    }
    
    @Override
    protected boolean onBlockBreak(final Block block, final User player, final String username, final IEssentials ess) throws SignException {
        final SignProtectionState state = this.isBlockProtected(block, player, username, false);
        if (state == SignProtectionState.OWNER || state == SignProtectionState.NOSIGN) {
            this.checkIfSignsAreBroken(block, player, username, ess);
            return true;
        }
        if ((state == SignProtectionState.ALLOWED || state == SignProtectionState.NOT_ALLOWED) && player.isAuthorized("essentials.signs.protection.override")) {
            this.checkIfSignsAreBroken(block, player, username, ess);
            return true;
        }
        player.sendMessage(Util.format("noDestroyPermission", block.getType().toString().toLowerCase()));
        return false;
    }
    
    @Override
    public boolean onBlockExplode(final Block block, final IEssentials ess) {
        final SignProtectionState state = this.isBlockProtected(block, null, null, false);
        return state == SignProtectionState.NOSIGN;
    }
    
    @Override
    public boolean onBlockBurn(final Block block, final IEssentials ess) {
        final SignProtectionState state = this.isBlockProtected(block, null, null, false);
        return state == SignProtectionState.NOSIGN;
    }
    
    @Override
    public boolean onBlockIgnite(final Block block, final IEssentials ess) {
        final SignProtectionState state = this.isBlockProtected(block, null, null, false);
        return state == SignProtectionState.NOSIGN;
    }
    
    @Override
    public boolean onBlockPush(final Block block, final IEssentials ess) {
        final SignProtectionState state = this.isBlockProtected(block, null, null, false);
        return state == SignProtectionState.NOSIGN;
    }
    
    public enum SignProtectionState
    {
        NOT_ALLOWED, 
        ALLOWED, 
        NOSIGN, 
        OWNER;
    }
}
