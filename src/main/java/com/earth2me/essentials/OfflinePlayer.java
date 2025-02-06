package com.earth2me.essentials;

import java.net.*;

import com.projectposeidon.ConnectionType;
import net.minecraft.server.Packet;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import java.util.*;
import org.bukkit.permissions.*;
import org.bukkit.map.*;
import org.bukkit.util.Vector;

public class OfflinePlayer implements Player
{
    private final String name;
    private final org.bukkit.OfflinePlayer offlinePlayer;
    final transient IEssentials ess;
    private Location location;
    private World world;
    private UUID uniqueId;
    
    public OfflinePlayer(final String name, final IEssentials ess) {
        this.location = new Location((World)null, 0.0, 0.0, 0.0, 0.0f, 0.0f);
        this.uniqueId = UUID.randomUUID();
        this.name = name;
        this.ess = ess;
        this.world = ess.getServer().getWorlds().get(0);
        offlinePlayer = Bukkit.getOfflinePlayer(name);
    }
    
    public boolean isOnline() {
        return false;
    }
    
    public boolean isOp() {
        return false;
    }
    
    public void sendMessage(final String string) {
    }
    
    public String getDisplayName() {
        return this.name;
    }
    
    public void setDisplayName(final String string) {
    }
    
    public void setCompassTarget(final Location lctn) {
    }

    @Override
    public void openInventory(Inventory inventory) {}

    public InetSocketAddress getAddress() {
        return null;
    }
    
    public void kickPlayer(final String string) {
    }
    
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isBanned()
    {
        return offlinePlayer.isBanned();
    }

    @Override
    public void setBanned(boolean b)
    {
        offlinePlayer.setBanned(b);
    }

    @Override
    public boolean isWhitelisted()
    {
        return offlinePlayer.isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean b)
    {
        offlinePlayer.setWhitelisted(b);
    }

    public PlayerInventory getInventory() {
        return null;
    }
    
    public ItemStack getItemInHand() {
        return null;
    }
    
    public void setItemInHand(final ItemStack is) {
    }
    
    public int getHealth() {
        return 0;
    }
    
    public void setHealth(final int i) {
    }
    
    public Egg throwEgg() {
        return null;
    }
    
    public Snowball throwSnowball() {
        return null;
    }
    
    public Arrow shootArrow() {
        return null;
    }
    
    public boolean isInsideVehicle() {
        return false;
    }
    
    public boolean leaveVehicle() {
        return false;
    }
    
    public Vehicle getVehicle() {
        return null;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public void setLocation(final Location loc) {
        this.location = loc;
        this.world = loc.getWorld();
    }
    
    public void teleportTo(final Location lctn) {
    }
    
    public void teleportTo(final Entity entity) {
    }
    
    public int getEntityId() {
        return -1;
    }
    
    public boolean performCommand(final String string) {
        return false;
    }
    
    public boolean isPlayer() {
        return false;
    }
    
    public int getRemainingAir() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setRemainingAir(final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public int getMaximumAir() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setMaximumAir(final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public boolean isSneaking() {
        return false;
    }
    
    public void setSneaking(final boolean bln) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void updateInventory() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void chat(final String string) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public double getEyeHeight() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public double getEyeHeight(final boolean bln) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public List<Block> getLineOfSight(final HashSet<Byte> hs, final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public Block getTargetBlock(final HashSet<Byte> hs, final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public List<Block> getLastTwoTargetBlocks(final HashSet<Byte> hs, final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public int getFireTicks() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public int getMaxFireTicks() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setFireTicks(final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void remove() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public Server getServer() {
        return (this.ess == null) ? null : this.ess.getServer();
    }
    
    public Vector getMomentum() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setMomentum(final Vector vector) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setVelocity(final Vector vector) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public Vector getVelocity() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void damage(final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void damage(final int i, final Entity entity) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public Location getEyeLocation() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void sendRawMessage(final String string) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public Location getCompassTarget() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public int getMaximumNoDamageTicks() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setMaximumNoDamageTicks(final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public int getLastDamage() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setLastDamage(final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public int getNoDamageTicks() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setNoDamageTicks(final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public boolean teleport(final Location lctn) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public boolean teleport(final Entity entity) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public Entity getPassenger() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public boolean setPassenger(final Entity entity) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public boolean isEmpty() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public boolean eject() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void saveData() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void loadData() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public boolean isSleeping() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public int getSleepTicks() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public List<Entity> getNearbyEntities(final double d, final double d1, final double d2) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public boolean isDead() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public float getFallDistance() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setFallDistance(final float f) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setSleepingIgnored(final boolean bln) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public boolean isSleepingIgnored() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void awardAchievement(final Achievement a) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void incrementStatistic(final Statistic ststc) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void incrementStatistic(final Statistic ststc, final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void incrementStatistic(final Statistic ststc, final Material mtrl) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void incrementStatistic(final Statistic ststc, final Material mtrl, final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void playNote(final Location lctn, final byte b, final byte b1) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void sendBlockChange(final Location lctn, final Material mtrl, final byte b) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void sendBlockChange(final Location lctn, final int i, final byte b) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void setLastDamageCause(final EntityDamageEvent ede) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public EntityDamageEvent getLastDamageCause() {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public void playEffect(final Location lctn, final Effect effect, final int i) {
        throw new UnsupportedOperationException(Util.i18n("notSupportedYet"));
    }
    
    public boolean sendChunkChange(final Location lctn, final int i, final int i1, final int i2, final byte[] bytes) {
        return true;
    }
    
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public UUID getPlayerUUID()
    {
        return null;
    }

    public void playNote(final Location lctn, final Instrument i, final Note note) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setPlayerTime(final long l, final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public long getPlayerTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public long getPlayerTimeOffset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean isPlayerTimeRelative() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void hidePlayer(Player player)
    {
    }

    @Override
    public void showPlayer(Player player)
    {
    }

    @Override
    public boolean canSee(Player player)
    {
        return false;
    }

    @Override
    public void sendPacket(Player player, Packet packet)
    {
    }

    public boolean isPermissionSet(final String string) {
        return false;
    }
    
    public boolean isPermissionSet(final Permission prmsn) {
        return false;
    }
    
    public boolean hasPermission(final String string) {
        return false;
    }
    
    public boolean hasPermission(final Permission prmsn) {
        return false;
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final String string, final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final String string, final boolean bln, final int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void removeAttachment(final PermissionAttachment pa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void recalculatePermissions() {
    }
    
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setOp(final boolean bln) {
    }
    
    public void sendMap(final MapView mv) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
