package com.crow.events;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.crow.config.ConfigLoader;
import com.crow.crow.Crow;
import com.crow.letter.OutgoingLetter;

public class PlayerE implements Listener{

    public static ArrayList<Crow> letterToSend;

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent interactEvent){
        Entity entity = interactEvent.getRightClicked();

        Player destinationPlayer = interactEvent.getPlayer();

        if (Crow.getCrows().containsKey(entity)){

            Crow crow = Crow.getCrows().get(entity);

            Iterator<ItemStack> iterator = OutgoingLetter.outgoingLetters.get(destinationPlayer.getUniqueId()).iterator();
            while (iterator.hasNext()) {
                ItemStack letter = iterator.next();

                if (destinationPlayer.getInventory().firstEmpty() == -1) {
                    destinationPlayer.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_INVENTORY_FULL);
                    crow.playParticleBad();
                    crow.remove();
                    return;

                } else {
                    destinationPlayer.getInventory().addItem(letter);
                    iterator.remove();
                }
            }
            
            crow.playParticleGood();
            destinationPlayer.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_LETTER_RECIEVED);
            crow.isDelivered = true;
            crow.remove();
            OutgoingLetter.outgoingLetters.remove(destinationPlayer.getUniqueId());
            
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent){
        if (joinEvent.getPlayer().isOnline()) {
            Player player = joinEvent.getPlayer();

            if (OutgoingLetter.outgoingLetters.get(player.getUniqueId()).size() > 0) {
                OutgoingLetter.send(player, false, ConfigLoader.ON_JOIN_DELAY);

            }
        }
    }

    @EventHandler
    public void onGamemode(PlayerGameModeChangeEvent event) {

        if (event.getPlayer().isOnline() && ConfigLoader.BLOCKED_GAMEMODES.contains(event.getPlayer().getGameMode())) {
            Player player = event.getPlayer();

            if (OutgoingLetter.outgoingLetters.get(player.getUniqueId()).size() > 0) {
                OutgoingLetter.send(player, false, ConfigLoader.ON_GAMEMODE_DELAY);

            }
        }
    }

}
