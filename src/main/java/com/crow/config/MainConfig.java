package com.crow.config;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Parrot;

import java.util.ArrayList;
import java.util.List;

/**
 * Where all the values for config.yml are stored and loaded
 *
 * @author Super
 */

public class MainConfig {

    public static int CEM_LETTER;
    public static int CEM_ANONIMOUS_LETTER;


    public static Parrot.Variant PIGEON_VARIANT;
    public static Parrot.Variant CROW_VARIANT;

    public static List<GameMode> BLOCKED_GAMEMODES;

    public static int DISTANCE_MODIFIER;
    public static int DESPAWN_DELAY;
    public static int RESEND_DELAY;
    public static int ON_JOIN_DELAY;
    public static int ON_GAMEMODE_DELAY;
    public static int ON_ENABLE_LETTERS_DELAY;
    public static int DIFFERENT_DIMENSION_DELAY;

    /**
     * Loads options from the config.yml file
     *
     * @author Kuglin
     */
    public static void loadConfigs() {

        YamlConfiguration config = ConfigLoader.getMainConfig().getConfig();

        CEM_LETTER = config.getInt("cem-letter");
        CEM_ANONIMOUS_LETTER = config.getInt("cem-anonimous-letter");

        PIGEON_VARIANT = Parrot.Variant.valueOf(config.getString("pigeon-variant"));
        CROW_VARIANT = Parrot.Variant.valueOf(config.getString("crow-variant"));


        BLOCKED_GAMEMODES = new ArrayList<GameMode>();

        DISTANCE_MODIFIER = config.getInt("receive-delay");
        DESPAWN_DELAY = config.getInt("despawn-delay");
        RESEND_DELAY = config.getInt("resend-delay");
        DIFFERENT_DIMENSION_DELAY = config.getInt("different-dimension-delay");

        ON_JOIN_DELAY = config.getInt("on-join-delay");
        ON_GAMEMODE_DELAY = config.getInt("on-gamemode-delay");
        ON_ENABLE_LETTERS_DELAY = config.getInt("on-enable-letters-delay");

        List<String> modeStrings = config.getStringList("blocked-gamemodes");
        if (modeStrings.isEmpty()) {
            return;
        }

        for (String mode : modeStrings) {
            BLOCKED_GAMEMODES.add(GameMode.valueOf(mode));
        }


    }
}
