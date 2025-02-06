package com.earth2me.essentials;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.earth2me.essentials.commands.*;
import java.util.*;
import org.bukkit.*;
import com.earth2me.essentials.register.payment.*;

public class User extends UserData implements Comparable<User>, IReplyTo, IUser
{
    private boolean justPortaled;
    private CommandSender replyTo;
    private transient User teleportRequester;
    private transient boolean teleportRequestHere;
    private final transient Teleport teleport;
    private transient long lastOnlineActivity;
    private transient long lastActivity;
    private boolean hidden;
    private transient boolean godStateBeforeAfk;
    private transient Location afkPosition;
    
    User(final Player base, final IEssentials ess) {
        super(base, ess);
        this.justPortaled = false;
        this.replyTo = null;
        this.lastActivity = System.currentTimeMillis();
        this.hidden = false;
        this.teleport = new Teleport(this, ess);
        this.godStateBeforeAfk = this.isGodModeEnabled();
        this.afkPosition = this.getLocation();
    }
    
    User update(final Player base) {
        this.setBase(base);
        return this;
    }
    
    @Override
    public boolean isAuthorized(final IEssentialsCommand cmd) {
        return this.isAuthorized(cmd, "essentials.");
    }
    
    @Override
    public boolean isAuthorized(final IEssentialsCommand cmd, final String permissionPrefix) {
        return this.isAuthorized(permissionPrefix + (cmd.getName().equals("r") ? "msg" : cmd.getName()));
    }
    
    @Override
    public boolean isAuthorized(final String node) {
        return !(this.base instanceof OfflinePlayer) && (this.isOp() || (!this.isJailed() && this.ess.getPermissionsHandler().hasPermission(this.base, node)));
    }
    
    public void healCooldown() throws Exception {
        final Calendar now = new GregorianCalendar();
        if (this.getLastHealTimestamp() > 0L) {
            final double cooldown = this.ess.getSettings().getHealCooldown();
            final Calendar cooldownTime = new GregorianCalendar();
            cooldownTime.setTimeInMillis(this.getLastHealTimestamp());
            cooldownTime.add(13, (int)cooldown);
            cooldownTime.add(14, (int)(cooldown * 1000.0 % 1000.0));
            if (cooldownTime.after(now) && !this.isAuthorized("essentials.heal.cooldown.bypass")) {
                throw new Exception(Util.format("timeBeforeHeal", Util.formatDateDiff(cooldownTime.getTimeInMillis())));
            }
        }
        this.setLastHealTimestamp(now.getTimeInMillis());
    }
    
    @Override
    public void giveMoney(final double value) {
        this.giveMoney(value, null);
    }
    
    public void giveMoney(final double value, final CommandSender initiator) {
        if (value == 0.0) {
            return;
        }
        this.setMoney(this.getMoney() + value);
        this.sendMessage(Util.format("addedToAccount", Util.formatCurrency(value, this.ess)));
        if (initiator != null) {
            initiator.sendMessage(Util.format("addedToOthersAccount", Util.formatCurrency(value, this.ess), this.getDisplayName()));
        }
    }
    
    public void payUser(final User reciever, final double value) throws Exception {
        if (value == 0.0) {
            return;
        }
        if (this.canAfford(value)) {
            this.setMoney(this.getMoney() - value);
            reciever.setMoney(reciever.getMoney() + value);
            this.sendMessage(Util.format("moneySentTo", Util.formatCurrency(value, this.ess), reciever.getDisplayName()));
            reciever.sendMessage(Util.format("moneyRecievedFrom", Util.formatCurrency(value, this.ess), this.getDisplayName()));
            return;
        }
        throw new Exception(Util.i18n("notEnoughMoney"));
    }
    
    @Override
    public void takeMoney(final double value) {
        this.takeMoney(value, null);
    }
    
    public void takeMoney(final double value, final CommandSender initiator) {
        if (value == 0.0) {
            return;
        }
        this.setMoney(this.getMoney() - value);
        this.sendMessage(Util.format("takenFromAccount", Util.formatCurrency(value, this.ess)));
        if (initiator != null) {
            initiator.sendMessage(Util.format("takenFromOthersAccount", Util.formatCurrency(value, this.ess), this.getDisplayName()));
        }
    }
    
