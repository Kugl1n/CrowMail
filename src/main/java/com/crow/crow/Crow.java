package com.crow.crow;

import java.util.HashMap;

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
        if (ConfigLoader.BLOCKED_GAMEMODES.contains(reciever.getGameMode()) || OutgoingLetter.bloquedPlayers.contains(reciever)){
            return;
        }

        // Spawn the Entity at Player position
        Location location = reciever.getLocation();
        crowEntity = (Parrot) reciever.getWorld().spawnEntity(location, EntityType.PARROT);
        crows.put(crowEntity, this);

        // Set Raven if Anonimous
        if (anonimous) {
            crowEntity.setVariant(Parrot.Variant.GRAY);
            reciever.sendMessage("§8§l[Crow§f§lMail§8§l] §r§7Um §4§lCorvo§r chegou.");
        }

        else {
            crowEntity.setVariant(Parrot.Variant.BLUE);
            reciever.sendMessage("§8§l[Crow§f§lMail§8§l] §r§7Um Pombo chegou.");
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
                    OutgoingLetter.send(reciever, anonimous);
                }
            
            }
            
        }.runTaskLater(CrowMail.getInstance(), ConfigLoader.DESPAWN_TIME);

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
