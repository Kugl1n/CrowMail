package com.crow.config;

import com.crow.letter.OutgoingLetter;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


/**
 * Manages Letter data between outgoing.yml and ArrayLists.
 *
 * @author Super
 */
public class OutgoingManager {

    public static final ConfigLoader outgoingConfig = ConfigLoader.getOutgoingConfig();

    public static HashMap<UUID, ArrayList<ItemStack>> getSavedLetters() { return outgoing; }

    public static HashMap<UUID, ArrayList<ItemStack>> outgoing = new HashMap<>();

    /**
     * Gets all current outgoing letters on the HashMap and stores them im outgoing.yml
     *
     * @author Super
     */
    public static void saveLetters(){
        outgoing = OutgoingLetter.convertArrayToHash();

        outgoing.forEach((player, letter) -> {
        outgoingConfig.getConfig().set(String.valueOf(player), letter);

        outgoingConfig.saveConfigs();
        });

    }

    /**
     * Gets the stored outgoing letters in outgoing.yml and loads them into the HashMap.
     *
     * @author Super
     */
    public static void loadLetters(){
        for (String key : outgoingConfig.getConfig().getKeys(false)){

            outgoing.put(UUID.fromString(key), (ArrayList<ItemStack>) outgoingConfig.getConfig().getList(key));

        }

    }


}
