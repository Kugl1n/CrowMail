package com.crow.crow;

import java.util.HashMap;

import com.crow.config.MainConfig;
import com.crow.config.MessageManager;
import jdk.tools.jmod.Main;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.crow.CrowMail;
import com.crow.config.ConfigLoader;
import com.crow.letter.OutgoingLetter;


public class Crow {


    //HashMap of All Crows
    private static HashMap<Entity, Crow> crows = new HashMap<>();

    // Crow Entity
    private Parrot crowEntity;

    public boolean isDelivered;

    // Constructor
    public Crow(){

        this.isDelivered = false;
    }

    public static HashMap<Entity,Crow> getCrows(){
        return crows;
    }

    public void spawn(Player reciever, boolean anonimous) {

        // Verify Gamemode and Blocked
        if (MainConfig.BLOCKED_GAMEMODES.contains(reciever.getGameMode())){
            reciever.sendMessage(MessageManager.IN_BLOCKED_GAMEMODE);
            return;
        }

        if (OutgoingLetter.blockedPlayers.contains(reciever)) {
            reciever.sendMessage(MessageManager.IN_BLOCKED_MODE);
            return;
        }

        // Spawn the Entity at Player position
        Location location = reciever.getLocation();
        crowEntity = (Parrot) reciever.getWorld().spawnEntity(location, EntityType.PARROT);
        crows.put(crowEntity, this);

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
        
        // Removes the entity after time
        new BukkitRunnable() {

            @Override
            public void run() {

                remove();

                if (!isDelivered && reciever.isOnline()) {
                    OutgoingLetter.send(reciever, anonimous, MainConfig.RESEND_DELAY);
                }
            
            }
            
        }.runTaskLater(CrowMail.getInstance(), MainConfig.DESPAWN_DELAY);

    }

    // Remove the entity completly
    public void remove() {

        crows.remove(crowEntity);
        crowEntity.remove();

    }

    public void playParticleGood(){
        crowEntity.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, crowEntity.getLocation(), 50, 0.3, 0.3, 0.3, 2);
    }

    public void playParticleBad(){
        crowEntity.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, crowEntity.getLocation(), 50, 0.3, 0.3, 0.3, 2);
    }

}
