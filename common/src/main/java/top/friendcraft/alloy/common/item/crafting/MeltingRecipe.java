package top.friendcraft.alloy.common.item.crafting;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import top.friendcraft.alloy.Alloy;

import java.util.List;

public class MeltingRecipe implements Recipe<MeltingInput> {
    private Logger logger = LogUtils.getLogger();
    public final String group;
    public final ItemStack result;
    public final List<Ingredient> ingredients;
    public final float experience;
    public final int cookingTime;
    public final int minLevels;
    @Nullable
    private PlacementInfo placementInfo;

    public MeltingRecipe(String group, ItemStack result, List<Ingredient> ingredients, float experience, int cookingTime, int minLevels) {
        logger.info("Successfully Register");
        logger.info("First Item: "+ingredients.getFirst().items().getFirst().value());
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.experience = experience;
        this.cookingTime = cookingTime;
        this.minLevels = minLevels;
    }

    @Override
    public boolean matches(MeltingInput input, Level level) {
        if (input.levels() < this.minLevels || input.ingredientCount() != this.ingredients.size()) {
            return false;
        }
        else {
            if (input.ingredientCount() != this.ingredients.size()) return false;
            boolean bool = true;
            for (int i = 0; i < this.ingredients.size(); i++) {
                bool = bool && this.ingredients.get(i).test(input.getItem(i));
            }
            return bool;
        }
    }


    public float experience() {
        return this.experience;
    }

    public int cookingTime() {
        return this.cookingTime;
    }

    @Override
    @NotNull
    public ItemStack assemble(MeltingInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends Recipe<MeltingInput>> getSerializer() {
        return Alloy.serializer.get();
    }

    @Override
    @NotNull
    public RecipeType<? extends Recipe<MeltingInput>> getType() {
        return Alloy.recipe.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredients);
        }

        return this.placementInfo;
    }

    @Override
    @NotNull
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    public static final class Serializer implements RecipeSerializer<MeltingRecipe> {
        public static final int defaultCookingTime = 800;

        @Override
        public MapCodec<MeltingRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                    Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
                    Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(recipe -> recipe.experience),
                    Codec.INT.fieldOf("cookingtime").orElse(defaultCookingTime).forGetter(recipe -> recipe.cookingTime),
                    Codec.INT.fieldOf("minlevel").orElse(1).forGetter(recipe -> recipe.minLevels)
            ).apply(instance, MeltingRecipe::new));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MeltingRecipe> streamCodec() {
            return StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
                    ItemStack.STREAM_CODEC, recipe -> recipe.result,
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.ingredients,
                    ByteBufCodecs.FLOAT, recipe -> recipe.experience,
                    ByteBufCodecs.INT, recipe -> recipe.cookingTime,
                    ByteBufCodecs.INT, recipe -> recipe.minLevels,
            MeltingRecipe::new);
        }
    }
}
