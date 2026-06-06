package io.github.lijinhong11.rebarwrench.api;

import org.jetbrains.annotations.NotNull;

class WrenchBuilder implements Wrenchable.Builder {
    private Wrenchable.InteractHandler function = (_, _, _) -> WrenchResult.UNSUPPORTED;

    @Override
    public Wrenchable.@NotNull Builder interactFunction(Wrenchable.InteractHandler function) {
        this.function = function;
        return this;
    }

    @Override
    public @NotNull Wrenchable build() {
        return this.function::handle;
    }
}
