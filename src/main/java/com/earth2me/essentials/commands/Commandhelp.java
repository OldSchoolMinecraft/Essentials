package com.earth2me.essentials.commands;

import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.constructor.*;
import org.bukkit.*;
import com.earth2me.essentials.*;
import org.bukkit.command.*;
import java.io.*;
import java.util.logging.*;
import org.bukkit.plugin.*;
import java.util.*;

public class Commandhelp extends EssentialsCommand
{
    private static final String DESCRIPTION = "description";
    private static final String PERMISSION = "permission";
    private static final String PERMISSIONS = "permissions";
    public final Yaml yaml;
    
    public Commandhelp() {
        super("help");
        this.yaml = new Yaml((BaseConstructor)new SafeConstructor());
    }
    
    @Override
    protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        int page = 1;
        String match = "";
        try {
            if (args.length > 0) {
                match = args[0].toLowerCase();
                page = Integer.parseInt(args[args.length - 1]);
                if (args.length == 1) {
                    match = "";
                }
            }
        }
        catch (Exception ex) {
            if (args.length == 1) {
                match = args[0].toLowerCase();
            }
        }
        final List<String> lines = this.getHelpLines(user, match);
        if (lines.isEmpty()) {
            throw new Exception(Util.i18n("noHelpFound"));
        }
        final int start = (page - 1) * 9;
        final int pages = lines.size() / 9 + ((lines.size() % 9 > 0) ? 1 : 0);
        user.sendMessage(Util.format("helpPages", page, pages));
        for (int i = start; i < lines.size() && i < start + 9; ++i) {
            user.sendMessage(lines.get(i));
        }
    }
    
    @Override
    protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        sender.sendMessage(Util.i18n("helpConsole"));
    }
    
    private List<String> getHelpLines(final User user, final String match) throws Exception {
        final List<String> retval = new ArrayList<String>();
        File helpFile = new File(this.ess.getDataFolder(), "help_" + Util.sanitizeFileName(user.getName()) + ".txt");
        if (!helpFile.exists()) {
            helpFile = new File(this.ess.getDataFolder(), "help_" + Util.sanitizeFileName(user.getGroup()) + ".txt");
        }
        if (!helpFile.exists()) {
            helpFile = new File(this.ess.getDataFolder(), "help.txt");
        }
        if (helpFile.exists()) {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(helpFile));
            try {
                while (bufferedReader.ready()) {
                    final String line = bufferedReader.readLine();
                    retval.add(line.replace('&', '§'));
                }
            }
            finally {
                bufferedReader.close();
            }
            return retval;
        }
        boolean reported = false;
        String pluginName = "";
        for (final Plugin p : this.ess.getServer().getPluginManager().getPlugins()) {
            try {
                final PluginDescriptionFile desc = p.getDescription();
                final HashMap<String, HashMap<String, Object>> cmds = (HashMap<String, HashMap<String, Object>>)desc.getCommands();
                pluginName = p.getDescription().getName().toLowerCase();
                for (final Map.Entry<String, HashMap<String, Object>> k : cmds.entrySet()) {
                    try {
                        if (!match.equalsIgnoreCase("") && !k.getKey().toLowerCase().contains(match) && (!(k.getValue().get("description") instanceof String) || !((String)k.getValue().get("description")).toLowerCase().contains(match)) && !pluginName.contains(match)) {
                            continue;
                        }
                        if (pluginName.contains("essentials")) {
                            final String node = "essentials." + k.getKey();
                            if (this.ess.getSettings().isCommandDisabled(k.getKey()) || !user.isAuthorized(node)) {
                                continue;
                            }
                            retval.add("§c" + k.getKey() + "§7: " + k.getValue().get("description"));
                        }
                        else {
                            if (!this.ess.getSettings().showNonEssCommandsInHelp()) {
                                continue;
                            }
                            final HashMap<String, Object> value = k.getValue();
                            if (value.containsKey("permission") && value.get("permission") instanceof String && !value.get("permission").equals("")) {
                                if (!user.isAuthorized((String)value.get("permission"))) {
                                    continue;
                                }
                                retval.add("§c" + k.getKey() + "§7: " + value.get("description"));
                            }
                            else if (value.containsKey("permission") && value.get("permission") instanceof List && !((String)value.get("permission")).isEmpty()) {
                                boolean enabled = false;
                                for (final Object o : (List)value.get("permission")) {
                                    if (o instanceof String && user.isAuthorized((String)o)) {
                                        enabled = true;
                                        break;
                                    }
                                }
                                if (!enabled) {
                                    continue;
                                }
                                retval.add("§c" + k.getKey() + "§7: " + value.get("description"));
                            }
                            else if (value.containsKey("permissions") && value.get("permissions") instanceof String && !value.get("permissions").equals("")) {
                                if (!user.isAuthorized((String)value.get("permissions"))) {
                                    continue;
                                }
                                retval.add("§c" + k.getKey() + "§7: " + value.get("description"));
                            }
                            else if (value.containsKey("permissions") && value.get("permissions") instanceof List && !((List)value.get("permissions")).isEmpty()) {
                                boolean enabled = false;
                                for (final Object o : (List)value.get("permissions")) {
                                    if (o instanceof String && user.isAuthorized((String)o)) {
                                        enabled = true;
                                        break;
                                    }
                                }
                                if (!enabled) {
                                    continue;
                                }
                                retval.add("§c" + k.getKey() + "§7: " + value.get("description"));
                            }
                            else if (user.isAuthorized("essentials.help." + pluginName)) {
                                retval.add("§c" + k.getKey() + "§7: " + value.get("description"));
                            }
                            else {
                                if (this.ess.getSettings().hidePermissionlessHelp()) {
                                    continue;
                                }
                                retval.add("§c" + k.getKey() + "§7: " + value.get("description"));
                            }
                        }
                    }
                    catch (NullPointerException ex2) {}
                }
            }
            catch (NullPointerException ex3) {}
            catch (Exception ex) {
                if (!reported) {
                    Commandhelp.logger.log(Level.WARNING, Util.format("commandHelpFailedForPlugin", pluginName), ex);
                }
                reported = true;
            }
        }
        return retval;
    }
}
