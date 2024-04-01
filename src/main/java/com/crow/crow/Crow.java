package com.crow.crow;

import com.crow.config.MainConfig;
import com.crow.config.MessageManager;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;

public class Crow {

    // Crow Entity
    private Parrot crowEntity;
    
    public void spawn(Player reciever, boolean anonimous) {

        // Spawn the Entity at Player position
        Location location = reciever.getLocation();
        crowEntity = (Parrot) reciever.getWorld().spawnEntity(location, EntityType.PARROT);
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

    public Parrot getCrowEntity(){
        return crowEntity;
    }

    public void playParticleGood(){
        crowEntity.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, crowEntity.getLocation(), 50, 0.3, 0.3, 0.3, 2);
    }

    public void playParticleBad(){
        crowEntity.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, crowEntity.getLocation(), 50, 0.3, 0.3, 0.3, 2);
    }

}