    public boolean canAfford(final double cost) {
        final double mon = this.getMoney();
        return mon >= cost || this.isAuthorized("essentials.eco.loan");
    }
    
    public void dispose() {
        this.base = (Player)new OfflinePlayer(this.getName(), this.ess);
    }
    
    public boolean getJustPortaled() {
        return this.justPortaled;
    }
    
    public void setJustPortaled(final boolean value) {
        this.justPortaled = value;
    }
    
    @Override
    public void setReplyTo(final CommandSender user) {
        this.replyTo = user;
    }
    
    @Override
    public CommandSender getReplyTo() {
        return this.replyTo;
    }
    
    @Override
    public int compareTo(final User other) {
        return ChatColor.stripColor(this.getDisplayName()).compareToIgnoreCase(ChatColor.stripColor(other.getDisplayName()));
    }
    
    @Override
    public boolean equals(final Object object) {
        return object instanceof User && ChatColor.stripColor(this.getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(((User)object).getDisplayName()));
    }
    
    @Override
    public int hashCode() {
        return ChatColor.stripColor(this.getDisplayName()).hashCode();
    }
    
    public Boolean canSpawnItem(final int itemId) {
        return !this.ess.getSettings().itemSpawnBlacklist().contains(itemId);
    }
    
    public Location getHome() throws Exception {
        return this.getHome(this.getHomes().get(0));
    }
    
    public void setHome() {
        this.setHome("home", this.getLocation());
    }
    
    public void setHome(final String name) {
        this.setHome(name, this.getLocation());
    }
    
    @Override
    public void setLastLocation() {
        this.setLastLocation(this.getLocation());
    }
    
    public void requestTeleport(final User player, final boolean here) {
        this.teleportRequester = player;
        this.teleportRequestHere = here;
    }
    
    public User getTeleportRequest() {
        return this.teleportRequester;
    }
    
    public boolean isTeleportRequestHere() {
        return this.teleportRequestHere;
    }
    
    public String getNick() {
        final StringBuilder nickname = new StringBuilder();
        final String nick = this.getNickname();
        if (this.ess.getSettings().isCommandDisabled("nick") || nick == null || nick.isEmpty() || nick.equals(this.getName())) {
            nickname.append(this.getName());
        }
        else {
            nickname.append(this.ess.getSettings().getNicknamePrefix()).append(nick);
        }
        if (this.isOp()) {
            try {
                nickname.insert(0, this.ess.getSettings().getOperatorColor().toString());
                nickname.append("§f");
            }
            catch (Exception ex) {}
        }
        if (this.ess.getSettings().addPrefixSuffix()) {
            final String prefix = this.ess.getPermissionsHandler().getPrefix(this.base).replace('&', '§').replace("{WORLDNAME}", this.getWorld().getName());
            final String suffix = this.ess.getPermissionsHandler().getSuffix(this.base).replace('&', '§').replace("{WORLDNAME}", this.getWorld().getName());
            nickname.insert(0, prefix);
            nickname.append(suffix);
            if (suffix.length() < 2 || !suffix.substring(suffix.length() - 2, suffix.length() - 1).equals("§")) {
                nickname.append("§f");
            }
        }
        return nickname.toString();
    }
    
    public Teleport getTeleport() {
        return this.teleport;
    }
    
    public long getLastOnlineActivity() {
        return this.lastOnlineActivity;
    }
    
    public void setLastOnlineActivity(final long timestamp) {
        this.lastOnlineActivity = timestamp;
    }
    
    @Override
    public double getMoney() {
        if (this.ess.getPaymentMethod().hasMethod()) {
            try {
                final Method method = this.ess.getPaymentMethod().getMethod();
                if (!method.hasAccount(this.getName())) {
                    throw new Exception();
                }
                final Method.MethodAccount account = this.ess.getPaymentMethod().getMethod().getAccount(this.getName());
                return account.balance();
            }
            catch (Throwable t) {}
        }
        return super.getMoney();
    }
    
