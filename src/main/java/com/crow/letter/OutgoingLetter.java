package com.crow.letter;

import java.util.*;

import com.crow.config.MainConfig;

import com.crow.config.MessageManager;
import com.crow.config.OutgoingManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.crow.CrowMail;
import com.crow.crow.Crow;
import net.md_5.bungee.api.ChatColor;

/**
 * Contains all methods related to letter sending, and outgoing queue.
 *
 * @author Kuglin
 */
public class OutgoingLetter {

    public static ArrayList<OutgoingLetter> getOutgoingLetters() { return outgoingLetters ; }

    public static ArrayList<OutgoingLetter> outgoingLetters = new ArrayList<>();
    private OutgoingLetter outgoingLetter;

    public static List<Player> blockedPlayers = new ArrayList<>();

    private OfflinePlayer player;
    private ItemStack letter;
    private boolean anonimous;
    private double distance;
    private UUID playerUUID;
    private Crow crow;
    private boolean isDelivered;

    public OutgoingLetter getOutgoingLetter() { return outgoingLetter; }

    /**
     * Takes the currently saved letters and from the HashMap and loads them into the ArrayList.
     * Made for onEnable()
     * For standard loading method:
     * @see OutgoingManager OutgoinManager.loadLetters()
     * @author Super
     */
    public static void convertSavedLetters(){
        HashMap<UUID, ArrayList<ItemStack>> savedLetters = OutgoingManager.getSavedLetters();

        savedLetters.forEach((player, letter) -> {
            for (ItemStack itemStack : letter) {

                new OutgoingLetter(Bukkit.getOfflinePlayer(player), itemStack, MainConfig.RESEND_DELAY);

            }
        });

    }

    /**
     * Takes ArrayList outgoingLetters content and converts it into a HashMap
     *
     * @return Converted outgoingLetters HashMap
     * @author Kuglin
     */
    public static HashMap<UUID, ArrayList<ItemStack>> convertArrayToHash() {

        HashMap<UUID, ArrayList<ItemStack>> hashMap = new HashMap<>();
        for (OutgoingLetter outLetter : outgoingLetters) {

            if (hashMap.get(outLetter.getPlayerUUID()) == null)
                hashMap.put(outLetter.getPlayerUUID(), new ArrayList<>());
            hashMap.get(outLetter.getPlayerUUID()).add(outLetter.getLetter());
        }

        return hashMap;
    }

    /**
     * Creates a letter and stores it on the outgoing list
     *
     * @param player OfflinePlayer Receiver
     * @param letter ItemStack Letter to be sent
     * @param distance double Distance between Sender and Receiver
     * @author Kuglin
     */
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

        if (!outgoingLetters.contains(this)) outgoingLetters.add(this);

        if (player.isOnline()) firstSend();

    }

    public void firstSend(){
        send(distance * MainConfig.DISTANCE_MODIFIER);
    }

    /**
     * Sends out an OutgoingLetter
     *
     * @param time double Ticks before sending
     * @author Kuglin
     */
    public void send(double time) {

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

        }.runTaskLater(CrowMail.getInstance(), MainConfig.MINIMUM_SEND_TIME + (int) time);

    }

    /**
     * Removes a crow Entity, and resends it if a letter wasn't received
     *
     * @author Kuglin
     */
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

    /**
     * Destroys a crow Entity
     *
     * @author Kuglin
     */
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

    public static ArrayList<OutgoingLetter> isPlayerIn(Player player){

        ArrayList<OutgoingLetter> outgoingLettersForPlayer = new ArrayList<>();

        UUID otherPlayerUUID = player.getUniqueId();

        for (OutgoingLetter outgoingLetter : outgoingLetters) {

            if (outgoingLetter.getPlayerUUID().equals(otherPlayerUUID)){
                outgoingLettersForPlayer.add(outgoingLetter);
            }      
        }
        return outgoingLettersForPlayer;
    }

    /**
     * Checks if a crow belongs to an OutgoingLetter
     *
     * @param crow Crow Entity
     * @return {@code OutgoingLetter}
     * @author Kuglin
     */
    public static OutgoingLetter isCrowIn(Entity crow) {

        for (OutgoingLetter outgoingLetter : outgoingLetters) {
            if (outgoingLetter.getCrow().getCrowEntity() == crow)
                return outgoingLetter;
        }

        return null;
    }


}
