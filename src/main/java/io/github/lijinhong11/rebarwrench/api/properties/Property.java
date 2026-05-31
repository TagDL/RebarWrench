package io.github.lijinhong11.rebarwrench.api.properties;

import com.google.common.base.Preconditions;
import io.github.pylonmc.rebar.block.RebarBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import xyz.xenondevs.invui.util.TriConsumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class Property<T> {
    private final Class<T> type;
    private final LinkedList<T> possibleValues;
    private final int defaultIndex;
    private final TriConsumer<RebarBlock, T, T> onChange;

    private Property(Builder<T> builder) {
        this.type = builder.clazz;
        this.possibleValues = builder.possibleValues;
        this.defaultIndex = builder.defaultIndex;
        this.onChange = builder.onChange;
    }

    public @NotNull Class<T> typeClass() {
        return this.type;
    }

    public @NotNull @Unmodifiable List<T> possibleValues() {
        return Collections.unmodifiableList(this.possibleValues);
    }

    public int defaultIndex() {
        return this.defaultIndex;
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

    public final static class Builder<T> {
        private final Class<T> clazz;
        private LinkedList<T> possibleValues = new LinkedList<>();
        private int defaultIndex = 0;
        private TriConsumer<RebarBlock, T, T> onChange;

        private Builder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public Builder<T> onValueChange(@NotNull TriConsumer<RebarBlock, T, T> onChange) {
            this.onChange = onChange;
            return this;
        }

        @SafeVarargs
        public final Builder<T> possibleValues(@NotNull T... values) {
            this.possibleValues = new LinkedList<>(Arrays.asList(values));
            return this;
        }

        public Builder<T> possibleValues(@NotNull List<T> values) {
            this.possibleValues = new LinkedList<>(values);
            return this;
        }

        public Builder<T> defaultIndex(int index) {
            this.defaultIndex = index;
            return this;
        }

        public @NotNull Property<T> build() {
            if (defaultIndex < 0 || defaultIndex >= possibleValues.size()) {
                throw new IndexOutOfBoundsException("The default index must between 0 and possible values' size");
            }

            return new Property<>(this);
        }
    }
}
