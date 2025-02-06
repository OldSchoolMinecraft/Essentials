package com.earth2me.essentials.commands;

public class NotEnoughArgumentsException extends Exception
{
    public NotEnoughArgumentsException() {
    }
    
    public NotEnoughArgumentsException(final Throwable ex) {
        super(ex);
    }
}
