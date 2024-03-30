package com.crow.letter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.crow.config.ConfigLoader;

public class LetterCreator {

    public static ItemStack createLetter(String player, String[] content, boolean anonimous) {

        // Creates the Item and Book meta
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bm = (BookMeta) book.getItemMeta();

        // Set Author and Letter Tittle (Anonimous or not)
        if (anonimous) {
            bm.setAuthor("Anônimo");
            bm.setTitle("Carta Anônima");

            // Custom Model Data from the letter
            bm.setCustomModelData(ConfigLoader.CEM_ANONIMOUS_LETTER);

        }
        else {
            bm.setAuthor(player);
            bm.setTitle("Carta de " + player);

            // Custom Model Data from the letter
            bm.setCustomModelData(ConfigLoader.CEM_LETTER);

        }

        // Converts Agrs to the String
        StringBuilder sb = new StringBuilder();

        for (String arg : content) {
            sb.append(arg).append(" ");
        }
        String msg = sb.toString();

        // Set the Book Pages
        ArrayList<String> pages = new ArrayList<>();
        pages.add(msg);
        bm.setPages(pages);

        // Set Some of the text on the item lore
        ArrayList<String> lore = splitText(msg, 30, 3);

        // Set the actual date to the item lore
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
        String dateNow = formatter.format(currentDate.getTime());

        lore.add(ChatColor.DARK_GRAY + dateNow);

        // Set the lore and item meta to the Book
        bm.setLore(lore);
        book.setItemMeta(bm);

        return book;

    }

    public static ArrayList<String> splitText(String text, int size, int maxSize) {

        ArrayList<String> stringList = new ArrayList<>();

        maxSize = maxSize*size;
        if (text.length() < maxSize) {
            maxSize = text.length();
        } 

        for (int i = 0; i < maxSize; i += size) {
            int end = Math.min(i + size, maxSize);
            stringList.add(text.substring(i, end));
        }

        return stringList;
    }

}
