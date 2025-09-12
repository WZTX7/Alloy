package top.friendcraft.alloy.common.fluid;

import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import top.friendcraft.alloy.core.registry.RegistrySupplier;

public class FluidSet<F extends ArchitecturyFlowingFluid> {
    RegistrySupplier<Fluid, F> source, flowing;
    SimpleArchitecturyFluidAttributes attributes;
    RegistrySupplier<Item, Item> item;
    RegistrySupplier<Block, LiquidBlock> block;

    public FluidSet(RegistrySupplier<Fluid, F> source, RegistrySupplier<Fluid, F> flowing, RegistrySupplier<Item, Item> item, RegistrySupplier<Block, LiquidBlock> block, SimpleArchitecturyFluidAttributes attributes) {
        this.source = source;
        this.flowing = flowing;
        this.item = item;
        this.block = block;
        this.attributes = attributes;
    }
}
