package com.crow.letter;

import java.util.*;

import com.crow.config.MainConfig;

import com.crow.config.MessageManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.crow.CrowMail;
import com.crow.crow.Crow;
import net.md_5.bungee.api.ChatColor;

public class OutgoingLetter {



    public static ArrayList<OutgoingLetter> getOutgoingLetters() { return outgoingLetters ; }

    public static ArrayList<OutgoingLetter> outgoingLetters = new ArrayList<>();
    public static List<Player> blockedPlayers = new ArrayList<>();

    private OfflinePlayer player;
    private ItemStack letter;
    private boolean anonimous;
    private double distance;
    private UUID playerUUID;
    private Crow crow;
    private boolean isDelivered;

    public OutgoingLetter getOutgoingLetter() { return outgoingLetter; }

    private OutgoingLetter outgoingLetter;


    public static HashMap<UUID, ArrayList<ItemStack>> convertArrayToHash() {

        HashMap<UUID, ArrayList<ItemStack>> hashMap = new HashMap<>();
        for (OutgoingLetter outLetter : outgoingLetters) {

            if (hashMap.get(outLetter.getPlayerUUID()) == null)
                hashMap.put(outLetter.getPlayerUUID(), new ArrayList<>());
            hashMap.get(outLetter.getPlayerUUID()).add(outLetter.getLetter());
        }

        return hashMap;
    }

    public OutgoingLetter(OfflinePlayer player, ItemStack letter, double distance) {

        outgoingLetter = this;

        this.player = player;
        this.letter = letter;
        this.distance = distance;
        this.crow = null;
        this.isDelivered = false;
        this.playerUUID = player.getUniqueId();

        // Add destination player to lore
        ItemMeta itemMeta = this.letter.getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.add(ChatColor.DARK_GRAY +"§TDestinatario: " + player.getName());
        
        if (itemMeta.getCustomModelData() == MainConfig.CEM_ANONIMOUS_LETTER)
            anonimous = true;

        itemMeta.setLore(lore);
        this.letter.setItemMeta(itemMeta);

        outgoingLetters.add(this);

        if (player.isOnline())
            firstSend();

    }

    public void firstSend(){
        send(((int)(distance)) * MainConfig.DISTANCE_MODIFIER);
    }

    public void send(int time) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (OutgoingLetter.blockedPlayers.contains(((Player) player))) {
                    ((Player) player).sendMessage(MessageManager.IN_BLOCKED_MODE);
                    return;
                } else if (MainConfig.BLOCKED_WORLDS.contains(((Player) player).getWorld())) {
                    ((Player) player).sendMessage(MessageManager.IN_BLOCKED_WORLD);
                    return;
                } else if (MainConfig.BLOCKED_GAMEMODES.contains(((Player) player).getGameMode())){
                    ((Player) player).sendMessage(MessageManager.IN_BLOCKED_GAMEMODE);
                    return;
                }


                crow = new Crow();
                crow.spawn((Player) player, anonimous);
                despawnCrow();
            }

        }.runTaskLater(CrowMail.getInstance(), time);

    }

    public void despawnCrow() {

        new BukkitRunnable() {
            @Override
            public void run() {

                removeCrow();

                if (player.isOnline() && !isDelivered){
                    send(MainConfig.RESEND_DELAY);
                }
            }

        }.runTaskLater(CrowMail.getInstance(), MainConfig.DESPAWN_DELAY);

    }

    public void removeCrow(){

        crow.remove();
        crow = null;

    }

    public UUID getPlayerUUID(){
        return playerUUID;
    }

    public Crow getCrow() {
        return crow;
    }

    public ItemStack getLetter() {
        return letter;
    }

    public void setDelivered(){
        isDelivered = true;
    }

    public void updatePlayer(Player newPlayer){
        player = newPlayer;
    }

    public static OutgoingLetter isPlayerIn(Player player){

        UUID otherPlayerUUID = player.getUniqueId();

        for (OutgoingLetter outgoingLetter : outgoingLetters) {

            if (outgoingLetter.getPlayerUUID().equals(otherPlayerUUID)){
                return outgoingLetter;
            }      
        }
        return null;
    }

    public static OutgoingLetter isCrowIn(Entity crow) {

        for (OutgoingLetter outgoingLetter : outgoingLetters) {
            if (outgoingLetter.getCrow().getCrowEntity() == crow)
                return outgoingLetter;
        }

        return null;
    }


}
