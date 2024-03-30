package com.crow.letter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LetterChecker {

    public static boolean isValidLetter(ItemStack letter){

        return (letter != null && letter.getType() == Material.WRITTEN_BOOK && letter.getItemMeta().hasCustomModelData());

    }

    public static boolean isSend(ItemStack letter){

        return letter.getItemMeta().getLore().toString().contains("§TDestinatario: ");

    }

}
