# RebarWrench
Wrench for rebar addons

## For Players & Server Owners
### Crafting
![crafting](media/crafting.png)  
2 * iron ingot + 2 * tin ingot

### Usage
![usage](media/usage.png)

## For Developers
### Importing
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.lijinhong11/RebarWrench)

```kotlin
dependencies {
    compileOnly("io.github.lijinhong11:RebarWrench:VERSION")
}
```
Replace the `VERSION` to the latest addon version

### Example
```java
package io.github.lijinhong11.rebarwrench;

import io.github.lijinhong11.rebarwrench.api.Wrenchable;
import io.github.lijinhong11.rebarwrench.api.properties.Property;
import io.github.pylonmc.rebar.block.RebarBlock;
import io.github.pylonmc.rebar.block.base.RebarInteractBlock;
import io.github.pylonmc.rebar.block.context.BlockCreateContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;

public class TestMachine extends RebarBlock implements RebarInteractBlock {
    private final NamespacedKey INT = new NamespacedKey(PLUGIN_INSTANCE, "ivalue");

    public static final Wrenchable WRENCHABLE = Wrenchable.builder()
            .addProperty("color", Component.text("Color"), Property.builder(NamedTextColor.class)
                    .entries(
                            Property.entry(NamedTextColor.BLUE, Component.text("Blue")),
                            Property.entry(NamedTextColor.GREEN, Component.text("Green"))
                    )
                    .defaultIndex(0)
                    .onValueChange((m, oldColor, newColor) -> {
                        Block b = m.getBlock();
                        b.setType(newColor == NamedTextColor.BLUE ? Material.BLUE_WOOL : Material.GREEN_WOOL);
                    })
                    .build()
            )
            .addProperty("ivalue", Component.text("Number"), Property.builder(int.class)
                    .possibleValues(0, 1, 2, 3, 4, 5)
                    .defaultIndex(0)
                    .onValueChange((m, oldInt, newInt) -> {
                        ((TestMachine) m).value = newInt;
                    })
                    .build())
            .build();

    private int value = 0;

    public TestMachine(@NonNull Block block, @NonNull BlockCreateContext context) {
        super(block, context);
    }

    public TestMachine(@NonNull Block block, @NonNull PersistentDataContainer pdc) {
        super(block, pdc);
    }

    @Override
    public void onInteract(@NonNull PlayerInteractEvent e, @NonNull EventPriority priority) {
        if (!e.getAction().isRightClick()) {
            return;
        }

        e.getPlayer().sendMessage("The int value is " + value);
    }

    @Override
    public void write(@NonNull PersistentDataContainer pdc) {
        pdc.set(INT, PersistentDataType.INTEGER, value);
    }
}

//Now to register the Wrenchable object
RebarWrench.registerWrenchable(TestMachine.class, TestMachine.WRENCHABLE);
```
