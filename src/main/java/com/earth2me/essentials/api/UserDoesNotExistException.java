package com.earth2me.essentials.api;

import com.earth2me.essentials.*;

public class UserDoesNotExistException extends Exception
{
    public UserDoesNotExistException(final String name) {
        super(Util.format("userDoesNotExist", name));
    }
}
