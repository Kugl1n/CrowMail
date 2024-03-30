package com.crow.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;

import com.crow.CrowMail;

public class ConfigLoader {

    public static int RECEIVE_DELAY;
    public static int DESPAWN_TIME;

    public static int CEM_LETTER;
    public static int CEM_ANONIMOUS_LETTER;

    public static List<GameMode> BLOCKED_GAMEMODES;
    public static void loadConfigs() {

        CrowMail plugin = CrowMail.getInstance();

        RECEIVE_DELAY = plugin.getConfig().getInt("receive-delay");
        DESPAWN_TIME = plugin.getConfig().getInt("despawn-time");

        CEM_LETTER = plugin.getConfig().getInt("cem-letter");
        CEM_ANONIMOUS_LETTER = plugin.getConfig().getInt("cem-anonimous-letter");

        BLOCKED_GAMEMODES = new ArrayList<GameMode>();

        List<String> modeStrings = plugin.getConfig().getStringList("blocked-gamemodes");
        if (modeStrings.isEmpty()) {
            return;
        }

        for (String mode : modeStrings) {
            BLOCKED_GAMEMODES.add(GameMode.valueOf(mode));
        }
    }
 
}
