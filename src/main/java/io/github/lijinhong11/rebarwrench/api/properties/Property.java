package io.github.lijinhong11.rebarwrench.api.properties;

import com.google.common.base.Preconditions;
import io.github.pylonmc.rebar.block.RebarBlock;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import xyz.xenondevs.invui.util.TriConsumer;

import java.util.*;

public final class Property<T> {
    private final Class<T> type;
    private final List<Entry<T>> entries;
    private final int defaultIndex;
    private final TriConsumer<RebarBlock, T, T> onChange;

    private Property(Builder<T> builder) {
        this.type = builder.type;
        this.entries = List.copyOf(builder.entries);
        this.defaultIndex = builder.defaultIndex;
        this.onChange = builder.onChange;
    }

    public @NotNull Class<T> typeClass() {
        return this.type;
    }

    public @NotNull @Unmodifiable List<T> possibleValues() {
        return entries.stream().map(e -> e.value).toList();
    }

    public @NotNull @Unmodifiable List<Entry<T>> entries() {
        return this.entries;
    }

    public int defaultIndex() {
        return this.defaultIndex;
    }

    public @NotNull Component displayName(int index) {
        return this.entries.get(index).displayName;
    }

    @SuppressWarnings("unchecked")
    public void triggerOnChange(@NotNull RebarBlock machine, @NotNull Object oldValue, @NotNull Object newValue) {
        if (this.onChange != null) {
            this.onChange.accept(machine, (T) oldValue, (T) newValue);
        }
    }

    public static <T> Builder<T> builder(@NotNull Class<T> clazz) {
        Preconditions.checkNotNull(clazz);
        return new Builder<>(clazz);
    }

    public static <T> Entry<T> entry(T value, Component displayName) {
        return new Entry<>(value, displayName);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Entry<T> {
        private final T value;
        private final @NonNull @NotNull Component displayName;
    }

    public final static class Builder<T> {
        private final Class<T> type;
        private final List<Entry<T>> entries = new ArrayList<>();
        private int defaultIndex = 0;
        private TriConsumer<RebarBlock, T, T> onChange;

        private Builder(Class<T> type) {
            this.type = type;
        }

        public Builder<T> onValueChange(@NotNull TriConsumer<RebarBlock, T, T> onChange) {
            this.onChange = onChange;
            return this;
        }

        @SafeVarargs
        public final Builder<T> entries(@NotNull Entry<T>... entries) {
            this.entries.clear();
            this.entries.addAll(Arrays.asList(entries));
            return this;
        }

        public Builder<T> entries(@NotNull List<Entry<T>> entries) {
            this.entries.clear();
            this.entries.addAll(entries);
            return this;
        }

        @SafeVarargs
        public final Builder<T> possibleValues(@NotNull T... values) {
            this.entries.clear();
            for (T value : values) {
                this.entries.add(new Entry<>(value, Component.text(String.valueOf(value))));
            }
            return this;
        }

        public Builder<T> possibleValues(@NotNull List<T> values) {
            this.entries.clear();
            for (T value : values) {
                this.entries.add(new Entry<>(value, Component.text(String.valueOf(value))));
            }
            return this;
        }

        public Builder<T> defaultIndex(int index) {
            this.defaultIndex = index;
            return this;
        }

        public @NotNull Property<T> build() {
            if (entries.isEmpty()) {
                throw new IllegalStateException("At least one entry is required");
            }

            if (defaultIndex < 0 || defaultIndex >= entries.size()) {
                throw new IndexOutOfBoundsException("defaultIndex must be between 0 and " + (entries.size() - 1));
            }

            return new Property<>(this);
        }
    }
}
