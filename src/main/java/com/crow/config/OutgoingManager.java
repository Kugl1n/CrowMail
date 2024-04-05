package com.crow.config;

import com.crow.letter.OutgoingLetter;
import org.bukkit.Bukkit;
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
     * Takes the currently saved letters and from the HashMap and creates an OutgoingLetter() for them
     * Made for onEnable()
     * For standard loading method:
     * loadLetters()
     * @author Super
     */
    public static void sendSavedLetters(){
        loadLetters();
        ArrayList<UUID> keys = new ArrayList<>();

        outgoing.forEach((player, letter) -> {
            for (ItemStack itemStack : letter) {

                new OutgoingLetter(Bukkit.getOfflinePlayer(player), itemStack, MainConfig.RESEND_DELAY);
            }
            keys.add(player);
        });

        for (UUID key : keys){
            outgoing.remove(key);
        }


    }

    /**
     * Gets all current outgoing letters on the HashMap and stores them im outgoing.yml
     *
     * @author Super
     */
    public static void saveLetters(){
        outgoing = OutgoingLetter.convertArrayToHash();

        for (String key : outgoingConfig.getConfig().getKeys(false)){
            outgoingConfig.getConfig().set(key, null);
        }

        if (!outgoing.isEmpty()) {

            outgoing.forEach((player, letter) -> {
            outgoingConfig.getConfig().set(String.valueOf(player), letter);

            outgoingConfig.saveConfigs();
            });
        }

        outgoingConfig.saveConfigs();
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
