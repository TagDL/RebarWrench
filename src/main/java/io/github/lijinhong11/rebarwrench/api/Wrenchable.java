package io.github.lijinhong11.rebarwrench.api;

import io.github.lijinhong11.rebarwrench.api.properties.PropertiesMap;
import io.github.lijinhong11.rebarwrench.api.properties.Property;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * A interface for wrench interactions
 */
public interface Wrenchable {
    @NotNull PropertiesMap properties();

    default boolean rejectFastBreaking() {
        return false;
    }

    static @NotNull Builder builder() {
        return new WrenchBuilder();
    }

    interface Builder {
        @NotNull Builder addProperty(@NotNull String key, @NotNull Property<?> property);

        @NotNull Builder addProperty(@NotNull String key, @NotNull Component displayName, @NotNull Property<?> property);

        @NotNull Builder rejectFastBreaking(boolean rejectFastBreaking);

        @NotNull Wrenchable build();
    }
}
