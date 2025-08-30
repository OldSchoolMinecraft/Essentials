package com.earth2me.essentials.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import java.io.*;
import com.earth2me.essentials.*;
import java.util.*;

public class Commandinfo extends EssentialsCommand
{
    public Commandinfo() {
        super("info");
    }
    
    @Override
    public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception {
        final String pageStr = (args.length > 0) ? args[0].trim() : null;
        final List<String> lines = new ArrayList<String>();
        final List<String> chapters = new ArrayList<String>();
        final Map<String, Integer> bookmarks = new HashMap<String, Integer>();
        File file = null;
        if (sender instanceof Player) {
            final User user = this.ess.getUser(sender);
            file = new File(this.ess.getDataFolder(), "info_" + Util.sanitizeFileName(user.getName()) + ".txt");
            if (!file.exists()) {
                file = new File(this.ess.getDataFolder(), "info_" + Util.sanitizeFileName(user.getGroup()) + ".txt");
            }
        }
        if (file == null || !file.exists()) {
            file = new File(this.ess.getDataFolder(), "info.txt");
        }
        if (!file.exists()) {
            file.createNewFile();
            throw new Exception(Util.i18n("infoFileDoesNotExist"));
        }
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        try {
            int lineNumber = 0;
            while (bufferedReader.ready()) {
                final String line = bufferedReader.readLine();
                if (line.length() > 0 && line.charAt(0) == '#') {
                    bookmarks.put(line.substring(1).toLowerCase().replaceAll("&[0-9a-f]", ""), lineNumber);
                    chapters.add(line.substring(1).replace('&', '§'));
                }
                lines.add(line.replace('&', '§'));
                ++lineNumber;
            }
        }
        finally {
            bufferedReader.close();
        }
        if (bookmarks.isEmpty()) {
            int page = 1;
            try {
                page = Integer.parseInt(pageStr);
            }
            catch (Exception ex) {
                page = 1;
            }
            final int start = (page - 1) * 9;
            final int pages = lines.size() / 9 + ((lines.size() % 9 > 0) ? 1 : 0);
            sender.sendMessage(Util.format("infoPages", page, pages));
            for (int i = start; i < lines.size() && i < start + 9; ++i) {
                sender.sendMessage((String)lines.get(i));
            }
            return;
        }
        if (pageStr == null || pageStr.isEmpty() || pageStr.matches("[0-9]+")) {
            if (lines.get(0).startsWith("#")) {
                sender.sendMessage(Util.i18n("infoChapter"));
                final StringBuilder sb = new StringBuilder();
                boolean first = true;
                for (final String string : chapters) {
                    if (!first) {
                        sb.append(", ");
                    }
                    first = false;
                    sb.append(string);
                }
                sender.sendMessage(sb.toString());
                return;
            }
            int page = 1;
            try {
                page = Integer.parseInt(pageStr);
            }
            catch (Exception ex) {
                page = 1;
            }
            final int start = (page - 1) * 9;
            int end;
            for (end = 0; end < lines.size(); ++end) {
                final String line2 = lines.get(end);
                if (line2.startsWith("#")) {
                    break;
                }
            }
            final int pages2 = end / 9 + ((end % 9 > 0) ? 1 : 0);
            sender.sendMessage(Util.format("infoPages", page, pages2));
            for (int j = start; j < end && j < start + 9; ++j) {
                sender.sendMessage((String)lines.get(j));
            }
        }
        else {
            int chapterpage = 0;
            if (args.length >= 2) {
                try {
                    chapterpage = Integer.parseInt(args[1]) - 1;
                }
                catch (Exception ex) {
                    chapterpage = 0;
                }
            }
            if (!bookmarks.containsKey(pageStr.toLowerCase())) {
                sender.sendMessage(Util.i18n("infoUnknownChapter"));
                return;
            }
            int chapterend;
            int chapterstart;
            for (chapterstart = (chapterend = bookmarks.get(pageStr.toLowerCase()) + 1); chapterend < lines.size(); ++chapterend) {
                final String line2 = lines.get(chapterend);
                if (line2.startsWith("#")) {
                    break;
                }
            }
            final int start2 = chapterstart + chapterpage * 9;
            final int page2 = chapterpage + 1;
            final int pages3 = (chapterend - chapterstart) / 9 + (((chapterend - chapterstart) % 9 > 0) ? 1 : 0);
            sender.sendMessage(Util.format("infoChapterPages", pageStr, page2, pages3));
            for (int k = start2; k < chapterend && k < start2 + 9; ++k) {
                sender.sendMessage((String)lines.get(k));
            }
        }
    }
}
