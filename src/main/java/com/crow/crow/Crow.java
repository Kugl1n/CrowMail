package com.crow.crow;

import com.crow.config.MainConfig;
import com.crow.config.MessageManager;

import com.crow.letter.OutgoingLetter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Crow {

    // Crow Entity
    private Parrot crowEntity;
    
    public void spawn(Player reciever, boolean anonimous) {

        // Spawn the Entity at Player position
        Location location = reciever.getLocation();
        crowEntity = (Parrot) reciever.getWorld().spawnEntity(location, EntityType.PARROT);
        reciever.playSound(reciever.getLocation(), Sound.ENTITY_PARROT_FLY, 1.0F, 1.0F);

        // crows.put(crowEntity, this);

        // Set Raven if Anonimous
        if (anonimous) {
            crowEntity.setVariant(MainConfig.CROW_VARIANT);
            reciever.sendMessage(MessageManager.CROW_ARRIVED);
        }

        else {
            crowEntity.setVariant(MainConfig.PIGEON_VARIANT);
            reciever.sendMessage(MessageManager.PIGEON_ARRIVED);
        }

        // Set the entity to stay still
        crowEntity.setInvulnerable(true);
        crowEntity.setGravity(false);
        crowEntity.setAI(false);
    
    }
    
    public void remove() {

        // crows.remove(crowEntity);
        crowEntity.remove();

    }

    public static void removeAllCrows(){
        ArrayList<OutgoingLetter> outgoingLetters = OutgoingLetter.getOutgoingLetters();

        for (OutgoingLetter outgoingLetter : outgoingLetters){
            if (outgoingLetter.getCrow().crowEntity.isValid()){
                outgoingLetter.getCrow().remove();
            }
        }

    }

    public Parrot getCrowEntity(){
        return crowEntity;
    }

    public void playFeedbackGood(Player player){
        crowEntity.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, crowEntity.getLocation(), 50, 0.3, 0.3, 0.3, 2);
        player.playSound(player.getLocation(), Sound.ENTITY_PARROT_AMBIENT, 1.0F, 1.0F);
    }

    public void playFeedbackBad(Player player){
        crowEntity.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, crowEntity.getLocation(), 10, 0.3, 0.3, 0.3, 2);
        player.playSound(player.getLocation(), Sound.ENTITY_PARROT_IMITATE_PILLAGER, 1.0F, 1.0F);

    }

}
