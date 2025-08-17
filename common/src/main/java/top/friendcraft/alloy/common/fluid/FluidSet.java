package top.friendcraft.alloy.common.fluid;

import com.mojang.datafixers.util.Either;
import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.core.item.ArchitecturyBucketItem;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FluidSet<F extends ArchitecturyFlowingFluid> {
    RegistrySupplier<F> source, flowing;
    SimpleArchitecturyFluidAttributes attributes;
    RegistrySupplier<ArchitecturyBucketItem> item;
    RegistrySupplier<ArchitecturyLiquidBlock> block;

    public FluidSet(RegistrySupplier<F> source, RegistrySupplier<F> flowing, RegistrySupplier<ArchitecturyBucketItem> item, RegistrySupplier<ArchitecturyLiquidBlock> block, SimpleArchitecturyFluidAttributes attributes) {
        this.source = source;
        this.flowing = flowing;
        this.item = item;
        this.block = block;
        this.attributes = attributes;
    }
}
