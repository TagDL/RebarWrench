package io.github.lijinhong11.rebarwrench.wrenches;

import io.github.lijinhong11.rebarwrench.RebarWrench;
import io.github.lijinhong11.rebarwrench.api.Wrenchable;
import io.github.lijinhong11.rebarwrench.api.properties.PropertiesMap;
import io.github.lijinhong11.rebarwrench.api.properties.Property;
import io.github.lijinhong11.rebarwrench.utils.KeyUtil;
import io.github.pylonmc.rebar.block.BlockStorage;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.context.BlockBreakContext;
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter;
import io.github.pylonmc.rebar.i18n.RebarArgument;
import io.github.pylonmc.rebar.item.RebarItem;
import io.github.pylonmc.rebar.item.base.RebarInteractor;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;

public class TheWrenchItem extends RebarItem implements RebarInteractor {
    private final boolean enableFastBreaking = getSettings(RebarWrench.WRENCH_KEY).get("enableFastBreaking", ConfigAdapter.BOOLEAN, true);

    public TheWrenchItem(@NonNull ItemStack stack) {
        super(stack);
    }

    @Override
    public void onUsedToClick(@NonNull PlayerInteractEvent event, @NonNull EventPriority priority) {
        Player p = event.getPlayer();

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        RebarBlock rb = BlockStorage.get(clickedBlock);
        if (rb == null) return;

        Wrenchable wrenchable = RebarWrench.getWrenchable(rb.getClass());
        if (wrenchable == null) {
            p.sendMessage(Component.translatable("rebarwrench.message.not_usable"));
            return;
        }

        if ((enableFastBreaking && !wrenchable.rejectFastBreaking()) && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            BlockStorage.breakBlock(clickedBlock, new BlockBreakContext.PluginBreak(clickedBlock, true, true));
            return;
        }

        PropertiesMap propertiesMap = wrenchable.properties();
        Map<String, Property<?>> props = propertiesMap.getProperties();
        if (props.isEmpty()) return;

        if (!event.getAction().isRightClick()) return;

        List<String> keys = List.copyOf(props.keySet());
        String currentKey = readActiveKey(clickedBlock, keys);

        if (event.getPlayer().isSneaking()) {
            int idx = keys.indexOf(currentKey);
            String nextKey = keys.get((idx + 1) % keys.size());
            writeActiveKey(clickedBlock, nextKey);
            p.sendMessage(Component.translatable("rebarwrench.message.switch_key", RebarArgument.of("key", propertiesMap.keyDisplayName(nextKey))));
        } else {
            Property<?> property = props.get(currentKey);
            int currentIdx = readIndex(clickedBlock, currentKey, property.defaultIndex());
            int nextIdx = (currentIdx + 1) % property.possibleValues().size();
            Object oldValue = property.possibleValues().get(currentIdx);
            Object newValue = property.possibleValues().get(nextIdx);
            writeIndex(clickedBlock, currentKey, nextIdx);
            property.triggerOnChange(rb, oldValue, newValue);
            p.sendMessage(Component.translatable("rebarwrench.message.switch_value",
                    RebarArgument.of("key", propertiesMap.keyDisplayName(currentKey)),
                    RebarArgument.of("value", property.displayName(nextIdx))));
        }
    }

    private static String readActiveKey(Block block, List<String> keys) {
        String key = block.getChunk().getPersistentDataContainer().get(KeyUtil.blockKey(block, "active"), PersistentDataType.STRING);
        if (key == null || !keys.contains(key)) {
            return keys.getFirst();
        }
        return key;
    }

    private static void writeActiveKey(Block block, String key) {
        block.getChunk().getPersistentDataContainer().set(KeyUtil.blockKey(block, "active"), PersistentDataType.STRING, key);
    }

    private static int readIndex(Block block, String propertyKey, int defaultIndex) {
        NamespacedKey k = KeyUtil.blockKey(block, "idx_" + propertyKey);
        return block.getChunk().getPersistentDataContainer().getOrDefault(k, PersistentDataType.INTEGER, defaultIndex);
    }

    private static void writeIndex(Block block, String propertyKey, int index) {
        NamespacedKey k = KeyUtil.blockKey(block, "idx_" + propertyKey);
        block.getChunk().getPersistentDataContainer().set(k, PersistentDataType.INTEGER, index);
    }
}
