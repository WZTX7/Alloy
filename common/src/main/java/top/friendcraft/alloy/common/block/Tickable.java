package top.friendcraft.alloy.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface Tickable {
    void tick(ServerLevel level, BlockPos pos, BlockState state);
}
