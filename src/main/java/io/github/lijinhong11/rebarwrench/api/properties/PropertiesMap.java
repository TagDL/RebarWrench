package io.github.lijinhong11.rebarwrench.api.properties;

import com.google.common.base.Preconditions;
import io.github.pylonmc.rebar.block.RebarBlock;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertiesMap {
    private final Map<String, Property<?>> properties = new LinkedHashMap<>();
    private final Map<String, Integer> currentIndices = new HashMap<>();
    private final Map<String, Component> keyDisplayNames = new LinkedHashMap<>();

    public PropertiesMap() {
    }

    public void addProperty(@NotNull String key, @NotNull Component displayName, @NotNull Property<?> property) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(displayName, "displayName cannot be null");
        Preconditions.checkNotNull(property, "property cannot be null");
        this.properties.put(key, property);
        this.currentIndices.put(key, property.defaultIndex());
        this.keyDisplayNames.put(key, displayName);
    }

    public @NotNull Component keyDisplayName(@NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkArgument(this.properties.containsKey(key), "No property found for key: " + key);
        return this.keyDisplayNames.getOrDefault(key, Component.text(key));
    }

    public @Nullable Property<?> getProperty(@NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        return this.properties.get(key);
    }

    public boolean hasProperty(@NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        return this.properties.containsKey(key);
    }

    public @NotNull @Unmodifiable Map<String, Property<?>> getProperties() {
        return Collections.unmodifiableMap(this.properties);
    }

    public int getCurrentIndex(@NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Property<?> property = this.properties.get(key);
        Preconditions.checkNotNull(property, "No property found for key: " + key);
        return this.currentIndices.getOrDefault(key, property.defaultIndex());
    }

    public void setCurrentIndex(@NotNull RebarBlock machine, @NotNull String key, int index) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Property<?> property = this.properties.get(key);
        Preconditions.checkNotNull(property, "No property found for key: " + key);
        Preconditions.checkArgument(index >= 0 && index < property.possibleValues().size(),
                "Index must be between 0 and " + (property.possibleValues().size() - 1));
        int oldIndex = getCurrentIndex(key);
        if (oldIndex == index) return;
        Object oldValue = property.possibleValues().get(oldIndex);
        this.currentIndices.put(key, index);
        Object newValue = property.possibleValues().get(index);
        property.triggerOnChange(machine, oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T getCurrentValue(@NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Property<?> property = this.properties.get(key);
        if (property == null) return null;
        int index = this.currentIndices.getOrDefault(key, property.defaultIndex());
        return (T) property.possibleValues().get(index);
    }

    public void cycleNext(@NotNull RebarBlock machine, @NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Property<?> property = this.properties.get(key);
        Preconditions.checkNotNull(property, "No property found for key: " + key);
        int current = getCurrentIndex(key);
        int next = (current + 1) % property.possibleValues().size();
        setCurrentIndex(machine, key, next);
    }

    public void cyclePrevious(@NotNull RebarBlock machine, @NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Property<?> property = this.properties.get(key);
        Preconditions.checkNotNull(property, "No property found for key: " + key);
        int current = getCurrentIndex(key);
        int size = property.possibleValues().size();
        int previous = (current - 1 + size) % size;
        setCurrentIndex(machine, key, previous);
    }

    public void reset(@NotNull String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Property<?> property = this.properties.get(key);
        Preconditions.checkNotNull(property, "No property found for key: " + key);
        this.currentIndices.put(key, property.defaultIndex());
    }
}
