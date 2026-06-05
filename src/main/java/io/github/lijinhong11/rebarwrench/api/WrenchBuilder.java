package io.github.lijinhong11.rebarwrench.api;

import io.github.lijinhong11.rebarwrench.api.properties.PropertiesMap;
import io.github.lijinhong11.rebarwrench.api.properties.Property;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

class WrenchBuilder implements Wrenchable.Builder {
    private final PropertiesMap properties = new PropertiesMap();
    private boolean rejectFastBreaking = false;

    @Override
    public Wrenchable.@NotNull Builder addProperty(@NotNull String key, @NotNull Property<?> property) {
        this.properties.addProperty(key, Component.text(key), property);
        return this;
    }

    @Override
    public @NotNull Wrenchable.Builder addProperty(@NotNull String key, @NotNull Component displayName, @NotNull Property<?> property) {
        this.properties.addProperty(key, displayName, property);
        return this;
    }

    @Override
    public Wrenchable.@NotNull Builder rejectFastBreaking(boolean rejectFastBreaking) {
        this.rejectFastBreaking = rejectFastBreaking;
        return this;
    }

    @Override
    public @NotNull Wrenchable build() {
        return new Wrenchable() {
            @Override
            public @NotNull PropertiesMap properties() {
                return properties;
            }

            @Override
            public boolean rejectFastBreaking() {
                return rejectFastBreaking;
            }
        };
    }
}
