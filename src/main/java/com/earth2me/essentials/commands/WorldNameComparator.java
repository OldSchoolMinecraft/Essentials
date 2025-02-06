package com.earth2me.essentials.commands;

import java.util.*;
import org.bukkit.*;

class WorldNameComparator implements Comparator<World>
{
    @Override
    public int compare(final World a, final World b) {
        return a.getName().compareTo(b.getName());
    }
}
