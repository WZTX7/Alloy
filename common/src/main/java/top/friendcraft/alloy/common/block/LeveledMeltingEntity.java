package top.friendcraft.alloy.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import top.friendcraft.alloy.Alloy;

public class LeveledMeltingEntity extends AbstractMeltingEntity {
    public LeveledMeltingEntity(BlockPos blockPos, BlockState blockState, int levels) {
        super(switchEntityType(levels), blockPos, blockState, levels);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(String.format("container.%s_melt_furnace_core", switch (this.levels) {
            case 2 -> "steel";
            default -> "base";
        }));
    }

    public static BlockEntityType<LeveledMeltingEntity> switchEntityType(int levels) {
        return switch (levels) {
            case 2 -> Alloy.steel_entity.get();
            default -> Alloy.base_entity.get();
        };
    }
}
