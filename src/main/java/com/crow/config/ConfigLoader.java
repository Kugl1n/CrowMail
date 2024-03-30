package com.crow.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.entity.Parrot;

import com.crow.CrowMail;

public class ConfigLoader {

    public static int RECEIVE_DELAY;
    public static int DESPAWN_TIME;

    public static int CEM_LETTER;
    public static int CEM_ANONIMOUS_LETTER;

    public static String MESSAGE_PLUGIN_PREFIX;
    public static String MESSAGE_INVENTORY_FULL;
    public static String MESSAGE_LETTER_CREATED;
    public static String MESSAGE_DONT_HOLDING_LETTER;
    public static String MESSAGE_WRITTEN_BY;
    public static String MESSAGE_INCORRECT_USAGE;
    public static String MESSAGE_ADD_RECIEVER;
    public static String MESSAGE_INVALID_LETTER;
    public static String MESSAGE_LETTER_SEND;
    public static String MESSAGE_LETTER_RECIEVED;

    public static String MESSAGE_PIGEON_ARRIVED;
    public static String MESSAGE_CROW_ARRIVED;

    public static String MESSAGE_DISABLE_LETTERS;
    public static String MESSAGE_ENABLE_LETTERS;

    public static Parrot.Variant PIGEON_VARIANT;
    public static Parrot.Variant CROW_VARIANT;

    public static List<GameMode> BLOCKED_GAMEMODES;
    



    public static void loadConfigs() {

        CrowMail plugin = CrowMail.getInstance();

        RECEIVE_DELAY = plugin.getConfig().getInt("receive-delay");
        DESPAWN_TIME = plugin.getConfig().getInt("despawn-time");

        CEM_LETTER = plugin.getConfig().getInt("cem-letter");
        CEM_ANONIMOUS_LETTER = plugin.getConfig().getInt("cem-anonimous-letter");

        MESSAGE_PLUGIN_PREFIX = plugin.getConfig().getString("message-plugin-prefix");
        MESSAGE_INVENTORY_FULL = plugin.getConfig().getString("message-inventory-full");
        MESSAGE_LETTER_CREATED = plugin.getConfig().getString("message-letter-created");
        MESSAGE_DONT_HOLDING_LETTER = plugin.getConfig().getString("message-dont-holding-letter");
        MESSAGE_WRITTEN_BY = plugin.getConfig().getString("message-written-by");
        MESSAGE_INCORRECT_USAGE = plugin.getConfig().getString("message-incorrect-usage");
        MESSAGE_ADD_RECIEVER = plugin.getConfig().getString("message-add-reciever");
        MESSAGE_INVALID_LETTER = plugin.getConfig().getString("message-invalid-letter");
        MESSAGE_LETTER_SEND = plugin.getConfig().getString("message-letter-send");
        MESSAGE_LETTER_RECIEVED = plugin.getConfig().getString("message-letter-recieved"); 

        MESSAGE_PIGEON_ARRIVED = plugin.getConfig().getString("message-pigeon-arrived");
        MESSAGE_CROW_ARRIVED = plugin.getConfig().getString("message-crow-arrived");

        MESSAGE_DISABLE_LETTERS = plugin.getConfig().getString("message-disable-letters");
        MESSAGE_ENABLE_LETTERS = plugin.getConfig().getString("message-enable-letters");

        PIGEON_VARIANT = Parrot.Variant.valueOf(plugin.getConfig().getString("pigeon-variant"));
        CROW_VARIANT = Parrot.Variant.valueOf(plugin.getConfig().getString("crow-variant"));


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
