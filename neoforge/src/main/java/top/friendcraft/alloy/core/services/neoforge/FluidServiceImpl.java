package top.friendcraft.alloy.core.services.neoforge;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import top.friendcraft.alloy.core.services.FluidService;
import top.friendcraft.alloy.neoforge.item.AlloyBucketItemForge;

import java.util.function.Supplier;

public class FluidServiceImpl implements FluidService {
    public BucketItem getBucketItemByPlatform(Supplier<? extends Fluid> fluidSupplier, Item.Properties properties) {
        return new AlloyBucketItemForge(fluidSupplier, properties);
    }

    public void callBucketRegister() {
    }
}
