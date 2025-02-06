package com.earth2me.essentials;

import java.net.*;

import com.projectposeidon.ConnectionType;
import net.minecraft.server.Packet;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.util.*;
import org.bukkit.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import java.util.*;
import org.bukkit.permissions.*;
import org.bukkit.map.*;
import org.bukkit.util.Vector;

public class PlayerWrapper implements Player
{
    protected Player base;
    
    public PlayerWrapper(final Player base) {
        this.base = base;
    }
    
    public final Player getBase() {
        return this.base;
    }
    
    public final Player setBase(final Player base) {
        return this.base = base;
    }
    
    public void setDisplayName(final String string) {
        this.base.setDisplayName(string);
    }
    
    public void setCompassTarget(final Location lctn) {
        this.base.setCompassTarget(lctn);
    }

    @Override
    public void openInventory(Inventory inventory)
    {
        base.openInventory(inventory);
    }

    public InetSocketAddress getAddress() {
        return this.base.getAddress();
    }
    
    public void kickPlayer(final String string) {
        this.base.kickPlayer(string);
    }
    
    public String getName() {
        return this.base.getName();
    }

    @Override
    public boolean isBanned()
    {
        return false;
    }

    @Override
    public void setBanned(boolean b)
    {

    }

    @Override
    public boolean isWhitelisted()
    {
        return false;
    }

    @Override
    public void setWhitelisted(boolean b)
    {

    }

    public PlayerInventory getInventory() {
        return this.base.getInventory();
    }
    
    public ItemStack getItemInHand() {
        return this.base.getItemInHand();
    }
    
    public void setItemInHand(final ItemStack is) {
        this.base.setItemInHand(is);
    }
    
    public int getHealth() {
        return this.base.getHealth();
    }
    
    public void setHealth(final int i) {
        this.base.setHealth(i);
    }
    
    public Egg throwEgg() {
        return this.base.throwEgg();
    }
    
    public Snowball throwSnowball() {
        return this.base.throwSnowball();
    }
    
    public Arrow shootArrow() {
        return this.base.shootArrow();
    }
    
    public boolean isInsideVehicle() {
        return this.base.isInsideVehicle();
    }
    
    public boolean leaveVehicle() {
        return this.base.leaveVehicle();
    }
    
    public Vehicle getVehicle() {
        return this.base.getVehicle();
    }
    
    public Location getLocation() {
        return this.base.getLocation();
    }
    
    public World getWorld() {
        return this.base.getWorld();
    }
    
    public Server getServer() {
        return this.base.getServer();
    }
    
    public boolean isOnline() {
        return this.base.isOnline();
    }
    
    public boolean isOp() {
        return this.base.isOp();
    }
    
    public boolean teleport(final Location lctn) {
        return this.base.teleport(lctn);
    }
    
    public boolean teleport(final Entity entity) {
        return this.base.teleport(entity);
    }
    
    public void sendMessage(final String string) {
        this.base.sendMessage(string);
    }
    
    public void setVelocity(final Vector vector) {
        this.base.setVelocity(vector);
    }
    
    public Vector getVelocity() {
        return this.base.getVelocity();
    }
    
    public double getEyeHeight() {
        return this.base.getEyeHeight();
    }
    
    public double getEyeHeight(final boolean bln) {
        return this.base.getEyeHeight(bln);
    }
    
    public List<Block> getLineOfSight(final HashSet<Byte> hs, final int i) {
        return (List<Block>)this.base.getLineOfSight((HashSet)hs, i);
    }
    
    public Block getTargetBlock(final HashSet<Byte> hs, final int i) {
        return this.base.getTargetBlock((HashSet)hs, i);
    }
    
    public List<Block> getLastTwoTargetBlocks(final HashSet<Byte> hs, final int i) {
        return (List<Block>)this.base.getLastTwoTargetBlocks((HashSet)hs, i);
    }
    
