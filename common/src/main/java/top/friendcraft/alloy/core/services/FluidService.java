package top.friendcraft.alloy.core.services;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public interface FluidService {
    BucketItem getBucketItemByPlatform(Supplier<? extends Fluid> fluidSupplier, Item.Properties properties);
    void callBucketRegister();
}
