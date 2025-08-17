package top.friendcraft.alloy.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DaylightDetectorBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.friendcraft.alloy.Alloy;
import top.friendcraft.alloy.common.item.ItemStackHandler;
import top.friendcraft.alloy.common.item.crafting.SunlightRecipe;

import java.util.Objects;

public class SunlightCollectorEntity extends BaseContainerBlockEntity implements Tickable {
    ItemStackHandler handler = new ItemStackHandler(2);
    int mixProgress;
    int mixTotalTime;
    int sunlight;
    int ticker = 0, killTicker = 0;
    protected final ContainerData dataAccess;
    private final RecipeManager.CachedCheck<SingleRecipeInput, SunlightRecipe> quickCheck;

    public SunlightCollectorEntity(BlockPos pos, BlockState blockState) {
        super(Alloy.sunlight_collector_entity.get(), pos, blockState);
        this.dataAccess = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SunlightCollectorEntity.this.mixProgress;
                    case 1 -> SunlightCollectorEntity.this.mixTotalTime;
                    case 2 -> SunlightCollectorEntity.this.sunlight;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SunlightCollectorEntity.this.mixProgress = value;
                    case 1 -> SunlightCollectorEntity.this.mixTotalTime = value;
                    case 2 -> SunlightCollectorEntity.this.sunlight = value;
                }
                ;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        this.quickCheck = RecipeManager.createCheck(Alloy.sunlight_recipe.get());
    }

    @Override
    @NotNull
    protected Component getDefaultName() {
        return Component.translatable("container.sunlight_collector");
    }

    @Override
    @NotNull
    protected NonNullList<ItemStack> getItems() {
        return handler.getStacks();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        handler = new ItemStackHandler(items);
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
        ContainerHelper.loadAllItems(compoundTag, handler.getStacks(), provider);
        this.mixProgress = compoundTag.getShort("MixTime");
        this.mixTotalTime = compoundTag.getShort("MixTimeTotal");
        this.sunlight = compoundTag.getShort("Sunlight");
    }

    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putShort("MixTime", (short) this.mixProgress);
        compoundTag.putShort("MixTimeTotal", (short) this.mixTotalTime);
        compoundTag.putShort("Sunlight", (short) this.sunlight);
        ContainerHelper.saveAllItems(compoundTag, handler.getStacks(), provider);
    }

    @Override
    public void tick(ServerLevel level, BlockPos pos, BlockState state) {
        int tickerLife = 5;
        BlockState up = level.getBlockState(pos.above());
        if (Objects.equals(
                up.getBlock().arch$registryName(), ResourceLocation.withDefaultNamespace("daylight_detector"))
        ) if (up.getValue(DaylightDetectorBlock.POWER) / 3 != 0)
            tickerLife = 5 / (up.getValue(DaylightDetectorBlock.POWER) / 3);
        this.ticker++;
        if (ticker >= tickerLife) {
            sunlight++;
            ticker = 0;
        }
        if (this.sunlight > 200) this.sunlight = 200;
        boolean isStatusChanged = false;
        ItemStack result = this.getItem(1);
        ItemStack in = this.getItem(0);
        boolean bl3 = !in.isEmpty();
        if (!isLit() && !bl3) {
            if (mixProgress > 0) {
                mixProgress = Mth.clamp(mixProgress - 2, 0, mixTotalTime);
            }
        } else {
            SingleRecipeInput singleRecipeInput = new SingleRecipeInput(in);
            RecipeHolder<SunlightRecipe> recipeHolder;
            if (bl3) {
                recipeHolder = quickCheck.getRecipeFor(singleRecipeInput, level).orElse(null);
            } else {
                recipeHolder = null;
            }

            int i = getMaxStackSize();
            if (isLit() && canBurn(recipeHolder, singleRecipeInput, i)) {
                ++killTicker;
                if (killTicker == 3) {
                    sunlight--;
                    killTicker = 0;
                }
                if (sunlight < 0) sunlight = 0;
                ++mixProgress;
                if (mixProgress == mixTotalTime) {
                    mixProgress = 0;
                    mixTotalTime = getTotalMixTime(level);
                    burn(recipeHolder, singleRecipeInput, i);
                    isStatusChanged = true;
                }
            } else {
                mixProgress = 0;
            }
        }
        if (isStatusChanged) {
            setChanged(level, pos, state);
        }
    }


    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemStack = getItem(slot);
        boolean bl = !stack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, stack);
        super.setItem(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        if (slot == 0 && !bl) {
            Level var6 = this.level;
            if (var6 instanceof ServerLevel serverLevel) {
                this.mixTotalTime = getTotalMixTime(serverLevel);
                this.mixProgress = 0;
                this.setChanged();
            }
        }
    }

    private int getTotalMixTime(ServerLevel level) {
        SingleRecipeInput meltingInput = new SingleRecipeInput(handler.getStackInSlot(0));
        return quickCheck.getRecipeFor(meltingInput, level).map((recipeHolder) -> recipeHolder.value().mixingTime()).orElse(200);
    }

    private boolean canBurn(@Nullable RecipeHolder<? extends SunlightRecipe> recipe, SingleRecipeInput recipeInput, int maxStackSize) {
        NonNullList<ItemStack> items = this.handler.getStacks();
        RegistryAccess registryAccess = this.level.registryAccess();
        if (!items.getFirst().isEmpty() && recipe != null) {
            ItemStack itemStack = recipe.value().assemble(recipeInput, registryAccess);
            if (itemStack.isEmpty()) {
                return false;
            } else {
                ItemStack itemStack2 = items.get(1);
                if (itemStack2.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItemSameComponents(itemStack2, itemStack)) {
                    return false;
                } else if (itemStack2.getCount() < maxStackSize && itemStack2.getCount() < itemStack2.getMaxStackSize()) {
                    return true;
                } else {
                    return itemStack2.getCount() < itemStack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private boolean burn(@Nullable RecipeHolder<? extends SunlightRecipe> recipe, SingleRecipeInput recipeInput, int maxStackSize) {
        RegistryAccess registryAccess = this.level.registryAccess();
        NonNullList<ItemStack> items = this.handler.getStacks();
        if (recipe != null && canBurn(recipe, recipeInput, maxStackSize)) {
            ItemStack itemStack = items.get(0);
            ItemStack itemStack2 = recipe.value().assemble(recipeInput, registryAccess);
            ItemStack itemStack3 = items.get(1);
            if (itemStack3.isEmpty()) {
                items.set(1, itemStack2.copy());
            } else if (ItemStack.isSameItemSameComponents(itemStack3, itemStack2)) {
                itemStack3.grow(1);
            }
            itemStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    private boolean isLit() {
        return this.sunlight > 0;
    }
}
