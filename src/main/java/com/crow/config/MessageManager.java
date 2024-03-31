package com.crow.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageManager {

    private static final ConfigLoader messageConfig = ConfigLoader.getMessageConfig();
    private static YamlConfiguration config = messageConfig.getConfig();


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

    public static String PIGEON_ARRIVED;
    public static String CROW_ARRIVED;

    public static String DISABLE_LETTERS;
    public static String ENABLE_LETTERS;

    /**
     * Reloads messages from messages.yml
     *
     * @author Super
     */

    public static void reloadMessages() {

        PLUGIN_PREFIX = format(config.getString("plugin-prefix"));
        INVENTORY_FULL = PLUGIN_PREFIX + format(config.getString("inventory-full"));
        LETTER_CREATED = PLUGIN_PREFIX + format(config.getString("letter-created"));
        NOT_HOLDING_LETTER = PLUGIN_PREFIX + format(config.getString("not-holding-letter"));
        WRITTEN_BY = PLUGIN_PREFIX + format(config.getString("written-by"));
        INCORRECT_USAGE = PLUGIN_PREFIX + format(config.getString("incorrect-usage"));
        ADD_RECIEVER = PLUGIN_PREFIX +format(config.getString("add-reciever"));
        INVALID_LETTER = PLUGIN_PREFIX + format(config.getString("invalid-letter"));
        LETTER_SENT = PLUGIN_PREFIX + format(config.getString("letter-sent"));
        LETTER_RECIEVED = PLUGIN_PREFIX + format(config.getString("letter-recieved"));

        PIGEON_ARRIVED = PLUGIN_PREFIX + format(config.getString("pigeon-arrived"));
        CROW_ARRIVED = PLUGIN_PREFIX + format(config.getString("crow-arrived"));

        DISABLE_LETTERS = PLUGIN_PREFIX + format(config.getString("disable-letters"));
        ENABLE_LETTERS = PLUGIN_PREFIX + format(config.getString("enable-letters"));

    }


    /**
     *
     * @param msg mensagem a ser formatada
     * @return ChatColor
     * @author Super
     */

    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg.replace("\\n", "\n"));
    }

}
