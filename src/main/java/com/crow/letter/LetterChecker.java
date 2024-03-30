package com.crow.letter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LetterChecker {

    public static boolean isValidLetter(ItemStack letter){

        return (letter != null && letter.getType() == Material.WRITTEN_BOOK && letter.getItemMeta().hasCustomModelData());

    }

    public static boolean isHoldingLetter(Player player) {
        return player.getInventory().getItemInMainHand() != null &&
               player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK &&
               player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData();
    }

    public static boolean wasSent(ItemStack letter){

        return letter.getItemMeta().getLore().toString().contains("§TDestinatario: ");

    }

}
