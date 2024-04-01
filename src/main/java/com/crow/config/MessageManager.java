package com.crow.config;

import org.bukkit.ChatColor;

public class MessageManager {


    private static final ConfigLoader config = ConfigLoader.getMessageConfig();


    public static String PLUGIN_PREFIX;
    public static String INVENTORY_FULL;
    public static String LETTER_CREATED;
    public static String NOT_HOLDING_LETTER;
    public static String WRITTEN_BY;
    public static String INCORRECT_USAGE;
    public static String ADD_RECIEVER;
    public static String INVALID_LETTER;
    public static String LETTER_SENT;
    public static String LETTER_RECIEVED;
    public static String SHRED_LETTER;
    public static String SHRED_ALL_LETTERS;
    public static String PIGEON_ARRIVED;
    public static String CROW_ARRIVED;

    public static String DISABLE_LETTERS;
    public static String ENABLE_LETTERS;
    public static String IN_BLOCKED_GAMEMODE;
    public static String IN_BLOCKED_MODE;
    public static String IN_BLOCKED_WORLD;
    public static String RELOAD_SUCCESS;

    public static String ERROR_NO_PERMISSION;



    /**
     * Reloads messages from messages.yml
     *
     * @author Super
     */

    public static void reloadMessages() {

        PLUGIN_PREFIX = format(config.getConfig().getString("plugin-prefix"));
        INVENTORY_FULL = PLUGIN_PREFIX + format(config.getConfig().getString("inventory-full"));
        LETTER_CREATED = PLUGIN_PREFIX + format(config.getConfig().getString("letter-created"));
        NOT_HOLDING_LETTER = PLUGIN_PREFIX + format(config.getConfig().getString("not-holding-letter"));
        WRITTEN_BY = PLUGIN_PREFIX + format(config.getConfig().getString("written-by"));
        INCORRECT_USAGE = PLUGIN_PREFIX + format(config.getConfig().getString("incorrect-usage"));
        ADD_RECIEVER = PLUGIN_PREFIX +format(config.getConfig().getString("add-reciever"));
        INVALID_LETTER = PLUGIN_PREFIX + format(config.getConfig().getString("invalid-letter"));
        LETTER_SENT = PLUGIN_PREFIX + format(config.getConfig().getString("letter-sent"));
        LETTER_RECIEVED = PLUGIN_PREFIX + format(config.getConfig().getString("letter-recieved"));
        SHRED_LETTER = PLUGIN_PREFIX + format(config.getConfig().getString("shred-letter"));
        SHRED_ALL_LETTERS = PLUGIN_PREFIX + format(config.getConfig().getString("shred-all-letters"));
        PIGEON_ARRIVED = PLUGIN_PREFIX + format(config.getConfig().getString("pigeon-arrived"));
        CROW_ARRIVED = PLUGIN_PREFIX + format(config.getConfig().getString("crow-arrived"));

        DISABLE_LETTERS = PLUGIN_PREFIX + format(config.getConfig().getString("disable-letters"));
        ENABLE_LETTERS = PLUGIN_PREFIX + format(config.getConfig().getString("enable-letters"));
        IN_BLOCKED_GAMEMODE = PLUGIN_PREFIX + format(config.getConfig().getString("in-blocked-gamemode"));
        IN_BLOCKED_MODE = PLUGIN_PREFIX + format(config.getConfig().getString("in-blocked-mode"));
        IN_BLOCKED_WORLD = PLUGIN_PREFIX + format(config.getConfig().getString("in-blocked-world"));
        RELOAD_SUCCESS = PLUGIN_PREFIX + format(config.getConfig().getString("reload-success"));

        ERROR_NO_PERMISSION = PLUGIN_PREFIX + format(config.getConfig().getString("error-no-permission"));

    }


    /**
     * Standard message formatter
     * @param msg Message to be formatted
     * @return String with translated ChatColors
     * @author Super
     */
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg.replace("\\n", "\n"));
    }

}
