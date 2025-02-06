package com.earth2me.essentials;

import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.generator.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.block.*;
import org.bukkit.util.Vector;

public class FakeWorld implements World
{
    private final String name;
    private final World.Environment env;
    
    FakeWorld(final String string, final World.Environment environment) {
        this.name = string;
        this.env = environment;
    }
    
    public Block getBlockAt(final int i, final int i1, final int i2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Block getBlockAt(final Location lctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int getBlockTypeIdAt(final int i, final int i1, final int i2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int getBlockTypeIdAt(final Location lctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int getHighestBlockYAt(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int getHighestBlockYAt(final Location lctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Chunk getChunkAt(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Chunk getChunkAt(final Location lctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Chunk getChunkAt(final Block block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean isChunkLoaded(final Chunk chunk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Chunk[] getLoadedChunks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void loadChunk(final Chunk chunk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean isChunkLoaded(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void loadChunk(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean loadChunk(final int i, final int i1, final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean unloadChunk(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean unloadChunk(final int i, final int i1, final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean unloadChunk(final int i, final int i1, final boolean bln, final boolean bln1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean unloadChunkRequest(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean unloadChunkRequest(final int i, final int i1, final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean regenerateChunk(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void refreshChunk(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Item dropItem(final Location lctn, final ItemStack is) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Item dropItemNaturally(final Location lctn, final ItemStack is) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Arrow spawnArrow(final Location lctn, final Vector vector, final float f, final float f1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean generateTree(final Location lctn, final TreeType tt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean generateTree(final Location lctn, final TreeType tt, final BlockChangeDelegate bcd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public LivingEntity spawnCreature(final Location lctn, final CreatureType ct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public LightningStrike strikeLightning(final Location lctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public LightningStrike strikeLightningEffect(final Location lctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<Entity> getEntities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<LivingEntity> getLivingEntities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<Player> getPlayers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public String getName() {
        return this.name;
    }
    
    public long getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Location getSpawnLocation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean setSpawnLocation(final int i, final int i1, final int i2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public long getTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setTime(final long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public long getFullTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setFullTime(final long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean hasStorm() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setStorm(final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int getWeatherDuration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setWeatherDuration(final int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean isThundering() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setThundering(final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int getThunderDuration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setThunderDuration(final int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public World.Environment getEnvironment() {
        return this.env;
    }
    
    public long getSeed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean getPVP() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setPVP(final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void save() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean createExplosion(final double d, final double d1, final double d2, final float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean createExplosion(final Location lctn, final float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ChunkGenerator getGenerator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<BlockPopulator> getPopulators() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void playEffect(final Location lctn, final Effect effect, final int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void playEffect(final Location lctn, final Effect effect, final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean createExplosion(final double d, final double d1, final double d2, final float f, final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean createExplosion(final Location lctn, final float f, final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public <T extends Entity> T spawn(final Location lctn, final Class<T> type) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ChunkSnapshot getEmptyChunkSnapshot(final int i, final int i1, final boolean bln, final boolean bln1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setSpawnFlags(final boolean bln, final boolean bln1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean getAllowAnimals() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean getAllowMonsters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public UUID getUID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Block getHighestBlockAt(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Block getHighestBlockAt(final Location lctn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Biome getBiome(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public double getTemperature(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public double getHumidity(final int i, final int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean unloadChunk(final Chunk chunk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int getMaxHeight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean getKeepSpawnInMemory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setKeepSpawnInMemory(final boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAutoSave()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAutoSave(boolean b)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
