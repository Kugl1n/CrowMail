package com.crow.events;

import com.crow.config.MainConfig;
import com.crow.config.MessageManager;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import com.crow.crow.Crow;
import com.crow.letter.OutgoingLetter;

public class PlayerE implements Listener{

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent interactEvent){
        
        Entity entity = interactEvent.getRightClicked();

        Player destinationPlayer = interactEvent.getPlayer();

        ArrayList<OutgoingLetter> outgoingLettersForPlayers = OutgoingLetter.isPlayerIn(destinationPlayer);
        OutgoingLetter outgoingLetterCrow = OutgoingLetter.isCrowIn(entity);

        for (OutgoingLetter outgoingLetter : outgoingLettersForPlayers) { 
            
            if (outgoingLetter == outgoingLetterCrow) {
                
                Crow crow = outgoingLetter.getCrow();

                if (destinationPlayer.getInventory().firstEmpty() == -1) {

                    destinationPlayer.sendMessage(MessageManager.INVENTORY_FULL);
                    crow.playParticleBad();
                    crow.remove();
                    return;
                }
    
                destinationPlayer.getInventory().addItem(outgoingLetter.getLetter());
                crow.playParticleGood();
                destinationPlayer.sendMessage(MessageManager.LETTER_RECIEVED);
                outgoingLetter.setDelivered();
                crow.remove();
                OutgoingLetter.outgoingLetters.remove(outgoingLetter);

                
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent){

        Player player = joinEvent.getPlayer();

        if (player.isOnline()) {

            ArrayList<OutgoingLetter> outgoingLetters = OutgoingLetter.isPlayerIn(player);
            
            for (OutgoingLetter outgoingLetter : outgoingLetters) {
                outgoingLetter.updatePlayer(player);
                outgoingLetter.send(MainConfig.ON_JOIN_DELAY);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){

        Player player = event.getPlayer();

        ArrayList<OutgoingLetter> outgoingLetters = OutgoingLetter.isPlayerIn(player);
        
        for (OutgoingLetter outgoingLetter : outgoingLetters) {
            outgoingLetter.removeCrow();
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){
        Player player = e.getPlayer();

        if (MainConfig.BLOCKED_WORLDS.contains(player.getWorld())){

            ArrayList<OutgoingLetter> outgoingLetters = OutgoingLetter.isPlayerIn(player);
            for (OutgoingLetter outgoingLetter : outgoingLetters) {
                outgoingLetter.send(MainConfig.ON_WORLD_CHANGE_DELAY);
            }
        }
    }

    @EventHandler
    public void onGamemode(PlayerGameModeChangeEvent event) {

        Player player = event.getPlayer();

        if (player.isOnline() && MainConfig.BLOCKED_GAMEMODES.contains(player.getGameMode())) {

            ArrayList<OutgoingLetter> outgoingLetters = OutgoingLetter.isPlayerIn(player);
            for (OutgoingLetter outgoingLetter : outgoingLetters) {
                outgoingLetter.send(MainConfig.ON_GAMEMODE_DELAY);
            }     
        }
    }
}
