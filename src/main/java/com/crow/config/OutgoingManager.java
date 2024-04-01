package com.crow.config;

import com.crow.letter.OutgoingLetter;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class OutgoingManager {

    public static final ConfigLoader outgoingConfig = ConfigLoader.getOutgoingConfig();

    public static ArrayList<OutgoingLetter> outgoing = OutgoingLetter.getOutgoingLetters();

    public static HashMap<UUID, ArrayList<ItemStack>> convertedOutgoing = new HashMap<>();

    public static void saveLetters(){
            convertedOutgoing = OutgoingLetter.converArrayToHash();

            convertedOutgoing.forEach((player, letter) -> {
            outgoingConfig.getConfig().set(String.valueOf(player), letter);

            outgoingConfig.saveConfigs();
            });

        }
    }
