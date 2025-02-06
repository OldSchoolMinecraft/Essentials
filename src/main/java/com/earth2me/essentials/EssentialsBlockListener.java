package com.earth2me.essentials;

import java.util.logging.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.*;
import org.bukkit.*;

public class EssentialsBlockListener extends BlockListener
{
    private final IEssentials ess;
    private static final Logger logger;
    
    public EssentialsBlockListener(final IEssentials ess) {
        this.ess = ess;
    }
    
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final User user = this.ess.getUser(event.getPlayer());
        final ItemStack is = new ItemStack(event.getBlockPlaced().getType(), 1, (short)0, Byte.valueOf(event.getBlockPlaced().getData()));
        switch (is.getType()) {
            case WOODEN_DOOR: {
                is.setType(Material.WOOD_DOOR);
                is.setDurability((short)0);
                break;
            }
            case IRON_DOOR_BLOCK: {
                is.setType(Material.IRON_DOOR);
                is.setDurability((short)0);
                break;
            }
            case SIGN_POST:
            case WALL_SIGN: {
                is.setType(Material.SIGN);
                is.setDurability((short)0);
                break;
            }
            case CROPS: {
                is.setType(Material.SEEDS);
                is.setDurability((short)0);
                break;
            }
            case CAKE_BLOCK: {
                is.setType(Material.CAKE);
                is.setDurability((short)0);
                break;
            }
            case BED_BLOCK: {
                is.setType(Material.BED);
                is.setDurability((short)0);
                break;
            }
            case REDSTONE_WIRE: {
                is.setType(Material.REDSTONE);
                is.setDurability((short)0);
                break;
            }
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON: {
                is.setType(Material.REDSTONE_TORCH_ON);
                is.setDurability((short)0);
                break;
            }
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON: {
                is.setType(Material.DIODE);
                is.setDurability((short)0);
                break;
            }
            case DOUBLE_STEP: {
                is.setType(Material.STEP);
                break;
            }
            case TORCH:
            case RAILS:
            case LADDER:
            case WOOD_STAIRS:
            case COBBLESTONE_STAIRS:
            case LEVER:
            case STONE_BUTTON:
            case FURNACE:
            case DISPENSER:
            case PUMPKIN:
            case JACK_O_LANTERN:
            case WOOD_PLATE:
            case STONE_PLATE:
            case PISTON_STICKY_BASE:
            case PISTON_BASE: {
                is.setDurability((short)0);
                break;
            }
            case FIRE: {
                return;
            }
        }
        final boolean unlimitedForUser = user.hasUnlimited(is);
        if (unlimitedForUser) {
            this.ess.scheduleSyncDelayedTask(new Runnable() {
                @Override
                public void run() {
                    user.getInventory().addItem(new ItemStack[] { is });
                    user.updateInventory();
                }
            });
        }
    }
    
    static {
        logger = Logger.getLogger("Minecraft");
    }
}
