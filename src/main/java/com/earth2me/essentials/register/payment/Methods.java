package com.earth2me.essentials.register.payment;

import com.earth2me.essentials.register.payment.methods.*;
import java.util.*;
import org.bukkit.plugin.*;

public class Methods
{
    private boolean self;
    private Method Method;
    private String preferred;
    private Set<Method> Methods;
    private Set<String> Dependencies;
    private Set<Method> Attachables;
    
    public Methods() {
        this.self = false;
        this.Method = null;
        this.preferred = "";
        this.Methods = new HashSet<Method>();
        this.Dependencies = new HashSet<String>();
        this.Attachables = new HashSet<Method>();
        this._init();
    }
    
    public Methods(final String preferred) {
        this.self = false;
        this.Method = null;
        this.preferred = "";
        this.Methods = new HashSet<Method>();
        this.Dependencies = new HashSet<String>();
        this.Attachables = new HashSet<Method>();
        this._init();
        if (this.Dependencies.contains(preferred)) {
            this.preferred = preferred;
        }
    }
    
    private void _init() {
        this.addMethod("iConomy", new iCo5());
        this.addMethod("BOSEconomy", new BOSE6());
        this.addMethod("BOSEconomy", new BOSE7());
    }
    
    public Set<String> getDependencies() {
        return this.Dependencies;
    }
    
    public Method createMethod(final Plugin plugin) {
        for (final Method method : this.Methods) {
            if (method.isCompatible(plugin)) {
                method.setPlugin(plugin);
                return method;
            }
        }
        return null;
    }
    
    private void addMethod(final String name, final Method method) {
        this.Dependencies.add(name);
        this.Methods.add(method);
    }
    
    public boolean hasMethod() {
        return this.Method != null;
    }
    
    public boolean setMethod(final Plugin method) {
        if (this.hasMethod()) {
            return true;
        }
        if (this.self) {
            return this.self = false;
        }
        int count = 0;
        boolean match = false;
        Plugin plugin = null;
        final PluginManager manager = method.getServer().getPluginManager();
        for (final String name : this.getDependencies()) {
            if (this.hasMethod()) {
                break;
            }
            if (method.getDescription().getName().equals(name)) {
                plugin = method;
            }
            else {
                plugin = manager.getPlugin(name);
            }
            if (plugin == null) {
                continue;
            }
            final Method current = this.createMethod(plugin);
            if (current == null) {
                continue;
            }
            if (this.preferred.isEmpty()) {
                this.Method = current;
            }
            else {
                this.Attachables.add(current);
            }
        }
        if (!this.preferred.isEmpty()) {
            do {
                if (this.hasMethod()) {
                    match = true;
                }
                else {
                    for (final Method attached : this.Attachables) {
                        if (attached == null) {
                            continue;
                        }
                        if (this.hasMethod()) {
                            match = true;
                            break;
                        }
                        if (this.preferred.isEmpty()) {
                            this.Method = attached;
                        }
                        if (count == 0) {
                            if (!this.preferred.equalsIgnoreCase(attached.getName())) {
                                continue;
                            }
                            this.Method = attached;
                        }
                        else {
                            this.Method = attached;
                        }
                    }
                    ++count;
                }
            } while (!match);
        }
        return this.hasMethod();
    }
    
    public Method getMethod() {
        return this.Method;
    }
    
    public boolean checkDisabled(final Plugin method) {
        if (!this.hasMethod()) {
            return true;
        }
        if (this.Method.isCompatible(method)) {
            this.Method = null;
        }
        return this.Method == null;
    }
}
