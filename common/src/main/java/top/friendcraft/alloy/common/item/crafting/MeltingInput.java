package top.friendcraft.alloy.common.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import top.friendcraft.alloy.common.item.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class MeltingInput implements RecipeInput {

    private final ItemStackHandler inputData;
    private final List<Integer> unemptyIds;
    private final int levels;
    private final int ingredientCount;

    public MeltingInput(List<ItemStack> inputData, int furnaceLevels) {
        this.inputData = new ItemStackHandler(NonNullList.of(ItemStack.EMPTY, inputData.toArray(new ItemStack[0])));
        this.unemptyIds = new ArrayList<>();
        this.levels = furnaceLevels;
        int i = 0;
        for (int j = 0; j < inputData.size(); j++) {
            ItemStack itemStack = inputData.get(j);
            if (!itemStack.isEmpty()) {
                ++i;
                this.unemptyIds.add(j);
            }
        }

        this.ingredientCount = i;
    }

    @Override
    public ItemStack getItem(int index) {
        return inputData.getStackInSlot(index);
    }

    public ItemStack getFirst() {
        return inputData.getStackInSlot(0);
    }

    @Override
    public int size() {
        return inputData.getSlots();
    }

    public int levels() {
        return levels;
    }

    public int ingredientCount() {
        return this.ingredientCount;
    }

    public List<Integer> ingredientIds() {
        return unemptyIds;
    }

    public boolean equals(Object object) {
        if (object == this) return true;
        else if (object instanceof MeltingInput input) {
            return this.ingredientCount == input.ingredientCount && ItemStack.listMatches(this.inputData.getStacks(), input.inputData.getStacks());
        } else return false;
    }
}
