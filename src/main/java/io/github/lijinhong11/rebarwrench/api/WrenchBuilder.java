package io.github.lijinhong11.rebarwrench.api;

import io.github.lijinhong11.rebarwrench.api.properties.PropertiesMap;
import io.github.lijinhong11.rebarwrench.api.properties.Property;
import org.jetbrains.annotations.NotNull;

class WrenchBuilder implements Wrenchable.Builder {
    private final PropertiesMap properties = new PropertiesMap();

    @Override
    public @NotNull Wrenchable.Builder addProperty(@NotNull String key, @NotNull Property<?> property) {
        this.properties.addProperty(key, property);
        return this;
    }

    @Override
    public @NotNull Wrenchable build() {
        return () -> properties;
    }
}
