package com.earth2me.essentials.event;

import org.bukkit.event.Event;

public class EcoInflateEvent extends Event
{
    private String username;
    private double amount;

    public EcoInflateEvent(String username, double amount)
    {
        super(Type.CUSTOM_EVENT);
        this.username = username;
        this.amount = amount;
    }

    public String getUsername()
    {
        return username;
    }

    public double getAmount()
    {
        return amount;
    }
}
