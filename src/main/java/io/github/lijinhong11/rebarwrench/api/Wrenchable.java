package io.github.lijinhong11.rebarwrench.api;

import io.github.pylonmc.rebar.block.RebarBlock;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Wrenchable {
    WrenchResult onWrenchInteract(Player player, RebarBlock block, WrenchAction action);

    static @NotNull Builder builder() {
        return new WrenchBuilder();
    }

    @FunctionalInterface
    interface InteractHandler {
        WrenchResult handle(Player player, RebarBlock block, WrenchAction action);
    }

    interface Builder {
        @NotNull Builder interactFunction(InteractHandler function);

        @NotNull Wrenchable build();
    }
}