    @Override
    public void setMoney(final double value) {
        if (this.ess.getPaymentMethod().hasMethod()) {
            try {
                final Method method = this.ess.getPaymentMethod().getMethod();
                if (!method.hasAccount(this.getName())) {
                    throw new Exception();
                }
                final Method.MethodAccount account = this.ess.getPaymentMethod().getMethod().getAccount(this.getName());
                account.set(value);
            }
            catch (Throwable t) {}
        }
        super.setMoney(value);
    }
    
    @Override
    public void setAfk(final boolean set) {
        this.setSleepingIgnored(this.isAuthorized("essentials.sleepingignored") || set);
        if (set && !this.isAfk() && this.ess.getSettings().getFreezeAfkPlayers()) {
            this.godStateBeforeAfk = this.isGodModeEnabled();
            this.setGodModeEnabled(true);
        }
        if (!set && this.isAfk() && this.ess.getSettings().getFreezeAfkPlayers()) {
            this.setGodModeEnabled(this.godStateBeforeAfk);
        }
        if (set && !this.isAfk()) {
            this.afkPosition = this.getLocation();
        }
        super.setAfk(set);
    }
    
    @Override
    public boolean toggleAfk() {
        final boolean now = super.toggleAfk();
        this.setSleepingIgnored(this.isAuthorized("essentials.sleepingignored") || now);
        return now;
    }
    
    @Override
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public void checkJailTimeout(final long currentTime) {
        if (this.getJailTimeout() > 0L && this.getJailTimeout() < currentTime && this.isJailed()) {
            this.setJailTimeout(0L);
            this.setJailed(false);
            this.sendMessage(Util.i18n("haveBeenReleased"));
            this.setJail(null);
            try {
                this.getTeleport().back();
            }
            catch (Exception ex) {}
        }
    }
    
    public void checkMuteTimeout(final long currentTime) {
        if (this.getMuteTimeout() > 0L && this.getMuteTimeout() < currentTime && this.isMuted()) {
            this.setMuteTimeout(0L);
            this.sendMessage(Util.i18n("canTalkAgain"));
            this.setMuted(false);
        }
    }
    
    public void checkBanTimeout(final long currentTime) {
        if (this.getBanTimeout() > 0L && this.getBanTimeout() < currentTime && this.ess.getBans().isNameBanned(this.getName())) {
            this.setBanTimeout(0L);
            this.ess.getBans().unbanByName(this.getName());
        }
    }
    
    public void updateActivity(final boolean broadcast) {
        if (this.isAfk()) {
            this.setAfk(false);
            if (broadcast && !this.isHidden()) {
                this.ess.broadcastMessage(this, ChatColor.translateAlternateColorCodes('&', Util.format("userIsNotAway", this.getDisplayName())));
            }
        }
        this.lastActivity = System.currentTimeMillis();
    }
    
    public void checkActivity() {
        final long autoafkkick = this.ess.getSettings().getAutoAfkKick();
        if (autoafkkick > 0L && this.lastActivity + autoafkkick * 1000L < System.currentTimeMillis() && !this.isHidden() && !this.isAuthorized("essentials.kick.exempt") && !this.isAuthorized("essentials.afk.kickexempt")) {
            final String kickReason = Util.format("autoAfkKickReason", autoafkkick / 60.0);
            this.kickPlayer(kickReason);
            for (final Player player : this.ess.getServer().getOnlinePlayers()) {
                final User user = this.ess.getUser(player);
                if (user.isAuthorized("essentials.kick.notify")) {
                    player.sendMessage(Util.format("playerKicked", "Console", this.getName(), kickReason));
                }
            }
        }
        final long autoafk = this.ess.getSettings().getAutoAfk();
        if (!this.isAfk() && autoafk > 0L && this.lastActivity + autoafk * 1000L < System.currentTimeMillis()) {
            this.setAfk(true);
            if (!this.isHidden()) {
                this.ess.broadcastMessage(this, ChatColor.translateAlternateColorCodes('&', Util.format("userIsAway", this.getDisplayName())));
            }
        }
    }
    
    public Location getAfkPosition() {
        return this.afkPosition;
    }
}
