package top.friendcraft.alloy.common.block;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class BlockWithItem<B extends Block> {
    public RegistrySupplier<B> block;
    public RegistrySupplier<BlockItem> item;
    public BlockWithItem(RegistrySupplier<B> block, RegistrySupplier<BlockItem> item) {
        this.block = block;
        this.item = item;
    }
}
