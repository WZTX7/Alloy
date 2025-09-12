package top.friendcraft.alloy.fabric.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AlloyBucketItemFabric extends BucketItem {
    private static final List<AlloyBucketItemFabric> RegisterTable = new ArrayList<>();
    Supplier<? extends Fluid> fluidSupplier;
    public AlloyBucketItemFabric(Supplier<? extends Fluid> content, Properties properties) {
        super(null, properties);
        fluidSupplier = content;
        RegisterTable.add(this);
    }

    public static void register() {
        for (AlloyBucketItemFabric item : RegisterTable) {
            item.registerIn();
        }
    }

    private void registerIn() {
        content = fluidSupplier.get();
    }

}
