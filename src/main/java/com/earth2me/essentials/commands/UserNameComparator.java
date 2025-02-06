package com.earth2me.essentials.commands;

import java.util.*;
import com.earth2me.essentials.*;

class UserNameComparator implements Comparator<User>
{
    @Override
    public int compare(final User a, final User b) {
        return a.getName().compareTo(b.getName());
    }
}
