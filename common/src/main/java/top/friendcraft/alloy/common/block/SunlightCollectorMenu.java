package top.friendcraft.alloy.common.block;

import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import top.friendcraft.alloy.Alloy;

public class SunlightCollectorMenu extends AbstractContainerMenu {
    Inventory inventory;
    Container container;
    ContainerData data;
    public SunlightCollectorMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(Alloy.sunlight_collector_menu.get(), containerId);
        this.inventory = inventory;
        this.container = container;
        this.data = data;
        this.addSlot(new Slot(container, 0, 33, 35));
        this.addSlot(new ResultSlot(container, 1, 116, 35));
        this.addStandardInventorySlots(inventory, 8, 84);
        this.addDataSlots(data);
    }

    public SunlightCollectorMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(2), new SimpleContainerData(3));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public float getBurnProgress() {
        int i = this.data.get(0);
        int j = this.data.get(1);
        return j != 0 && i != 0 ? Mth.clamp((float)i / (float)j, 0.0F, 1.0F) : 0.0F;
    }

    public float getCollectingProgress() {
        int i = this.data.get(2);
        int j = 200;
        return i != 0 ? Mth.clamp((float)i / (float)j, 0.0F, 1.0F) : 0.0F;
    }

    private static final class ResultSlot extends Slot {
        public ResultSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
