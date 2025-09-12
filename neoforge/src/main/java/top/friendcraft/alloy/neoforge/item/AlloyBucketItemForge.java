package top.friendcraft.alloy.neoforge.item;

import com.mojang.logging.LogUtils;
import dev.architectury.platform.hooks.EventBusesHooks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import top.friendcraft.alloy.Alloy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AlloyBucketItemForge extends BucketItem {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<AlloyBucketItemForge> RegisterTable = new ArrayList<>();
    Supplier<? extends Fluid> fluidSupplier;
    public AlloyBucketItemForge(Supplier<? extends Fluid> content, Properties properties) {
        super(null, properties);
        fluidSupplier = content;
        EventBusesHooks.whenAvailable(Alloy.MOD_ID, bus -> bus.addListener(this::listener));
        RegisterTable.add(this);
    }

    private void listener(RegisterCapabilitiesEvent event) {
        if (BuiltInRegistries.ITEM.containsValue(this)) {
            event.registerItem(Capabilities.FluidHandler.ITEM,
                    (stack, ctx) -> new FluidBucketWrapper(stack),
                    this);
        } else {
            LOGGER.warn("Bucket item not registered: {}", this);
        }
    }

    private static void listener2(RegisterEvent event) {
        if (event.getRegistry().equals(Registries.CREATIVE_MODE_TAB)) {
            RegisterTable.forEach(AlloyBucketItemForge::registerIn);
        }
    }

    private void registerIn() {
        content = fluidSupplier.get();
    }

    static {
        EventBusesHooks.whenAvailable(Alloy.MOD_ID, bus -> bus.addListener(AlloyBucketItemForge::listener2));
    }
}
