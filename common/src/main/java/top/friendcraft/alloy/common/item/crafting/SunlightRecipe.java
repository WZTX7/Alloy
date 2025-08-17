package top.friendcraft.alloy.common.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import top.friendcraft.alloy.Alloy;

import java.util.function.Function;

public class SunlightRecipe extends SingleItemRecipe {
    private int mixingTime;
    public SunlightRecipe(String group, Ingredient input, ItemStack result, int mixingTime) {
        super(group, input, result);
        this.mixingTime = mixingTime;
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return Alloy.sunlight_serializer.get();
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return Alloy.sunlight_recipe.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    @Override
    public ItemStack result() {
        return super.result();
    }

    public int mixingTime() {
        return this.mixingTime;
    }

    public static final class Serializer implements RecipeSerializer<SunlightRecipe> {
        public static final int defaultCookingTime = 200;
        @Override
        @NotNull
        public MapCodec<SunlightRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(SingleItemRecipe::group),
                    Ingredient.CODEC.fieldOf("input").forGetter(SingleItemRecipe::input),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SunlightRecipe::result),
                    Codec.INT.fieldOf("mixingTime").orElse(defaultCookingTime).forGetter(SunlightRecipe::mixingTime)
            ).apply(instance, SunlightRecipe::new));
        }

        @Override
        @NotNull
        public StreamCodec<RegistryFriendlyByteBuf, SunlightRecipe> streamCodec() {
            Function<SunlightRecipe, String> var10002 = SingleItemRecipe::group;
            Function<SunlightRecipe, Ingredient> var10006 = SingleItemRecipe::input;
            Function<SunlightRecipe, ItemStack> var10008 = SunlightRecipe::result;
            Function<SunlightRecipe, Integer> var10012 = SunlightRecipe::mixingTime;
            return StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, var10002,
                    Ingredient.CONTENTS_STREAM_CODEC, var10006,
                    ItemStack.STREAM_CODEC, var10008,
                    ByteBufCodecs.INT, var10012,
            SunlightRecipe::new);
        }
    }
}
