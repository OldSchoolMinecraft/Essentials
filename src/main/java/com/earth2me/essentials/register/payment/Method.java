package com.earth2me.essentials.register.payment;

import org.bukkit.plugin.*;

public interface Method
{
    Object getPlugin();
    
    String getName();
    
    String getVersion();
    
    String format(final double p0);
    
    boolean hasBanks();
    
    boolean hasBank(final String p0);
    
    boolean hasAccount(final String p0);
    
    boolean hasBankAccount(final String p0, final String p1);
    
    MethodAccount getAccount(final String p0);
    
    MethodBankAccount getBankAccount(final String p0, final String p1);
    
    boolean isCompatible(final Plugin p0);
    
    void setPlugin(final Plugin p0);
    
    public interface MethodBankAccount
    {
        double balance();
        
        String getBankName();
        
        int getBankId();
        
        boolean set(final double p0);
        
        boolean add(final double p0);
        
        boolean subtract(final double p0);
        
        boolean multiply(final double p0);
        
        boolean divide(final double p0);
        
        boolean hasEnough(final double p0);
        
        boolean hasOver(final double p0);
        
        boolean hasUnder(final double p0);
        
        boolean isNegative();
        
        boolean remove();
        
        String toString();
    }
    
    public interface MethodAccount
    {
        double balance();
        
        boolean set(final double p0);
        
        boolean add(final double p0);
        
        boolean subtract(final double p0);
        
        boolean multiply(final double p0);
        
        boolean divide(final double p0);
        
        boolean hasEnough(final double p0);
        
        boolean hasOver(final double p0);
        
        boolean hasUnder(final double p0);
        
        boolean isNegative();
        
        boolean remove();
        
        String toString();
    }
}
