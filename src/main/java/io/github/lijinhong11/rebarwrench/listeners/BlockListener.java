package io.github.lijinhong11.rebarwrench.listeners;

import io.github.lijinhong11.rebarwrench.RebarWrench;
import io.github.lijinhong11.rebarwrench.api.Wrenchable;
import io.github.lijinhong11.rebarwrench.utils.KeyUtil;
import io.github.pylonmc.rebar.block.BlockStorage;
import io.github.pylonmc.rebar.block.RebarBlock;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;

public class BlockListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(BlockStorage.get(block) instanceof RebarBlock rb)) return;
        Wrenchable wrenchable = RebarWrench.getWrenchable(rb.getClass());
        if (wrenchable == null) return;
        PersistentDataContainer pdc = block.getChunk().getPersistentDataContainer();

        pdc.remove(KeyUtil.blockKey(block, "active"));

        for (String propertyKey : wrenchable.properties().getProperties().keySet()) {
            pdc.remove(KeyUtil.blockKey(block, "idx_" + propertyKey));
        }
    }
}
