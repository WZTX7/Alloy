package top.friendcraft.alloy.fabric;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import top.friendcraft.alloy.Alloy;
import net.fabricmc.api.ModInitializer;
import top.friendcraft.alloy.core.registry.RegistryHolder;

public final class AlloyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Alloy.init();
        RegistryHolder.register();
        Alloy.registerFuels();
    }
}
