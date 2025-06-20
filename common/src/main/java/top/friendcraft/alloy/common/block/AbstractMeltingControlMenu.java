package top.friendcraft.alloy.common.block;

import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.friendcraft.alloy.Alloy;

import java.util.stream.StreamSupport;

public abstract class AbstractMeltingControlMenu extends AbstractContainerMenu {
    Inventory inventory;
    Container container;
    ContainerData data;
    int levels;
    protected AbstractMeltingControlMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, Container container, ContainerData data, int levels) {
        super(menuType, containerId);
        this.inventory = inventory;
        this.container = container;
        this.data = data;
        this.levels = levels;
        StreamSupport.stream(Alloy.inputData.get(levels)[0].spliterator(), false).forEach(function -> this.addSlot(function.apply(container)));
        StreamSupport.stream(Alloy.inputData.get(levels)[1].spliterator(), false).forEach(function -> this.addSlot(function.apply(container)));
        this.addSlot(new Slot(container, 37, 134, 35));
        this.addStandardInventorySlots(inventory, 8, 84);
        this.addDataSlots(data);
    }

    protected AbstractMeltingControlMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, int levels) {
        this(menuType, containerId, inventory, new SimpleContainer(38), new SimpleContainerData(4), levels);
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public float getBurnProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? Mth.clamp((float)i / (float)j, 0.0F, 1.0F) : 0.0F;
    }

    public float getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }

        return Mth.clamp((float)this.data.get(0) / (float)i, 0.0F, 1.0F);
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }
}
