package top.friendcraft.alloy.common.block;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.friendcraft.alloy.Alloy;

public class LeveledMeltingControlScreen extends AbstractMeltingControlScreen<LeveledMeltingControlMenu>{

    public LeveledMeltingControlScreen(LeveledMeltingControlMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, switch (menu.levels) {
            case 2 -> "textures/gui/container/steel_melt_furnace_core.png";
            default -> "textures/gui/container/base_melt_furnace_core.png";
        }));
    }
}
