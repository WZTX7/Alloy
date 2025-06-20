package top.friendcraft.alloy.common.block;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class LeveledMeltFurnaceCore extends AbstractMeltFurnaceCore {
    public LeveledMeltFurnaceCore(Properties properties, int levels) {
        super(properties, levels);
    }

    @Override
    @NotNull
    protected MapCodec<? extends AbstractMeltFurnaceCore> codec() {
        return simpleCodec(properties -> new LeveledMeltFurnaceCore(properties, this.levels));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LeveledMeltingEntity(blockPos, blockState, this.levels);
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractMeltingEntity entity && player instanceof ServerPlayer serverPlayer) {
            MenuRegistry.openExtendedMenu(serverPlayer, entity, friendlyByteBuf -> {});
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createFurnaceTicker(level, blockEntityType, LeveledMeltingEntity.switchEntityType(levels));
    }
}