    public int getFireTicks() {
        return this.base.getFireTicks();
    }
    
    public int getMaxFireTicks() {
        return this.base.getMaxFireTicks();
    }
    
    public void setFireTicks(final int i) {
        this.base.setFireTicks(i);
    }
    
    public void remove() {
        this.base.remove();
    }
    
    public void updateInventory() {
        this.base.updateInventory();
    }
    
    public void chat(final String string) {
        this.base.chat(string);
    }
    
    public boolean isSneaking() {
        return this.base.isSneaking();
    }
    
    public void setSneaking(final boolean bln) {
        this.base.setSneaking(bln);
    }
    
    public int getEntityId() {
        return this.base.getEntityId();
    }
    
    public boolean performCommand(final String string) {
        return this.base.performCommand(string);
    }
    
    public int getRemainingAir() {
        return this.base.getRemainingAir();
    }
    
    public void setRemainingAir(final int i) {
        this.base.setRemainingAir(i);
    }
    
    public int getMaximumAir() {
        return this.base.getMaximumAir();
    }
    
    public void setMaximumAir(final int i) {
        this.base.setMaximumAir(i);
    }
    
    public String getDisplayName() {
        if (this.base.getDisplayName() != null) {
            return this.base.getDisplayName();
        }
        return this.base.getName();
    }
    
    public void damage(final int i) {
        this.base.damage(i);
    }
    
    public void damage(final int i, final Entity entity) {
        this.base.damage(i, entity);
    }
    
    public Location getEyeLocation() {
        return this.base.getEyeLocation();
    }
    
    public void sendRawMessage(final String string) {
        this.base.sendRawMessage(string);
    }
    
    public Location getCompassTarget() {
        return this.base.getCompassTarget();
    }
    
    public int getMaximumNoDamageTicks() {
        return this.base.getMaximumNoDamageTicks();
    }
    
    public void setMaximumNoDamageTicks(final int i) {
        this.base.setMaximumNoDamageTicks(i);
    }
    
    public int getLastDamage() {
        return this.base.getLastDamage();
    }
    
    public void setLastDamage(final int i) {
        this.base.setLastDamage(i);
    }
    
    public int getNoDamageTicks() {
        return this.base.getNoDamageTicks();
    }
    
    public void setNoDamageTicks(final int i) {
        this.base.setNoDamageTicks(i);
    }
    
    public Entity getPassenger() {
        return this.base.getPassenger();
    }
    
    public boolean setPassenger(final Entity entity) {
        return this.base.setPassenger(entity);
    }
    
    public boolean isEmpty() {
        return this.base.isEmpty();
    }
    
    public boolean eject() {
        return this.base.eject();
    }
    
    public void saveData() {
        this.base.saveData();
    }
    
    public void loadData() {
        this.base.loadData();
    }
    
    public boolean isSleeping() {
        return this.base.isSleeping();
    }
    
    public int getSleepTicks() {
        return this.base.getSleepTicks();
    }
    
    public List<Entity> getNearbyEntities(final double d, final double d1, final double d2) {
        return (List<Entity>)this.base.getNearbyEntities(d, d1, d2);
    }
    
    public boolean isDead() {
        return this.base.isDead();
    }
    
    public float getFallDistance() {
        return this.base.getFallDistance();
    }
    
    public void setFallDistance(final float f) {
        this.base.setFallDistance(f);
    }
    
    public void setSleepingIgnored(final boolean bln) {
        this.base.setSleepingIgnored(bln);
    }
    
    public boolean isSleepingIgnored() {
        return this.base.isSleepingIgnored();
    }
    
    public void awardAchievement(final Achievement a) {
        this.base.awardAchievement(a);
    }
    
    public void incrementStatistic(final Statistic ststc) {
        this.base.incrementStatistic(ststc);
    }
    
    public void incrementStatistic(final Statistic ststc, final int i) {
        this.base.incrementStatistic(ststc, i);
    }
    
