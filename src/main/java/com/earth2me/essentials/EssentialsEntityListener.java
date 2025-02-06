package com.earth2me.essentials;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.event.entity.*;

public class EssentialsEntityListener extends EntityListener
{
    private final IEssentials ess;
    
    public EssentialsEntityListener(final IEssentials ess) {
        this.ess = ess;
    }
    
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent edEvent = (EntityDamageByEntityEvent)event;
            final Entity eAttack = edEvent.getDamager();
            final Entity eDefend = edEvent.getEntity();
            if (eDefend instanceof Player && eAttack instanceof Player) {
                final User defender = this.ess.getUser(eDefend);
                final User attacker = this.ess.getUser(eAttack);
                final ItemStack is = attacker.getItemInHand();
                final List<String> commandList = attacker.getPowertool(is);
                if (commandList != null && !commandList.isEmpty()) {
                    for (final String command : commandList) {
                        if (command != null && !command.isEmpty()) {
                            attacker.getServer().dispatchCommand((CommandSender)attacker, command.replaceAll("\\{player\\}", defender.getName()));
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
        if (event.getEntity() instanceof Player && this.ess.getUser(event.getEntity()).isGodModeEnabled()) {
            final Player player = (Player)event.getEntity();
            player.setFireTicks(0);
            player.setRemainingAir(player.getMaximumAir());
            event.setCancelled(true);
        }
    }
    
    public void onEntityCombust(final EntityCombustEvent event) {
        if (event.getEntity() instanceof Player && this.ess.getUser(event.getEntity()).isGodModeEnabled()) {
            event.setCancelled(true);
        }
    }
    
    public void onEntityDeath(final EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            final User user = this.ess.getUser(event.getEntity());
            if (user.isAuthorized("essentials.back.ondeath") && !this.ess.getSettings().isCommandDisabled("back")) {
                user.setLastLocation();
                user.sendMessage(Util.i18n("backAfterDeath"));
            }
        }
    }
}
