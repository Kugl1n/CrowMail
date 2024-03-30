package com.crow.letter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.crow.CrowMail;
import com.crow.config.ConfigLoader;
import com.crow.crow.Crow;

import net.md_5.bungee.api.ChatColor;

public class OutgoingLetter {

    public static HashMap<UUID, ArrayList<ItemStack>> outgoingLetters = new HashMap<>();
    public static List<Player> bloquedPlayers = new ArrayList<>();

    public static void newOutgoingLetter(OfflinePlayer player, ItemStack letter) {

        UUID uuid = player.getUniqueId();

        // Add destination player to lore
        ItemMeta itemMeta = letter.getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.add(ChatColor.DARK_GRAY +"§TDestinatario: " + player.getName());
        
        itemMeta.setLore(lore);
        letter.setItemMeta(itemMeta);

        
        // Adds the letter to the outgoing letters
        if (!outgoingLetters.containsKey(uuid)){
            outgoingLetters.put(uuid, new ArrayList<>());
        }
        outgoingLetters.get(uuid).add(new ItemStack(letter));

        // If the player is online, send the letter
        if (player.isOnline() && (outgoingLetters.get(uuid).size()==1) && ((Player) player).getGameMode() != GameMode.SPECTATOR) {

            if (letter.getItemMeta().getCustomModelData() == 101)
                send( (Player) player, true);
            else
                send( (Player) player, false);
        }

    }

    public static void send(Player player, boolean anonimous) {

        new BukkitRunnable() {
            @Override
            public void run() {

                Crow crow = new Crow();
                crow.spawn((Player) player, anonimous);
            
            }

        }.runTaskLater(CrowMail.getInstance(), ConfigLoader.RECEIVE_DELAY);

    }

}
