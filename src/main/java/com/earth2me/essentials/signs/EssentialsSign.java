package com.earth2me.essentials.signs;

import org.bukkit.*;
import org.bukkit.event.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.material.*;
import com.earth2me.essentials.*;
import org.bukkit.inventory.*;
import org.bukkit.material.Sign;

import java.util.*;

public class EssentialsSign
{
    private static final Set<Material> EMPTY_SET;
    protected final transient String signName;
    private static final String FORMAT_SUCCESS = "�1[%s]";
    private static final String FORMAT_TEMPLATE = "[%s]";
    private static final String FORMAT_FAIL = "�4[%s]";
    
    public EssentialsSign(final String signName) {
        this.signName = signName;
    }
    
    public final boolean onSignCreate(final SignChangeEvent event, final IEssentials ess) {
        final ISign sign = new EventSign(event);
        final User user = ess.getUser(event.getPlayer());
        if (!user.isAuthorized("essentials.signs." + this.signName.toLowerCase() + ".create") && !user.isAuthorized("essentials.signs.create." + this.signName.toLowerCase())) {
            return true;
        }
        sign.setLine(0, String.format("�4[%s]", this.signName));
        try {
            final boolean ret = this.onSignCreate(sign, user, this.getUsername(user), ess);
            if (ret) {
                sign.setLine(0, this.getSuccessName());
            }
            return ret;
        }
        catch (ChargeException ex) {
            ess.showError((CommandSender)user, ex, this.signName);
        }
        catch (SignException ex2) {
            ess.showError((CommandSender)user, ex2, this.signName);
        }
        return true;
    }
    
    public String getSuccessName() {
        return String.format("�1[%s]", this.signName);
    }
    
    public String getTemplateName() {
        return String.format("[%s]", this.signName);
    }
    
    private String getUsername(final User user) {
        return user.getName().substring(0, (user.getName().length() > 13) ? 13 : user.getName().length());
    }
    
    public final boolean onSignInteract(final Block block, final Player player, final IEssentials ess) {
        final ISign sign = new BlockSign(block);
        final User user = ess.getUser(player);
        try {
            return (user.isAuthorized("essentials.signs." + this.signName.toLowerCase() + ".use") || user.isAuthorized("essentials.signs.use." + this.signName.toLowerCase())) && this.onSignInteract(sign, user, this.getUsername(user), ess);
        }
        catch (ChargeException ex) {
            ess.showError((CommandSender)user, ex, this.signName);
            return false;
        }
        catch (SignException ex2) {
            ess.showError((CommandSender)user, ex2, this.signName);
            return false;
        }
    }
    
    public final boolean onSignBreak(final Block block, final Player player, final IEssentials ess) {
        final ISign sign = new BlockSign(block);
        final User user = ess.getUser(player);
        try {
            return (user.isAuthorized("essentials.signs." + this.signName.toLowerCase() + ".break") || user.isAuthorized("essentials.signs.break." + this.signName.toLowerCase())) && this.onSignBreak(sign, user, this.getUsername(user), ess);
        }
        catch (SignException ex) {
            ess.showError((CommandSender)user, ex, this.signName);
            return false;
        }
    }
    
    protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        return true;
    }
    
    protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        return true;
    }
    
    protected boolean onSignBreak(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException {
        return true;
    }
    
    public final boolean onBlockPlace(final Block block, final Player player, final IEssentials ess) {
        final User user = ess.getUser(player);
        try {
            return this.onBlockPlace(block, user, this.getUsername(user), ess);
        }
        catch (ChargeException ex) {
            ess.showError((CommandSender)user, ex, this.signName);
        }
        catch (SignException ex2) {
            ess.showError((CommandSender)user, ex2, this.signName);
        }
        return false;
    }
    
    public final boolean onBlockInteract(final Block block, final Player player, final IEssentials ess) {
        final User user = ess.getUser(player);
        try {
            return this.onBlockInteract(block, user, this.getUsername(user), ess);
        }
        catch (ChargeException ex) {
            ess.showError((CommandSender)user, ex, this.signName);
        }
        catch (SignException ex2) {
            ess.showError((CommandSender)user, ex2, this.signName);
        }
        return false;
    }
    
    public final boolean onBlockBreak(final Block block, final Player player, final IEssentials ess) {
        final User user = ess.getUser(player);
        try {
            return this.onBlockBreak(block, user, this.getUsername(user), ess);
        }
        catch (SignException ex) {
            ess.showError((CommandSender)user, ex, this.signName);
            return false;
        }
    }
    
    public boolean onBlockExplode(final Block block, final IEssentials ess) {
        return true;
    }
    
    public boolean onBlockBurn(final Block block, final IEssentials ess) {
        return true;
    }
    
    public boolean onBlockIgnite(final Block block, final IEssentials ess) {
        return true;
    }
    
    public boolean onBlockPush(final Block block, final IEssentials ess) {
        return true;
    }
    
    public static boolean checkIfBlockBreaksSigns(final Block block) {
        final Block sign = block.getRelative(BlockFace.UP);
        if (sign.getType() == Material.SIGN_POST && isValidSign(new BlockSign(sign))) {
            return true;
        }
        final BlockFace[] arr$;
        final BlockFace[] directions = arr$ = new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
        for (final BlockFace blockFace : arr$) {
            final Block signblock = block.getRelative(blockFace);
            if (signblock.getType() == Material.WALL_SIGN) {
                final Sign signMat = (Sign)signblock.getState().getData();
                if (signMat.getFacing() == blockFace && isValidSign(new BlockSign(signblock))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isValidSign(final ISign sign) {
        return sign.getLine(0).matches("�1\\[.*\\]");
    }
    
    protected boolean onBlockPlace(final Block block, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        return true;
    }
    
    protected boolean onBlockInteract(final Block block, final User player, final String username, final IEssentials ess) throws SignException, ChargeException {
        return true;
    }
    
    protected boolean onBlockBreak(final Block block, final User player, final String username, final IEssentials ess) throws SignException {
        return true;
    }
    
    public Set<Material> getBlocks() {
        return EssentialsSign.EMPTY_SET;
    }
    
    protected final void validateTrade(final ISign sign, final int index, final IEssentials ess) throws SignException {
        final String line = sign.getLine(index).trim();
        if (line.isEmpty()) {
            return;
        }
        final Trade trade = this.getTrade(sign, index, 0, ess);
        final Double money = trade.getMoney();
        if (money != null) {
            sign.setLine(index, Util.formatCurrency(money, ess));
        }
    }
    
    protected final void validateTrade(final ISign sign, final int amountIndex, final int itemIndex, final User player, final IEssentials ess) throws SignException {
        final Trade trade = this.getTrade(sign, amountIndex, itemIndex, player, ess);
        final ItemStack item = trade.getItemStack();
        sign.setLine(amountIndex, Integer.toString(item.getAmount()));
        sign.setLine(itemIndex, sign.getLine(itemIndex).trim());
    }
    
    protected final Trade getTrade(final ISign sign, final int amountIndex, final int itemIndex, final User player, final IEssentials ess) throws SignException {
        final ItemStack item = this.getItemStack(sign.getLine(itemIndex), 1, ess);
        final int amount = Math.min(this.getIntegerPositive(sign.getLine(amountIndex)), item.getType().getMaxStackSize() * player.getInventory().getSize());
        if (item.getTypeId() == 0 || amount < 1) {
            throw new SignException(Util.i18n("moreThanZero"));
        }
        item.setAmount(amount);
        return new Trade(item, ess);
    }
    
    protected final void validateInteger(final ISign sign, final int index) throws SignException {
        final String line = sign.getLine(index).trim();
        if (line.isEmpty()) {
            throw new SignException("Empty line " + index);
        }
        final int quantity = this.getIntegerPositive(line);
        sign.setLine(index, Integer.toString(quantity));
    }
    
    protected final int getIntegerPositive(final String line) throws SignException {
        final int quantity = this.getInteger(line);
        if (quantity < 1) {
            throw new SignException(Util.i18n("moreThanZero"));
        }
        return quantity;
    }
    
    protected final int getInteger(final String line) throws SignException {
        try {
            final int quantity = Integer.parseInt(line);
            return quantity;
        }
        catch (NumberFormatException ex) {
            throw new SignException("Invalid sign", ex);
        }
    }
    
    protected final ItemStack getItemStack(final String itemName, final int quantity, final IEssentials ess) throws SignException {
        try {
            final ItemStack item = ess.getItemDb().get(itemName);
            item.setAmount(quantity);
            return item;
        }
        catch (Exception ex) {
            throw new SignException(ex.getMessage(), ex);
        }
    }
    
    protected final Double getMoney(final String line) throws SignException {
        final boolean isMoney = line.matches("^[^0-9-\\.][\\.0-9]+$");
        return isMoney ? this.getDoublePositive(line.substring(1)) : null;
    }
    
    protected final Double getDoublePositive(final String line) throws SignException {
        final double quantity = this.getDouble(line);
        if (Math.round(quantity * 100.0) < 1.0) {
            throw new SignException(Util.i18n("moreThanZero"));
        }
        return quantity;
    }
    
    protected final Double getDouble(final String line) throws SignException {
        try {
            return Double.parseDouble(line);
        }
        catch (NumberFormatException ex) {
            throw new SignException(ex.getMessage(), ex);
        }
    }
    
    protected final Trade getTrade(final ISign sign, final int index, final IEssentials ess) throws SignException {
        return this.getTrade(sign, index, 1, ess);
    }
    
    protected final Trade getTrade(final ISign sign, final int index, final int decrement, final IEssentials ess) throws SignException {
        final String line = sign.getLine(index).trim();
        if (line.isEmpty()) {
            return new Trade(this.signName.toLowerCase() + "sign", ess);
        }
        final Double money = this.getMoney(line);
        if (money != null) {
            return new Trade(money, ess);
        }
        final String[] split = line.split("[ :]+", 2);
        if (split.length != 2) {
            throw new SignException(Util.i18n("invalidCharge"));
        }
        final int quantity = this.getIntegerPositive(split[0]);
        final String item = split[1].toLowerCase();
        if (item.equalsIgnoreCase("times")) {
            sign.setLine(index, quantity - decrement + " times");
            return new Trade(this.signName.toLowerCase() + "sign", ess);
        }
        final ItemStack stack = this.getItemStack(item, quantity, ess);
        sign.setLine(index, quantity + " " + item);
        return new Trade(stack, ess);
    }
    
    static {
        EMPTY_SET = new HashSet<Material>();
    }
    
    static class EventSign implements ISign
    {
        private final transient SignChangeEvent event;
        private final transient Block block;
        
        public EventSign(final SignChangeEvent event) {
            this.event = event;
            this.block = event.getBlock();
        }
        
        @Override
        public final String getLine(final int index) {
            return this.event.getLine(index);
        }
        
        @Override
        public final void setLine(final int index, final String text) {
            this.event.setLine(index, text);
        }
        
        @Override
        public Block getBlock() {
            return this.block;
        }
        
        @Override
        public void updateSign() {
        }
    }
    
    static class BlockSign implements ISign
    {
        private final transient org.bukkit.block.Sign sign;
        private final transient Block block;
        
        public BlockSign(final Block block) {
            this.block = block;
            this.sign = (org.bukkit.block.Sign)block.getState();
        }
        
        @Override
        public final String getLine(final int index) {
            return this.sign.getLine(index);
        }
        
        @Override
        public final void setLine(final int index, final String text) {
            this.sign.setLine(index, text);
        }
        
        @Override
        public final Block getBlock() {
            return this.block;
        }
        
        @Override
        public final void updateSign() {
            this.sign.update();
        }
    }
    
    public interface ISign
    {
        String getLine(final int p0);
        
        void setLine(final int p0, final String p1);
        
        Block getBlock();
        
        void updateSign();
    }
}
