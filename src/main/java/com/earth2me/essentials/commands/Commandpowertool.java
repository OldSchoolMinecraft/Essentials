package com.earth2me.essentials.commands;

import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.inventory.*;
import java.util.*;

public class Commandpowertool extends EssentialsCommand
{
    public Commandpowertool() {
        super("powertool");
    }
    
    @Override
    protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        final ItemStack itemStack = user.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            throw new Exception(Util.i18n("powerToolAir"));
        }
        final String itemName = itemStack.getType().toString().toLowerCase().replaceAll("_", " ");
        String command = EssentialsCommand.getFinalArg(args, 0);
        List<String> powertools = user.getPowertool(itemStack);
        Label_0437: {
            if (command != null && !command.isEmpty()) {
                if (command.equalsIgnoreCase("l:")) {
                    if (powertools == null || powertools.isEmpty()) {
                        throw new Exception(Util.format("powerToolListEmpty", itemName));
                    }
                    user.sendMessage(Util.format("powerToolList", Util.joinList(powertools), itemName));
                    return;
                }
                else {
                    if (command.startsWith("r:")) {
                        try {
                            command = command.substring(2);
                            if (!powertools.contains(command)) {
                                throw new Exception(Util.format("powerToolNoSuchCommandAssigned", command, itemName));
                            }
                            powertools.remove(command);
                            user.sendMessage(Util.format("powerToolRemove", command, itemName));
                            break Label_0437;
                        }
                        catch (Exception e) {
                            user.sendMessage(e.getMessage());
                            return;
                        }
                    }
                    if (command.startsWith("a:")) {
                        command = command.substring(2);
                        if (powertools.contains(command)) {
                            throw new Exception(Util.format("powerToolAlreadySet", command, itemName));
                        }
                    }
                    else if (powertools != null && !powertools.isEmpty()) {
                        powertools.clear();
                    }
                    else {
                        powertools = new ArrayList<String>();
                    }
                    powertools.add(command);
                    user.sendMessage(Util.format("powerToolAttach", Util.joinList(powertools), itemName));
                }
            }
            else {
                if (powertools != null) {
                    powertools.clear();
                }
                user.sendMessage(Util.format("powerToolRemoveAll", itemName));
            }
        }
        user.setPowertool(itemStack, powertools);
    }
}
