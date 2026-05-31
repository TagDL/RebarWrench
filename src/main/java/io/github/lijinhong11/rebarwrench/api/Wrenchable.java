package io.github.lijinhong11.rebarwrench.api;

import io.github.lijinhong11.rebarwrench.api.properties.PropertiesMap;
import io.github.lijinhong11.rebarwrench.api.properties.Property;
import org.jetbrains.annotations.NotNull;

public interface Wrenchable {
    @NotNull PropertiesMap properties();

    static @NotNull Builder builder() {
        return new WrenchBuilder();
    }

    interface Builder {
        @NotNull Builder addProperty(@NotNull String key, @NotNull Property<?> property);

        @NotNull Wrenchable build();
    }
}