    public void incrementStatistic(final Statistic ststc, final Material mtrl) {
        this.base.incrementStatistic(ststc, mtrl);
    }
    
    public void incrementStatistic(final Statistic ststc, final Material mtrl, final int i) {
        this.base.incrementStatistic(ststc, mtrl, i);
    }
    
    public void playNote(final Location lctn, final byte b, final byte b1) {
        this.base.playNote(lctn, b, b1);
    }
    
    public void sendBlockChange(final Location lctn, final Material mtrl, final byte b) {
        this.base.sendBlockChange(lctn, mtrl, b);
    }
    
    public void sendBlockChange(final Location lctn, final int i, final byte b) {
        this.base.sendBlockChange(lctn, i, b);
    }
    
    public void setLastDamageCause(final EntityDamageEvent ede) {
        this.base.setLastDamageCause(ede);
    }
    
    public EntityDamageEvent getLastDamageCause() {
        return this.base.getLastDamageCause();
    }
    
    public void playEffect(final Location lctn, final Effect effect, final int i) {
        this.base.playEffect(lctn, effect, i);
    }
    
    public boolean sendChunkChange(final Location lctn, final int i, final int i1, final int i2, final byte[] bytes) {
        return this.base.sendChunkChange(lctn, i, i1, i2, bytes);
    }
    
    public UUID getUniqueId() {
        return this.base.getUniqueId();
    }

    @Override
    public UUID getPlayerUUID()
    {
        return null;
    }

    public void playNote(final Location lctn, final Instrument i, final Note note) {
        this.base.playNote(lctn, i, note);
    }
    
    public void setPlayerTime(final long l, final boolean bln) {
        this.base.setPlayerTime(l, bln);
    }
    
    public long getPlayerTime() {
        return this.base.getPlayerTime();
    }
    
    public long getPlayerTimeOffset() {
        return this.base.getPlayerTimeOffset();
    }
    
    public boolean isPlayerTimeRelative() {
        return this.base.isPlayerTimeRelative();
    }

    @Override
    public ConnectionType getConnectionType()
    {
        return null;
    }

    @Override
    public boolean hasReceivedPacket0()
    {
        return false;
    }

    @Override
    public boolean isUsingReleaseToBeta()
    {
        return false;
    }

    public void resetPlayerTime() {
        this.base.resetPlayerTime();
    }

    @Override
    public void hidePlayer(Player player)
    {
        base.hidePlayer(player);
    }

    @Override
    public void showPlayer(Player player)
    {
        base.showPlayer(player);
    }

    @Override
    public boolean canSee(Player player)
    {
        return base.canSee(player);
    }

    @Override
    public void sendPacket(Player player, Packet packet)
    {
        base.sendPacket(player, packet);
    }

    public boolean isPermissionSet(final String string) {
        return this.base.isPermissionSet(string);
    }
    
    public boolean isPermissionSet(final Permission prmsn) {
        return this.base.isPermissionSet(prmsn);
    }
    
    public boolean hasPermission(final String string) {
        return this.base.hasPermission(string);
    }
    
    public boolean hasPermission(final Permission prmsn) {
        return this.base.hasPermission(prmsn);
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final String string, final boolean bln) {
        return this.base.addAttachment(plugin, string, bln);
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin) {
        return this.base.addAttachment(plugin);
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final String string, final boolean bln, final int i) {
        return this.base.addAttachment(plugin, string, bln, i);
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final int i) {
        return this.base.addAttachment(plugin, i);
    }
    
    public void removeAttachment(final PermissionAttachment pa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void recalculatePermissions() {
        this.base.recalculatePermissions();
    }
    
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return (Set<PermissionAttachmentInfo>)this.base.getEffectivePermissions();
    }
    
    public void setOp(final boolean bln) {
        this.base.setOp(bln);
    }
    
    public void sendMap(final MapView mv) {
        this.base.sendMap(mv);
    }
}
