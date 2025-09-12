package top.friendcraft.alloy.common.block;

import com.mojang.logging.LogUtils;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import top.friendcraft.alloy.Alloy;
import top.friendcraft.alloy.common.item.InputData;
import top.friendcraft.alloy.common.item.ItemStackHandler;
import top.friendcraft.alloy.common.item.crafting.MeltingInput;
import top.friendcraft.alloy.common.item.crafting.MeltingRecipe;

public abstract class AbstractMeltingEntity extends BaseContainerBlockEntity implements Tickable {
    private static final Logger logger = LogUtils.getLogger();
    protected ItemStackHandler handler = new ItemStackHandler(38);
    int litTime;
    int litDuration = 0;
    int cookingProgress;
    int cookingTotalTime;
    int lastFuelSlot;
    protected final ContainerData dataAccess;
    int levels;
    private final RecipeManager.CachedCheck<MeltingInput, MeltingRecipe> quickCheck;
    boolean isFast = false;

    private static final BlockPattern SPEED = BlockPatternBuilder.start()
            .aisle( "?????",
                    "?DDD?",
                    "?DDD?",
                    "?DVD?",
                    "?????")
            .aisle( "?DDD?",
                    "DDLDD",
                    "DLLLD",
                    "DDLDD",
                    "?DDD?")
            .where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))
            .where('D', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.DEEPSLATE_BRICKS).or(BlockStatePredicate.forBlock(Blocks.POLISHED_DEEPSLATE))))
            .where('L', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.LAVA)))
            .where('V', BlockInWorld.hasState(BlockStatePredicate.forBlock(Alloy.blocks.base_core.block.get())))
            .build();

    protected AbstractMeltingEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int levels) {
        super(blockEntityType, blockPos, blockState);
        this.dataAccess = new ContainerData() {
            public int get(int i) {
                return switch (i) {
                    case 0 -> AbstractMeltingEntity.this.litTime;
                    case 1 -> AbstractMeltingEntity.this.litDuration;
                    case 2 -> AbstractMeltingEntity.this.cookingProgress;
                    case 3 -> AbstractMeltingEntity.this.cookingTotalTime;
                    default -> 0;
                };
            }

            public void set(int i, int j) {
                switch (i) {
                    case 0 -> AbstractMeltingEntity.this.litTime = j;
                    case 1 -> AbstractMeltingEntity.this.litDuration = j;
                    case 2 -> AbstractMeltingEntity.this.cookingProgress = j;
                    case 3 -> AbstractMeltingEntity.this.cookingTotalTime = j;
                }
            }

            public int getCount() {
                return 4;
            }
        };
        this.levels = levels;
        this.quickCheck = RecipeManager.createCheck(Alloy.blocks.recipe.get());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return handler.getStacks();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        handler = new ItemStackHandler(nonNullList);
    }


    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.handler = new ItemStackHandler(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
        ContainerHelper.loadAllItems(compoundTag, this.handler.getStacks(), provider);
        this.litTime = compoundTag.getShort("BurnTime");
        this.cookingProgress = compoundTag.getShort("CookTime");
        this.cookingTotalTime = compoundTag.getShort("CookTimeTotal");
        this.litDuration = 0;
    }

    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putShort("BurnTime", (short)this.litTime);
        compoundTag.putShort("CookTime", (short)this.cookingProgress);
        compoundTag.putShort("CookTimeTotal", (short)this.cookingTotalTime);
        ContainerHelper.saveAllItems(compoundTag, this.handler.getStacks(), provider);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new LeveledMeltingControlMenu(i, inventory, this, this.dataAccess, this.levels);
    }

    @Override
    public int getContainerSize() {
        return handler.getSlots();
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, AbstractMeltingEntity furnace) {
        furnace.tick(level, pos, state);
    }

    public void tick(ServerLevel level, BlockPos pos, BlockState state) {
        boolean initIsLit = isLit();
        boolean isStatusChanged = false;
        if (initIsLit) --litTime;
        if (litDuration == 0) {
            lastFuelSlot = checkLitDurationData();
        }
        if (!isLit() && (lastFuelSlot == -1 || checkIsInputEmpty())) {
            if (cookingProgress > 0) cookingProgress = Mth.clamp(cookingProgress - 2, 0, cookingTotalTime);
        } else {
            MeltingInput input = new MeltingInput(inputs(), levels);
            RecipeHolder<MeltingRecipe> recipeHolder;
            if (!checkIsInputEmpty()) {
                recipeHolder = quickCheck.getRecipeFor(input, level).orElse(null);
            } else {
                recipeHolder = null;
            }
            if (!isLit() && canBurn(level.registryAccess(), recipeHolder, input)) {
                ItemStack itemStack = getItem(lastFuelSlot == -1 ? 32 : lastFuelSlot);
                litTime = getBurnDuration(level.fuelValues(), itemStack);
                litDuration = litTime;
                if (isLit()) {
                    isStatusChanged = true;
                    if (lastFuelSlot != -1) {
                        Item item = itemStack.getItem();
                        itemStack.shrink(1);
                        if (itemStack.isEmpty()) {
                            setItem(1, item.getCraftingRemainder());
                        }
                    }
                }
            }
            if (isLit() && canBurn(level.registryAccess(), recipeHolder, input)) {
                ++cookingProgress;
                if (isFast) cookingProgress+=3;
                if (cookingProgress >= cookingTotalTime) {
                    cookingProgress = 0;
                    cookingTotalTime = getTotalCookTime(level);
                    boolean used = burn(level.registryAccess(), recipeHolder, input);
                    isStatusChanged = true;
                }
            } else {
                cookingProgress = 0;
            }
        }
        if (initIsLit != isLit()) {
            isStatusChanged = true;
            state = state.setValue(AbstractFurnaceBlock.LIT, isLit());
            level.setBlock(pos, state, 3);
        }
        if (isStatusChanged) {
            setChanged(level, pos, state);
        }
        if (!(state.getBlock() instanceof AbstractMeltFurnaceCore) || levels != 1)
            return;
        Direction oppsite_direction = state.getValue(AbstractMeltFurnaceCore.FACING).getOpposite();
        int x = oppsite_direction.getUnitVec3i().getX(); // 初始偏移量X
        int y = oppsite_direction.getUnitVec3i().getY(); // 初始偏移量Y
        int z = oppsite_direction.getUnitVec3i().getZ(); // 初始偏移量Z
        BlockPos posA = pos.offset(
                x+z+x+x+z,
                y,
                z-x+z+z-x
        ); // 偏移量
        isFast = SPEED.find(level, posA) != null;
        if (isFast) logger.info("fast");
    }

    protected int getBurnDuration(FuelValues fuelValues, ItemStack stack) {
        return FuelRegistry.get(stack, Alloy.blocks.recipe.get(), fuelValues);
    }

    protected int checkLitDurationData() {
        InputData durationData = Alloy.inputData.get(levels)[1];
        for (int j = 0; j < durationData.height(); j++) {
            for (int i = 0; i < durationData.width(); i++) {
                int id = j * durationData.width() + i + durationData.base();
                ItemStack itemStack = this.getItem(id);
                if (itemStack.isEmpty()) continue;
                litDuration = getBurnDuration(level.fuelValues(), itemStack);
                return id;
            }
        } return -1;
    }

    protected boolean checkIsInputEmpty() {
        int size = Alloy.inputData.get(levels)[0].size();
        boolean bool = true;
        for (int i = 0; i < size; i++) {
            bool = (!handler.getStackInSlot(i).isEmpty()) && bool;
        }
        return bool;
    }

    protected NonNullList<ItemStack> inputs() {
        int size = Alloy.inputData.get(levels)[0].size();
        NonNullList<ItemStack> list =NonNullList.withSize(size, ItemStack.EMPTY);
        for (int i = 0; i < size; i++) {
            list.set(i, handler.getStackInSlot(i));
        }
        return list;
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemStack = getItem(slot);
        boolean bl = !stack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, stack);
        super.setItem(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        if (0 <= slot && slot <= Alloy.inputData.get(levels)[0].size() && !bl) {
            Level var6 = this.level;
            if (var6 instanceof ServerLevel serverLevel) {
                this.cookingTotalTime = getTotalCookTime(serverLevel);
                this.cookingProgress = 0;
                this.setChanged();
            }
        }
    }

    private boolean canBurn(RegistryAccess access, @Nullable RecipeHolder<MeltingRecipe> recipe, MeltingInput input) {
        int maxStackSize = getMaxStackSize();
        if (!checkIsInputEmpty() && recipe != null) {
            ItemStack result = recipe.value().assemble(input, access);
            if (result.isEmpty()) return false;
            ItemStack itemStack2 = getItem(37);
            if (itemStack2.isEmpty()) {
                return true;
            } else if (!ItemStack.isSameItemSameComponents(itemStack2, result)) {
                return false;
            } else if (itemStack2.getCount() < maxStackSize && itemStack2.getCount() < itemStack2.getMaxStackSize()) {
                return true;
            } else {
                return itemStack2.getCount() < result.getMaxStackSize();
            }
        }
        return false;
    }

    private boolean burn(RegistryAccess registryAccess, @Nullable RecipeHolder<MeltingRecipe> recipe, MeltingInput recipeInput) {
        if (recipe != null && canBurn(registryAccess, recipe, recipeInput)) {
            ItemStack itemStack2 = recipe.value().assemble(recipeInput, registryAccess);
            ItemStack itemStack3 = getItem(37);
            if (itemStack3.isEmpty()) {
                setItem(37, itemStack2.copy());
            } else if (ItemStack.isSameItemSameComponents(itemStack3, itemStack2)) {
                itemStack3.grow(1);
            }
            materialShrink(recipeInput);
            return true;
        } else {
            return false;
        }
    }

    private void materialShrink(MeltingInput recipeInput) {
        for (int id: recipeInput.ingredientIds()) {
            getItem(id).shrink(1);
        }
    }

    private int getTotalCookTime(ServerLevel level) {
        MeltingInput meltingInput = new MeltingInput(inputs(), levels);
        return quickCheck.getRecipeFor(meltingInput, level).map((recipeHolder) -> recipeHolder.value().cookingTime()).orElse(200);
    }
}
