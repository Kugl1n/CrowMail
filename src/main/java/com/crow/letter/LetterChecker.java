package com.crow.letter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class LetterChecker {

    public static boolean isValidLetter(ItemStack letter){

        return (letter != null && letter.getType() == Material.WRITTEN_BOOK && letter.getItemMeta().hasCustomModelData());

    }

    /**
     * Used to check if a player is holding a letter
     * @param player Command sender
     * @return {@code boolean} item in main hand is a letter
     */
    public static boolean isHoldingLetter(Player player) {
        return player.getInventory().getItemInMainHand() != null &&
               player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK &&
               player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData();
    }

    /**
     * Used to check if a player is holding a letter written by them
     * @param player Command sender
     * @return {@code boolean} item in main hand is a letter written by @player
     */
    public static boolean isHoldingOwnLetter(Player player) {
        return player.getInventory().getItemInMainHand() != null &&
                player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK &&
                player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData() &&
                Objects.equals(LetterCreator.getLetterOwner(player), player.getName());
    }

    public static boolean wasSent(ItemStack letter){

        return letter.getItemMeta().getLore().toString().contains("§TDestinatario: ");

    }

}
