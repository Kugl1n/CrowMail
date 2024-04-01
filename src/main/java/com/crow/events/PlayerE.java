package com.crow.events;

import java.util.ArrayList;

import com.crow.config.MainConfig;
import com.crow.config.MessageManager;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.crow.crow.Crow;
import com.crow.letter.OutgoingLetter;

public class PlayerE implements Listener{

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent interactEvent){
        
        Entity entity = interactEvent.getRightClicked();

        Player destinationPlayer = interactEvent.getPlayer();

        OutgoingLetter outgoingLetter1 = OutgoingLetter.isPlayerIn(destinationPlayer);
        OutgoingLetter outgoingLetter2 = OutgoingLetter.isCrowIn(entity);

        if (outgoingLetter1 == outgoingLetter2) {

            Crow crow = outgoingLetter1.getCrow();

            if (destinationPlayer.getInventory().firstEmpty() == -1) {

                destinationPlayer.sendMessage(MessageManager.INVENTORY_FULL);
                crow.playParticleBad();
                crow.remove();
                return;
            }

            destinationPlayer.getInventory().addItem(outgoingLetter1.getLetter());
            crow.playParticleGood();
            destinationPlayer.sendMessage(MessageManager.LETTER_RECIEVED);
            outgoingLetter1.setDelivered();
            crow.remove();
            OutgoingLetter.outgoingLetters.remove(outgoingLetter1);

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent){

        Player player = joinEvent.getPlayer();

        if (player.isOnline()) {
            OutgoingLetter outgoingLetter = OutgoingLetter.isPlayerIn(player);
            
            if (outgoingLetter != null){
                outgoingLetter.updatePlayer(player);
                outgoingLetter.send(MainConfig.ON_JOIN_DELAY);
            }
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){

        Player player = event.getPlayer();

        OutgoingLetter outgoingLetter = OutgoingLetter.isPlayerIn(player);
        if (outgoingLetter != null) {
            outgoingLetter.removeCrow();
        }
    }

    @EventHandler
    public void onGamemode(PlayerGameModeChangeEvent event) {

        Player player = event.getPlayer();

        if (player.isOnline() && MainConfig.BLOCKED_GAMEMODES.contains(player.getGameMode())) {

            OutgoingLetter outgoingLetter = OutgoingLetter.isPlayerIn(player);
            if (outgoingLetter != null)
                outgoingLetter.send(MainConfig.ON_GAMEMODE_DELAY);

        }
    }

}
