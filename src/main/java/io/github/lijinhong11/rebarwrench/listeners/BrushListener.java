package io.github.lijinhong11.rebarwrench.listeners;

import io.github.lijinhong11.rebarwrench.wrenches.TheWrenchItem;
import io.github.pylonmc.rebar.item.RebarItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.PlayerInventory;

public class BrushListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBrush(BlockBreakEvent e) {
        Player p = e.getPlayer();
        PlayerInventory pi = p.getInventory();

        if (RebarItem.isRebarItem(pi.getItemInOffHand(), TheWrenchItem.class) || RebarItem.isRebarItem(pi.getItemInMainHand(), TheWrenchItem.class)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBrush(BlockDropItemEvent e) {
        Player p = e.getPlayer();
        PlayerInventory pi = p.getInventory();

        if (RebarItem.isRebarItem(pi.getItemInOffHand(), TheWrenchItem.class) || RebarItem.isRebarItem(pi.getItemInMainHand(), TheWrenchItem.class)) {
            e.setCancelled(true);
        }
    }
}
