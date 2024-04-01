package com.crow.events;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> results = new ArrayList<>();


        if (sender instanceof Player) {
            if (args.length == 1) {
                switch (label.toLowerCase()){
                    case "crowmail":
                        return StringUtil.copyPartialMatches(args[0], Arrays.asList("reload"), new ArrayList<>());

                    case "rasgar":
                        return StringUtil.copyPartialMatches(args[0], Arrays.asList("todas"), new ArrayList<>());

                    case "enviar":
                        List<String> names = new ArrayList<>();

                        for (Player player : Bukkit.getOnlinePlayers()){
                            names.add(player.getName());
                        }
                        return StringUtil.copyPartialMatches(args[0], names, new ArrayList<>());

                }
            }
        }

        return results;
    }
}
