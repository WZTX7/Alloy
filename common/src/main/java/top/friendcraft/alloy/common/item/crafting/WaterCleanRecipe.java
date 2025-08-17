package top.friendcraft.alloy.common.item.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class WaterCleanRecipe extends SingleItemRecipe {
    public WaterCleanRecipe(String group, Ingredient input, ItemStack result) {
        super(group, input, result);
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return null;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public static final class Serializer extends SingleItemRecipe.Serializer<WaterCleanRecipe> {
        public Serializer() {
            super(WaterCleanRecipe::new);
        }
    }
}
