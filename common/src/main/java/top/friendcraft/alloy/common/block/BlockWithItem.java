package top.friendcraft.alloy.common.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import top.friendcraft.alloy.core.registry.RegistrySupplier;

public class BlockWithItem<B extends Block> {
    public RegistrySupplier<Block, B> block;
    public RegistrySupplier<Item, BlockItem> item;
    public BlockWithItem(RegistrySupplier<Block, B> block, RegistrySupplier<Item, BlockItem> item) {
        this.block = block;
        this.item = item;
    }
}
