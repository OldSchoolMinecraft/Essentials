package com.earth2me.essentials.register.payment.methods;

import com.earth2me.essentials.register.payment.*;
import com.iConomy.*;
import com.iConomy.util.*;
import org.bukkit.plugin.*;
import com.iConomy.system.*;

public class iCo5 implements Method
{
    private iConomy iConomy;
    
    @Override
    public iConomy getPlugin() {
        return this.iConomy;
    }
    
    @Override
    public String getName() {
        return "iConomy";
    }
    
    @Override
    public String getVersion() {
        return "5";
    }
    
    @Override
    public String format(final double amount) {
        final iConomy iConomy = this.iConomy;
        return com.iConomy.iConomy.format(amount);
    }
    
    @Override
    public boolean hasBanks() {
        return Constants.Banking;
    }
    
    @Override
    public boolean hasBank(final String bank) {
        if (this.hasBanks()) {
            final iConomy iConomy = this.iConomy;
            if (com.iConomy.iConomy.Banks.exists(bank)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasAccount(final String name) {
        final iConomy iConomy = this.iConomy;
        return com.iConomy.iConomy.hasAccount(name);
    }
    
    @Override
    public boolean hasBankAccount(final String bank, final String name) {
        if (this.hasBank(bank)) {
            final iConomy iConomy = this.iConomy;
            if (com.iConomy.iConomy.getBank(bank).hasAccount(name)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public MethodAccount getAccount(final String name) {
        final iConomy iConomy = this.iConomy;
        return new iCoAccount(com.iConomy.iConomy.getAccount(name));
    }
    
    @Override
    public MethodBankAccount getBankAccount(final String bank, final String name) {
        final iConomy iConomy = this.iConomy;
        return new iCoBankAccount(com.iConomy.iConomy.getBank(bank).getAccount(name));
    }
    
    @Override
    public boolean isCompatible(final Plugin plugin) {
        return plugin.getDescription().getName().equalsIgnoreCase("iconomy") && plugin.getClass().getName().equals("com.iConomy.iConomy") && plugin instanceof iConomy;
    }
    
    @Override
    public void setPlugin(final Plugin plugin) {
        this.iConomy = (iConomy)plugin;
    }
    
    public class iCoAccount implements MethodAccount
    {
        private Account account;
        private Holdings holdings;
        
        public iCoAccount(final Account account) {
            this.account = account;
            this.holdings = account.getHoldings();
        }
        
        public Account getiCoAccount() {
            return this.account;
        }
        
        @Override
        public double balance() {
            return this.holdings.balance();
        }
        
        @Override
        public boolean set(final double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.set(amount);
            return true;
        }
        
        @Override
        public boolean add(final double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.add(amount);
            return true;
        }
        
        @Override
        public boolean subtract(final double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.subtract(amount);
            return true;
        }
        
        @Override
        public boolean multiply(final double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.multiply(amount);
            return true;
        }
        
        @Override
        public boolean divide(final double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.divide(amount);
            return true;
        }
        
        @Override
        public boolean hasEnough(final double amount) {
            return this.holdings.hasEnough(amount);
        }
        
        @Override
        public boolean hasOver(final double amount) {
            return this.holdings.hasOver(amount);
        }
        
        @Override
        public boolean hasUnder(final double amount) {
            return this.holdings.hasUnder(amount);
        }
        
        @Override
        public boolean isNegative() {
            return this.holdings.isNegative();
        }
        
        @Override
        public boolean remove() {
            if (this.account == null) {
                return false;
            }
            this.account.remove();
            return true;
        }
    }
    
    public class iCoBankAccount implements MethodBankAccount
    {
        private BankAccount account;
        private Holdings holdings;
        
        public iCoBankAccount(final BankAccount account) {
            this.account = account;
            this.holdings = account.getHoldings();
        }
        
        public BankAccount getiCoBankAccount() {
            return this.account;
        }
        
        @Override
        public String getBankName() {
            return this.account.getBankName();
        }
        
        @Override
        public int getBankId() {
            return this.account.getBankId();
        }
        
        @Override
        public double balance() {
            return this.holdings.balance();
        }
        
        @Override
        public boolean set(final double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.set(amount);
            return true;
        }
        
        @Override
        public boolean add(final double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.add(amount);
            return true;
        }
        
        @Override
        public boolean subtract(final double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.subtract(amount);
            return true;
        }
        
        @Override
        public boolean multiply(final double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.multiply(amount);
            return true;
        }
        
        @Override
        public boolean divide(final double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.divide(amount);
            return true;
        }
        
        @Override
        public boolean hasEnough(final double amount) {
            return this.holdings.hasEnough(amount);
        }
        
        @Override
        public boolean hasOver(final double amount) {
            return this.holdings.hasOver(amount);
        }
        
        @Override
        public boolean hasUnder(final double amount) {
            return this.holdings.hasUnder(amount);
        }
        
        @Override
        public boolean isNegative() {
            return this.holdings.isNegative();
        }
        
        @Override
        public boolean remove() {
            if (this.account == null) {
                return false;
            }
            this.account.remove();
            return true;
        }
    }
}
