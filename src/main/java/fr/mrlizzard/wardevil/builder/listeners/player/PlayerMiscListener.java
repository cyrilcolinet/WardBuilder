package fr.mrlizzard.wardevil.builder.listeners.player;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerMiscListener implements Listener {

    private WardBuilder                     instance;

    public PlayerMiscListener(WardBuilder instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if ((event.getEntity() != null) && (event.getEntity().getType().equals(EntityType.PLAYER))) {
            event.setCancelled(true);
        }
    }
}
