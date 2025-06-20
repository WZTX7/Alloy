package top.friendcraft.alloy.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import top.friendcraft.alloy.Alloy;

public class SunlightCollectorEntity extends BaseContainerBlockEntity {
    ItemStack mixinSlot;
    ItemStack resultSlot;
    int mixProgress;
    int mixTotalTime;
    protected final ContainerData dataAccess;
    public SunlightCollectorEntity(BlockPos pos, BlockState blockState) {
        super(Alloy.sunlight_collector_entity.get(), pos, blockState);
        this.dataAccess = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 1 -> SunlightCollectorEntity.this.mixProgress;
                    case 2 -> SunlightCollectorEntity.this.mixTotalTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 1 -> SunlightCollectorEntity.this.mixProgress = value;
                    case 2 -> SunlightCollectorEntity.this.mixTotalTime = value;
                };
            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    @NotNull
    protected Component getDefaultName() {
        return Component.translatable("container.sunlight_collector");
    }

    @Override
    @NotNull
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> list = NonNullList.withSize(2, mixinSlot);
        list.set(1, resultSlot);
        return list;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        mixinSlot = items.get(0);
        resultSlot = items.get(1);
    }

    @Override
    @NotNull
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SunlightCollectorMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }


    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        NonNullList<ItemStack> stacks = getItems();
        ContainerHelper.loadAllItems(compoundTag, stacks, provider);
        mixinSlot = stacks.getFirst();
        resultSlot = stacks.get(1);
        this.mixProgress = compoundTag.getShort("MixTime");
        this.mixTotalTime = compoundTag.getShort("MixTimeTotal");
    }

    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putShort("MixTime", (short) this.mixProgress);
        compoundTag.putShort("MixTimeTotal", (short) this.mixTotalTime);
        NonNullList<ItemStack> stacks = getItems();
        ContainerHelper.saveAllItems(compoundTag, stacks, provider);
    }
}
