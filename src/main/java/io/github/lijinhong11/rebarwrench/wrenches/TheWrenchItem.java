package io.github.lijinhong11.rebarwrench.wrenches;

import io.github.lijinhong11.rebarwrench.RebarWrench;
import io.github.lijinhong11.rebarwrench.api.WrenchAction;
import io.github.lijinhong11.rebarwrench.api.WrenchResult;
import io.github.lijinhong11.rebarwrench.api.Wrenchable;
import io.github.pylonmc.rebar.block.BlockStorage;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.context.BlockBreakContext;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.interfaces.InteractRebarItemHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

public class TheWrenchItem extends RebarItem implements InteractRebarItemHandler {
    private final boolean enableFastBreaking = getSettings().get("enableFastBreaking", ConfigAdapter.BOOLEAN, true);

    public TheWrenchItem(@NonNull ItemStack stack) {
        super(stack);
    }

    @Override
    public void onInteract(@NonNull PlayerInteractEvent event, @NonNull EventPriority priority) {
        Player p = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        Material type = clickedBlock.getType();
        if (type == Material.SUSPICIOUS_SAND || type == Material.SUSPICIOUS_GRAVEL) {
            event.setCancelled(true);
        }

        RebarBlock rb = BlockStorage.get(clickedBlock);
        if (rb == null) return;

        Wrenchable wrenchable = RebarWrench.getWrenchable(rb.getClass());
        if (wrenchable == null) {
            p.sendMessage(Component.translatable("rebarwrench.message.not_usable"));
            return;
        }

        WrenchAction action;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && p.isSneaking()) {
            if (!enableFastBreaking) return;
            action = WrenchAction.FAST_BREAK;
        } else if (event.getAction().isRightClick()) {
            action = p.isSneaking() ? WrenchAction.ROTATE : WrenchAction.CONFIGURE;
        } else {
            return;
        }

        WrenchResult result = wrenchable.onWrenchInteract(p, rb, action);

        if (result == WrenchResult.FAILED) {
            p.sendMessage(Component.translatable("rebarwrench.message.operation_unsupported"));
        }

        if (action == WrenchAction.FAST_BREAK && result == WrenchResult.SUCCESS) {
            event.setCancelled(true);
            BlockStorage.breakBlock(clickedBlock, new BlockBreakContext.PluginBreak(clickedBlock, true, true));
        }

        //avoid other machine events happen
        event.setCancelled(true);
    }
}
