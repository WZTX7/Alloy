package top.friendcraft.alloy.common.block;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import top.friendcraft.alloy.Alloy;

public class LeveledMeltingControlMenu extends AbstractMeltingControlMenu{
    public LeveledMeltingControlMenu(int containerId, Inventory inventory, Container container, ContainerData data, int levels) {
        super(chooseByLevel(levels), containerId, inventory, container, data, levels);
    }

    public LeveledMeltingControlMenu(int containerId, Inventory inventory, int levels) {
        super(chooseByLevel(levels), containerId, inventory, levels);
    }

    private static MenuType<LeveledMeltingControlMenu> chooseByLevel(int levels) {
        return switch (levels) {
            case 2 -> Alloy.steel_menu.get();
            default -> Alloy.base_menu.get();
        };
    }
}
