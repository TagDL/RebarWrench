package io.github.lijinhong11.rebarwrench.utils;

import io.github.lijinhong11.rebarwrench.RebarWrench;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;

public class KeyUtil {
    private KeyUtil() {}


    public static NamespacedKey blockKey(Block block, String sub) {
        return new NamespacedKey(RebarWrench.INSTANCE, block.getX() + "_" + block.getY() + "_" + block.getZ() + "_" + sub);
    }
}
