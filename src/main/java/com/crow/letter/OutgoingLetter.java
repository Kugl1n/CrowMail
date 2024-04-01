package com.crow.letter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.crow.config.MainConfig;

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

    public static ArrayList<OutgoingLetter> outgoingLetters = new ArrayList<>();
    public static List<Player> blockedPlayers = new ArrayList<>();

    private OfflinePlayer player;
    private ItemStack letter;
    private boolean anonimous;
    private double distance;
    private UUID playerUUID;
    private Crow crow;
    private boolean isDelivered;

    public OutgoingLetter(OfflinePlayer player, ItemStack letter, double distance) {

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

                if (MainConfig.BLOCKED_GAMEMODES.contains(((Player) player).getGameMode()) || OutgoingLetter.blockedPlayers.contains(player))
                    return;

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
