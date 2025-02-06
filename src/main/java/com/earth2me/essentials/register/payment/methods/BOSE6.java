package com.earth2me.essentials.register.payment.methods;

import com.earth2me.essentials.register.payment.*;
import cosine.boseconomy.*;
import org.bukkit.plugin.*;

public class BOSE6 implements Method
{
    private BOSEconomy BOSEconomy;
    
    @Override
    public BOSEconomy getPlugin() {
        return this.BOSEconomy;
    }
    
    @Override
    public String getName() {
        return "BOSEconomy";
    }
    
    @Override
    public String getVersion() {
        return "0.6.2";
    }
    
    @Override
    public String format(final double amount) {
        String currency = this.BOSEconomy.getMoneyNamePlural();
        if (amount == 1.0) {
            currency = this.BOSEconomy.getMoneyName();
        }
        return amount + " " + currency;
    }
    
    @Override
    public boolean hasBanks() {
        return true;
    }
    
    @Override
    public boolean hasBank(final String bank) {
        return this.BOSEconomy.bankExists(bank);
    }
    
    @Override
    public boolean hasAccount(final String name) {
        return this.BOSEconomy.playerRegistered(name, false);
    }
    
    @Override
    public boolean hasBankAccount(final String bank, final String name) {
        return this.BOSEconomy.isBankOwner(bank, name) || this.BOSEconomy.isBankMember(bank, name);
    }
    
    @Override
    public MethodAccount getAccount(final String name) {
        if (!this.hasAccount(name)) {
            return null;
        }
        return new BOSEAccount(name, this.BOSEconomy);
    }
    
    @Override
    public MethodBankAccount getBankAccount(final String bank, final String name) {
        if (!this.hasBankAccount(bank, name)) {
            return null;
        }
        return new BOSEBankAccount(bank, this.BOSEconomy);
    }
    
    @Override
    public boolean isCompatible(final Plugin plugin) {
        return plugin.getDescription().getName().equalsIgnoreCase("boseconomy") && plugin instanceof BOSEconomy && plugin.getDescription().getVersion().equals("0.6.2");
    }
    
    @Override
    public void setPlugin(final Plugin plugin) {
        this.BOSEconomy = (BOSEconomy)plugin;
    }
    
    public class BOSEAccount implements MethodAccount
    {
        private String name;
        private BOSEconomy BOSEconomy;
        
        public BOSEAccount(final String name, final BOSEconomy bOSEconomy) {
            this.name = name;
            this.BOSEconomy = bOSEconomy;
        }
        
        @Override
        public double balance() {
            return this.BOSEconomy.getPlayerMoney(this.name);
        }
        
        @Override
        public boolean set(final double amount) {
            final int IntAmount = (int)Math.ceil(amount);
            return this.BOSEconomy.setPlayerMoney(this.name, IntAmount, false);
        }
        
        @Override
        public boolean add(final double amount) {
            final int IntAmount = (int)Math.ceil(amount);
            return this.BOSEconomy.addPlayerMoney(this.name, IntAmount, false);
        }
        
        @Override
        public boolean subtract(final double amount) {
            final int IntAmount = (int)Math.ceil(amount);
            final int balance = (int)this.balance();
            return this.BOSEconomy.setPlayerMoney(this.name, balance - IntAmount, false);
        }
        
        @Override
        public boolean multiply(final double amount) {
            final int IntAmount = (int)Math.ceil(amount);
            final int balance = (int)this.balance();
            return this.BOSEconomy.setPlayerMoney(this.name, balance * IntAmount, false);
        }
        
        @Override
        public boolean divide(final double amount) {
            final int IntAmount = (int)Math.ceil(amount);
            final int balance = (int)this.balance();
            return this.BOSEconomy.setPlayerMoney(this.name, balance / IntAmount, false);
        }
        
        @Override
        public boolean hasEnough(final double amount) {
            return this.balance() >= amount;
        }
        
        @Override
        public boolean hasOver(final double amount) {
            return this.balance() > amount;
        }
        
        @Override
        public boolean hasUnder(final double amount) {
            return this.balance() < amount;
        }
        
        @Override
        public boolean isNegative() {
            return this.balance() < 0.0;
        }
        
        @Override
        public boolean remove() {
            return false;
        }
    }
    
    public class BOSEBankAccount implements MethodBankAccount
    {
        private final String bank;
        private final BOSEconomy BOSEconomy;
        
        public BOSEBankAccount(final String bank, final BOSEconomy bOSEconomy) {
            this.bank = bank;
            this.BOSEconomy = bOSEconomy;
        }
        
        @Override
        public String getBankName() {
            return this.bank;
        }
        
        @Override
        public int getBankId() {
            return -1;
        }
        
        @Override
        public double balance() {
            return this.BOSEconomy.getBankMoney(this.bank);
        }
        
        @Override
        public boolean set(final double amount) {
            final int IntAmount = (int)Math.ceil(amount);
            return this.BOSEconomy.setBankMoney(this.bank, IntAmount, true);
        }
        
        @Override
        public boolean add(final double amount) {
            final int IntAmount = (int)Math.ceil(amount);
            final int balance = (int)this.balance();
            return this.BOSEconomy.setBankMoney(this.bank, balance + IntAmount, false);
        }
        
        @Override
        public boolean subtract(final double amount) {
            final int IntAmount = (int)Math.ceil(amount);
            final int balance = (int)this.balance();
            return this.BOSEconomy.setBankMoney(this.bank, balance - IntAmount, false);
        }
        
        @Override
        public boolean multiply(final double amount) {
            final int IntAmount = (int)Math.ceil(amount);
            final int balance = (int)this.balance();
            return this.BOSEconomy.setBankMoney(this.bank, balance * IntAmount, false);
        }
        
        @Override
        public boolean divide(final double amount) {
            final int IntAmount = (int)Math.ceil(amount);
            final int balance = (int)this.balance();
            return this.BOSEconomy.setBankMoney(this.bank, balance / IntAmount, false);
        }
        
        @Override
        public boolean hasEnough(final double amount) {
            return this.balance() >= amount;
        }
        
        @Override
        public boolean hasOver(final double amount) {
            return this.balance() > amount;
        }
        
        @Override
        public boolean hasUnder(final double amount) {
            return this.balance() < amount;
        }
        
        @Override
        public boolean isNegative() {
            return this.balance() < 0.0;
        }
        
        @Override
        public boolean remove() {
            return this.BOSEconomy.removeBank(this.bank);
        }
    }
}
